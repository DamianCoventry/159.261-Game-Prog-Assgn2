//
// Lunar Gravity
//
// This game is based upon the Amiga video game Gravity Force that was
// released in 1989 by Stephan Wenzler
//
// https://www.mobygames.com/game/gravity-force
// https://www.youtube.com/watch?v=m9mFtCvnko8
//
// This implementation is Copyright (c) 2021, Damian Coventry
// All rights reserved
// Written for Massey University course 159.261 Game Programming (Assignment 2)
//

package com.lunargravity.world.view;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.objects.PhysicsBody;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.lunargravity.application.LargeNumberFont;
import com.lunargravity.engine.audio.SoundBuffer;
import com.lunargravity.engine.audio.SoundBufferCache;
import com.lunargravity.engine.audio.SoundSource;
import com.lunargravity.engine.core.IEngine;
import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.physics.CollisionMeshCache;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.timeouts.TimeoutManager;
import com.lunargravity.engine.widgetsystem.ImageWidget;
import com.lunargravity.engine.widgetsystem.Widget;
import com.lunargravity.engine.widgetsystem.WidgetCreateInfo;
import com.lunargravity.engine.widgetsystem.WidgetManager;
import com.lunargravity.world.controller.GameWorldController;
import com.lunargravity.world.controller.IGameWorldControllerObserver;
import com.lunargravity.world.model.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;

public class GameWorldView implements
        IGameWorldView,
        IGameWorldControllerObserver,
        ISceneAssetOwner,
        IPlayerShotObserver
{
    private static final float CAMERA_Z_DISTANCE = 15.0f;
    private static final int MIN_EXPLOSION_FORCE = 250;
    private static final int MAX_EXPLOSION_FORCE = 400;
    private static final int NUM_PLAYER_SHOTS = 20;
    private static final float PLAYER_SHOT_FORCE = 500.0f;
    private static final float PLAYER_SHOT_OFFSET = 0.75f; // Measured using Blender
    private static final int NUM_PLAYER_SHIP_HIT_SOUNDS = 4;

    private static final com.jme3.math.Vector3f ZERO_VELOCITY = new com.jme3.math.Vector3f(0, 0, 0);
    private static final com.jme3.math.Vector3f PREVENT_XY_AXES_ROTATION = new com.jme3.math.Vector3f(0.0f, 0.0f, 1.0f);
    private static final com.jme3.math.Vector3f PREVENT_Z_AXIS_MOVEMENT = new com.jme3.math.Vector3f(1.0f, 1.0f, 0.0f);
    private static final Vector4f WHITE = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    private static final String SINGLE_PLAYER_STATUS_BAR = "singlePlayerStatusBar";

    private final IEngine _engine;
    private final Renderer _renderer;
    private final IGameWorldModel _model;
    private final WidgetManager _widgetManager;
    private final LargeNumberFont _font;

    private final SoundBufferCache _soundBufferCache;
    private final DisplayMeshCache _displayMeshCache;
    private final CollisionMeshCache _collisionMeshCache;
    private final MaterialCache _materialCache;
    private final TextureCache _textureCache;
    private final Vector3f _jomlVector;
    private final com.jme3.math.Vector3f _jmeVector;

    private Widget _singlePlayerStatusBar;
    private DisplayMesh _worldDisplayMesh;

    private static class RigidBodyObject {
        public boolean _active;
        public int _timeoutId;
        public final PhysicsRigidBody _rigidBody;
        public final DisplayMesh _displayMesh;
        public final Matrix4f _modelMatrix;
        public RigidBodyObject(PhysicsRigidBody rigidBody, DisplayMesh displayMesh, boolean active) {
            _active = active;
            _timeoutId = 0;
            _rigidBody = rigidBody;
            _displayMesh = displayMesh;
            _modelMatrix = new Matrix4f();
        }
    }

    private final ArrayList<RigidBodyObject> _lunarLandersAndCrates;
    private final ArrayList<PhysicsRigidBody> _deliveryZones;
    private final ArrayList<RigidBodyObject> _debris;
    private final ArrayList<RigidBodyObject> _playerShots;
    private PhysicsRigidBody _worldRigidBody;
    private com.jme3.math.Transform _physicsTransform;
    private com.jme3.math.Matrix4f _physicsMatrix;

    private final Transform[] _cameras;
    private final ArrayList<Vector3f> _crateStartPoints;
    private final ArrayList<Vector3f> _deliveryZoneStartPoints;
    private final Vector3f[] _lunarLanderStartPoints;
    private final Matrix4f _mvpMatrix;

    private SoundSource _playerShipShoot;
    private SoundSource _playerShipExplode;
    private SoundSource[] _playerShipHits;
    private SoundSource _collectCrate;

    public GameWorldView(WidgetManager widgetManager, IEngine engine, IGameWorldModel model) throws IOException {
        _widgetManager = widgetManager;
        _engine = engine;
        _renderer = engine.getRenderer();
        _model = model;

        _displayMeshCache = new DisplayMeshCache();
        _soundBufferCache = new SoundBufferCache();

        _collisionMeshCache = new CollisionMeshCache();
        _materialCache = new MaterialCache();
        _textureCache = new TextureCache();
        _font = new LargeNumberFont(_renderer);

        _mvpMatrix = new Matrix4f();
        _lunarLandersAndCrates = new ArrayList<>();
        _deliveryZones = new ArrayList<>();
        _debris = new ArrayList<>();
        _playerShots = new ArrayList<>();

        _physicsTransform = new com.jme3.math.Transform();
        _physicsMatrix = new com.jme3.math.Matrix4f();
        _jomlVector = new Vector3f();
        _jmeVector = new com.jme3.math.Vector3f();

        _cameras = new Transform[2];
        _cameras[0] = new Transform();
        _cameras[1] = new Transform();

        _lunarLanderStartPoints = new Vector3f[2];
        _lunarLanderStartPoints[0] = new Vector3f(0.0f, 0.0f, 0.0f);
        _lunarLanderStartPoints[1] = new Vector3f(0.0f, 0.0f, 0.0f);

        _crateStartPoints = new ArrayList<>();
        _deliveryZoneStartPoints = new ArrayList<>();
    }

    @Override
    public void initialLoadCompleted() throws UnsupportedAudioFileException, IOException {
        int width = (int) _engine.getDesktopWindowWidth();
        int height = (int) _engine.getDesktopWindowHeight();
        if (_model.getNumPlayers() == 1) {
            _renderer.setPerspectiveViewports(new ViewportConfig[]{
                    ViewportConfig.createFullWindow(width, height)
            });
        } else {
            _renderer.setPerspectiveViewports(new ViewportConfig[]{
                    ViewportConfig.createDefault(0, 0, width / 2, height),
                    ViewportConfig.createDefault(width / 2, 0, width / 2, height)
            });
        }

        _lunarLandersAndCrates.clear();
        _debris.clear();
        _playerShots.clear();
        _model.clearCrates();
        _model.clearDeliveryZones();

        createCrates();
        createDeliveryZones();
        createLunarLanders();
        createDebris();
        createPlayerShots();

        loadSounds();
    }

    private void loadSounds() throws UnsupportedAudioFileException, IOException {
        SoundBuffer soundBuffer = SoundBuffer.fromFile("sounds/PlayerShipShoot.wav");
        _soundBufferCache.add(soundBuffer);
        _playerShipShoot = new SoundSource(false, false);
        _playerShipShoot.setBuffer(soundBuffer);

        soundBuffer = SoundBuffer.fromFile("sounds/PlayerShipExploding.wav");
        _soundBufferCache.add(soundBuffer);
        _playerShipExplode = new SoundSource(false, false);
        _playerShipExplode.setBuffer(soundBuffer);

        _playerShipHits = new SoundSource[NUM_PLAYER_SHIP_HIT_SOUNDS];
        for (int i = 0; i < NUM_PLAYER_SHIP_HIT_SOUNDS; ++i) {
            soundBuffer = SoundBuffer.fromFile(String.format("sounds/Hit%d.wav", i));
            _soundBufferCache.add(soundBuffer);
            _playerShipHits[i] = new SoundSource(false, false);
            _playerShipHits[i].setBuffer(soundBuffer);
        }

        soundBuffer = SoundBuffer.fromFile("sounds/CollectCrate.wav");
        _soundBufferCache.add(soundBuffer);
        _collectCrate = new SoundSource(false, false);
        _collectCrate.setBuffer(soundBuffer);
    }

    private void unloadSounds() {
        if (_playerShipShoot != null) {
            _playerShipShoot.freeResources();
        }
        if (_playerShipExplode != null) {
            _playerShipExplode.freeResources();
        }
        if (_playerShipHits != null) {
            for (var a : _playerShipHits) {
                a.freeResources();
            }
        }
        if (_collectCrate != null) {
            _collectCrate.freeResources();
        }
        _soundBufferCache.freeResources();
    }

    @Override
    public void viewThink() {
        // TODO
    }

    @Override
    public void drawView3d(int viewport, Matrix4f projectionMatrix) {
        if (viewport < 0 || viewport > 1) {
            return;
        }

        // Synchronise first so that we can attach a camera to each player's lunar lander
        for (var rbo : _lunarLandersAndCrates) {
            synchroniseDisplayMeshWithCollisionMesh(rbo);
        }

        for (var rbo : _debris) {
            if (rbo._active) {
                synchroniseDisplayMeshWithCollisionMesh(rbo);
            }
        }

        for (var rbo : _playerShots) {
            if (rbo._active) {
                synchroniseDisplayMeshWithCollisionMesh(rbo);
            }
        }

        attachCamerasToLunarLanders();

        if (_worldDisplayMesh != null) {
            _mvpMatrix.identity().mul(projectionMatrix).mul(_cameras[viewport].getViewMatrix());
            _worldDisplayMesh.draw(_renderer, _renderer.getDiffuseTextureProgram(), _mvpMatrix);
        }

        for (var rbo : _lunarLandersAndCrates) {
            _mvpMatrix.identity().mul(projectionMatrix).mul(_cameras[viewport].getViewMatrix()).mul(rbo._modelMatrix);
            rbo._displayMesh.draw(_renderer, _renderer.getDiffuseTextureProgram(), _mvpMatrix);
        }

        for (var rbo : _debris) {
            if (rbo._active) {
                _mvpMatrix.identity().mul(projectionMatrix).mul(_cameras[viewport].getViewMatrix()).mul(rbo._modelMatrix);
                rbo._displayMesh.draw(_renderer, _renderer.getDiffuseTextureProgram(), _mvpMatrix);
            }
        }

        for (var rbo : _playerShots) {
            if (rbo._active) {
                _mvpMatrix.identity().mul(projectionMatrix).mul(_cameras[viewport].getViewMatrix()).mul(rbo._modelMatrix);
                rbo._displayMesh.draw(_renderer, _renderer.getDiffuseTextureProgram(), _mvpMatrix);
            }
        }
    }

    @Override
    public void drawView2d(Matrix4f projectionMatrix) {
        if (!_widgetManager.isVisible(_singlePlayerStatusBar)) {
            return;
        }

        final float horizontalSpacing = 110.0f;
        float x = 30.0f;
        _font.drawNumber(projectionMatrix, _model.getEpisode()+1, x, 892.0f, 1.0f, WHITE); x += horizontalSpacing;
        _font.drawNumber(projectionMatrix, _model.getMission()+1, x, 892.0f, 1.0f, WHITE); x += horizontalSpacing;
        _font.drawNumber(projectionMatrix, _model.getPlayerState(0).getHitPoints(), x, 892.0f, 1.0f, WHITE); x += horizontalSpacing;
        _font.drawNumber(projectionMatrix, _model.getPlayerState(0).getFuel(), x, 892.0f, 1.0f, WHITE); x += horizontalSpacing;
        _font.drawNumber(projectionMatrix, _model.getShipsRemaining(0), x, 892.0f, 1.0f, WHITE); x += horizontalSpacing;
        _font.drawNumber(projectionMatrix, _model.getNumCratesRemaining(), x, 892.0f, 1.0f, WHITE); x += horizontalSpacing;
        _font.drawNumber(projectionMatrix, _model.getNumCratesCollected(), x, 892.0f, 1.0f, WHITE); x += horizontalSpacing;
        _font.drawNumber(projectionMatrix, _model.getNumCratesDelivered(), x, 892.0f, 1.0f, WHITE);

        // TODO: what to do about player two?
    }

    @Override
    public DisplayMeshCache getDisplayMeshCache() {
        return _displayMeshCache;
    }

    @Override
    public MaterialCache getMaterialCache() {
        return _materialCache;
    }

    @Override
    public TextureCache getTextureCache() {
        return _textureCache;
    }

    @Override
    public void onFrameEnd() {
        // Nothing to do
    }

    @Override
    public void resetState() {
        resetRigidBodyObjects(_lunarLandersAndCrates); // <-- player ship rigid bodies, and crate rigid bodies, are in here
        resetRigidBodyObjects(_debris);
        resetRigidBodyObjects(_playerShots);

        for (int i = 0; i < _model.getNumPlayers(); ++i){
            _model.getPlayerState(i).setRigidBody(null);
        }

        for (var rigidBody : _deliveryZones) {
            _engine.getPhysicsSpace().remove(rigidBody);
        }
        _deliveryZones.clear();

        _engine.getPhysicsSpace().remove(_worldRigidBody);
        _worldRigidBody = null;

        _worldDisplayMesh = null;
        _displayMeshCache.freeResources();
        _collisionMeshCache.clear();
        _materialCache.clear();
        _textureCache.freeResources();
        _crateStartPoints.clear();
        _deliveryZoneStartPoints.clear();
        _soundBufferCache.freeResources();
        _physicsTransform = new com.jme3.math.Transform();
        _physicsMatrix = new com.jme3.math.Matrix4f();

        _model.resetPlayers();
        _model.clearCrates();
        _model.clearDeliveryZones();
        _model.clearPlayerShots();

        unloadSounds();
    }

    @Override
    public void freeResources() {
        _collisionMeshCache.clear();
        _font.freeResources();
    }

    @Override
    public void showMissionStatusBar() {
        _widgetManager.hideAll();
        _widgetManager.show(_singlePlayerStatusBar, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void hideMissionStatusBar() {
        _widgetManager.hideAll();
    }

    @Override
    public void objectLoaded(String name, String type, Transform transform) {
        // TODO
    }

    @Override
    public void displayMeshLoaded(DisplayMesh displayMesh) {
        if (_model.getNumPlayers() == 1 && displayMesh.getName().equals("SinglePlayerStart")) {
            _cameras[0]._position = new Vector3f(displayMesh.getMidPoint()).add(new Vector3f(0.0f, 0.0f, CAMERA_Z_DISTANCE));
            _cameras[0].calculateViewMatrix();
            _lunarLanderStartPoints[0] = new Vector3f(displayMesh.getMidPoint());
        }
        else if (_model.getNumPlayers() == 2 && displayMesh.getName().startsWith("TwoPlayersStart")) {
            if (displayMesh.getName().endsWith("001")) {
                _cameras[0]._position = new Vector3f(displayMesh.getMidPoint()).add(new Vector3f(0.0f, 0.0f, CAMERA_Z_DISTANCE));
                _cameras[0].calculateViewMatrix();
                _lunarLanderStartPoints[0] = new Vector3f(displayMesh.getMidPoint());
            }
            else if (displayMesh.getName().endsWith("002")) {
                _cameras[1]._position = new Vector3f(displayMesh.getMidPoint()).add(new Vector3f(0.0f, 0.0f, CAMERA_Z_DISTANCE));
                _cameras[1].calculateViewMatrix();
                _lunarLanderStartPoints[1] = new Vector3f(displayMesh.getMidPoint());
            }
        }
        else if(displayMesh.getName().contains(".Display")) {
            if (_displayMeshCache.getByExactName(displayMesh.getName()) == null) {
                if (displayMesh.getName().equals("World.Display")) {
                    _worldDisplayMesh = displayMesh;
                }
                _displayMeshCache.add(displayMesh);
            }
        }
        else if (displayMesh.getName().startsWith("CrateStart.")) {
            _crateStartPoints.add(displayMesh.getMidPoint());
        }
        else if (displayMesh.getName().startsWith("DeliveryZoneStart.")) {
            _deliveryZoneStartPoints.add(displayMesh.getMidPoint());
        }
    }

    @Override
    public void collisionMeshLoaded(String name, CollisionShape collisionShape) {
        _collisionMeshCache.add(name, collisionShape);
        if (name.equals("World.MeshShape")) {
            _worldRigidBody = new PhysicsRigidBody(collisionShape, PhysicsBody.massForStatic);
            _engine.getPhysicsSpace().addCollisionObject(_worldRigidBody);
        }
    }

    @Override
    public void materialLoaded(Material material) {
        // TODO
    }

    @Override
    public void textureLoaded(GlTexture texture) {
        // TODO
    }

    @Override
    public void widgetLoaded(ViewportConfig viewportConfig, WidgetCreateInfo wci) throws IOException {
        if (wci == null) {
            System.out.print("GameWorldView.widgetLoaded() was passed a null WidgetCreateInfo object");
            return;
        }
        if (wci._id.equals(SINGLE_PLAYER_STATUS_BAR) && wci._type.equals("ImageWidget")) {
            _singlePlayerStatusBar = new Widget(viewportConfig, wci, new ImageWidget(_widgetManager));
        }
    }

    @Override
    public void crateCollectionCompleted(Crate crate) {
        removeCrate(crate);
        _collectCrate.play();
    }

    @Override
    public void crateCollectionAborted() {
        // TODO
    }

    @Override
    public void crateStartedDelivering(Crate crate, com.jme3.math.Vector3f playerPosition) {
        _jomlVector.x = playerPosition.x;
        _jomlVector.y = playerPosition.y;
        _jomlVector.z = playerPosition.z;
        createCrateRigidBody(_jomlVector, crate);
    }

    @Override
    public void crateDeliveryCompleted(Crate crate) {
        removeCrate(crate);
    }

    @Override
    public void respawnCrateAtStartPosition(Crate crate) {
        // TODO: play an animation?
        crate.respawnAtStartPosition();
    }

    @Override
    public void allCratesDelivered() {
        // TODO
    }

    @Override
    public void playerShipTookDamage(int player, int hitPointsDamage, int hitPointsRemaining) {
        // TODO: draw sparks?
        _playerShipHits[randomInteger(0, 3)].play();
    }

    @Override
    public void playerShipExploding(int i) {
        Player player = _model.getPlayerState(i);
        player.getRigidBody().getPhysicsLocation(_jmeVector);
        _jomlVector.x = _jmeVector.x;
        _jomlVector.y = _jmeVector.y;
        _jomlVector.z = _jmeVector.z;

        dropCollectedCrates(player);
        spawnPlayerShipDebris(player);
        removePlayerShip(player);

        _playerShipExplode.play();
    }

    @Override
    public void playerShotExploded(PlayerShot playerShot) {
        for (var a : _playerShots) {
            if (a._rigidBody.getUserObject() == playerShot) {
                a._active = false;
                a._rigidBody.setUserObject(null);
                if (_engine.getPhysicsSpace().contains(a._rigidBody)) {
                    _engine.getPhysicsSpace().removeCollisionObject(a._rigidBody);
                }
                break;
            }
        }
    }

    private void dropCollectedCrates(Player player) {
        if (!player.hasCollectedAtLeastOneCrate()) {
            return;
        }

        for (var crate : player.getCollectedCrates()) {
            crate.removeTimeouts(_engine.getTimeoutManager());
            crate.setState(Crate.State.IDLE);
            createCrateRigidBody(_jomlVector, crate);
        }

        player.clearCollectedCrates();
    }

    private void removePlayerShip(Player player) {
        for (var rbo : _lunarLandersAndCrates) {
            if (rbo._rigidBody == player.getRigidBody()) {
                _engine.getPhysicsSpace().removeCollisionObject(rbo._rigidBody);
                rbo._rigidBody.setUserObject(null);
                player.setRigidBody(null);
                _lunarLandersAndCrates.remove(rbo);
                break;
            }
        }
    }

    private void removeCrate(Crate crate) {
        for (var rbo : _lunarLandersAndCrates) {
            if (rbo._rigidBody.getUserObject() == crate) {
                _engine.getPhysicsSpace().removeCollisionObject(rbo._rigidBody);
                rbo._rigidBody.setUserObject(null);
                crate.setRigidBody(null, null);
                _lunarLandersAndCrates.remove(rbo);

                if (_model.getPlayerState(0).getRigidBody() != null) {
                    _model.getPlayerState(0).getRigidBody().activate();
                }
                if (_model.getNumPlayers() == 2 && _model.getPlayerState(1).getRigidBody() != null) {
                    _model.getPlayerState(1).getRigidBody().activate();
                }
                break;
            }
        }
    }

    private void spawnPlayerShipDebris(Player player) {
        for (var rbo : _debris) {
            rbo._active = true;
            rbo._rigidBody.setPhysicsLocation(_jmeVector);
            rbo._timeoutId = _engine.getTimeoutManager().addTimeout(randomInteger(2500, 4500), callCount -> {
                rbo._active = false;
                rbo._timeoutId = 0;
                _engine.getPhysicsSpace().removeCollisionObject(rbo._rigidBody);
                return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
            });
            rbo._rigidBody.applyCentralForce(generateRandomForce());
            _engine.getPhysicsSpace().addCollisionObject(rbo._rigidBody);
        }
    }

    private com.jme3.math.Vector3f generateRandomForce() {
        int angle0 = randomInteger(0, 359);
        int angle1 = randomInteger(0, 359);
        float x = (float)Math.sin(angle0);
        float y = (float)Math.cos(angle0);
        float z = (float)Math.sin(angle1);
        var direction = new com.jme3.math.Vector3f(x, y, z).normalize();
        return direction.multLocal(randomInteger(MIN_EXPLOSION_FORCE, MAX_EXPLOSION_FORCE));
    }

    private int randomInteger(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    @Override
    public void playerShipDead(int player) {
        // TODO: what to draw?
    }

    @Override
    public void playerShipSpawned(int player) {
        createLunarLanderRigidBody(player);
    }

    @Override
    public void playerFiredWeapon(int id) {
        RigidBodyObject rbo = getUnusedPlayerShot();
        if (rbo == null) {
            return;
        }

        rbo._rigidBody.clearForces();
        rbo._rigidBody.setLinearVelocity(ZERO_VELOCITY);
        rbo._rigidBody.setAngularVelocity(ZERO_VELOCITY);

        Player player = _model.getPlayerState(id);
        var direction = player.calculateAndGetUpVector();
        var position = player.getRigidBody().getPhysicsLocation(null);

        _jmeVector.set(direction).multLocal(PLAYER_SHOT_OFFSET).addLocal(position);
        rbo._rigidBody.setPhysicsLocation(_jmeVector);

        _jmeVector.set(direction).multLocal(PLAYER_SHOT_FORCE);
        rbo._rigidBody.applyCentralForce(_jmeVector);

        rbo._active = true;
        _model.addPlayerShot(player, rbo._rigidBody);

        _engine.getPhysicsSpace().addCollisionObject(rbo._rigidBody);
        rbo._rigidBody.setGravity(new com.jme3.math.Vector3f(0.0f, 0.0f, 0.0f)); // Overwrite the physics space gravity

        _playerShipShoot.play();
    }

    private RigidBodyObject getUnusedPlayerShot() {
        for (var a : _playerShots) {
            if (!a._active) {
                return a;
            }
        }
        return null;
    }

    @Override
    public void playerWeaponCooledDown(int id) {
        // TODO: what to draw?
    }

    private void resetRigidBodyObjects(ArrayList<RigidBodyObject> rigidBodyObjects) {
        for (var rbo : rigidBodyObjects) {
            if (_engine.getPhysicsSpace().contains(rbo._rigidBody)) { // because debris always exists, but might not be in the physics space
                _engine.getPhysicsSpace().remove(rbo._rigidBody);
            }
            if (rbo._timeoutId != 0) {
                _engine.getTimeoutManager().removeTimeout(rbo._timeoutId);
                rbo._timeoutId = 0;
            }
        }
        rigidBodyObjects.clear();
    }

    private void createLunarLanders() {
        for (int i = 0; i < _model.getNumPlayers(); ++i) {
            createLunarLanderRigidBody(i);
        }
    }

    private void createLunarLanderRigidBody(int i) {
        DisplayMesh displayMesh = _displayMeshCache.getByExactName("LunarLander.Display");
        CollisionShape collisionMesh = _collisionMeshCache.getByExactName("LunarLander");
        if (displayMesh == null || collisionMesh == null) {
            return;
        }

        PhysicsRigidBody rigidBody = new PhysicsRigidBody(collisionMesh, GameWorldController.LUNAR_LANDER_MASS);

        Vector3f position = _lunarLanderStartPoints[i];
        rigidBody.setPhysicsLocation(new com.jme3.math.Vector3f(position.x, position.y, position.z));

        rigidBody.setLinearFactor(PREVENT_Z_AXIS_MOVEMENT);
        rigidBody.setAngularFactor(PREVENT_XY_AXES_ROTATION);

        _lunarLandersAndCrates.add(new RigidBodyObject(rigidBody, displayMesh, true));
        _engine.getPhysicsSpace().addCollisionObject(rigidBody);

        Player player = _model.getPlayerState(i);
        player.reset();
        player.setRigidBody(rigidBody);
        rigidBody.setUserObject(player);
    }

    private void createCrates() {
        for (var point : _crateStartPoints) {
            createCrateRigidBody(point, null);
        }
    }

    private void createCrateRigidBody(Vector3f point, Crate existingCrate) {
        DisplayMesh displayMesh = _displayMeshCache.getByExactName("Crate.Display");
        CollisionShape collisionMesh = _collisionMeshCache.getByExactName("Crate.BoxShape");
        if (displayMesh == null || collisionMesh == null) {
            return;
        }

        PhysicsRigidBody rigidBody = new PhysicsRigidBody(collisionMesh, 1.0f); // TODO: what about mass?
        com.jme3.math.Vector3f startPosition = new com.jme3.math.Vector3f(point.x, point.y, point.z);
        rigidBody.setPhysicsLocation(startPosition);

        rigidBody.setLinearFactor(PREVENT_Z_AXIS_MOVEMENT);

        _lunarLandersAndCrates.add(new RigidBodyObject(rigidBody, displayMesh, true));
        _engine.getPhysicsSpace().addCollisionObject(rigidBody);

        if (existingCrate == null) {
            _model.addCrate(rigidBody, startPosition);
        }
        else {
            existingCrate.setRigidBody(rigidBody, startPosition);
            rigidBody.setUserObject(existingCrate);
        }
    }

    private void createDeliveryZones() {
        CollisionShape collisionMesh = _collisionMeshCache.getByExactName("DeliveryZone.BoxShape");
        if (collisionMesh != null) {
            for (var point : _deliveryZoneStartPoints) {
                PhysicsRigidBody rigidBody = new PhysicsRigidBody(collisionMesh, PhysicsBody.massForStatic);
                rigidBody.setPhysicsLocation(new com.jme3.math.Vector3f(point.x, point.y, point.z));
                _engine.getPhysicsSpace().addCollisionObject(rigidBody);
                _deliveryZones.add(rigidBody);
                _model.addDeliveryZone(rigidBody);
            }
        }
    }

    // We'll allocate them now, but not insert them into the simulation
    private void createDebris() {
        int i = 1;
        DisplayMesh displayMesh = _displayMeshCache.getByPartialName(String.format("Debris.Display.%03d", i));
        CollisionShape collisionMesh = _collisionMeshCache.getByPartialName(String.format("Debris.BoxShape.%03d", i));

        while (displayMesh != null && collisionMesh != null) {
            PhysicsRigidBody rigidBody = new PhysicsRigidBody(collisionMesh, 1.0f); // TODO: what about mass?
            RigidBodyObject rbo = new RigidBodyObject(rigidBody, displayMesh, false);
            _debris.add(rbo); // save for later insertion

            displayMesh = _displayMeshCache.getByPartialName(String.format("Debris.Display.%03d", i));
            collisionMesh = _collisionMeshCache.getByPartialName(String.format("Debris.BoxShape.%03d", i));
            ++i;
        }
    }

    // We'll allocate them now, but not insert them into the simulation
    private void createPlayerShots() {
        DisplayMesh displayMesh = _displayMeshCache.getByExactName("Bullet.Display");
        CollisionShape collisionMesh = _collisionMeshCache.getByExactName("Bullet.SphereShape");
        if (displayMesh == null || collisionMesh == null) {
            return;
        }
        for (int i = 0; i < NUM_PLAYER_SHOTS; ++i) {
            PhysicsRigidBody rigidBody = new PhysicsRigidBody(collisionMesh, 1.0f); // TODO: what about mass?
            RigidBodyObject rbo = new RigidBodyObject(rigidBody, displayMesh, false);
            _playerShots.add(rbo); // save for later insertion
        }
    }

    private void attachCamerasToLunarLanders() {
        if (_model.getPlayerState(0).getRigidBody() != null) {
            _physicsTransform = _model.getPlayerState(0).getRigidBody().getMotionState().physicsTransform(_physicsTransform);
            _cameras[0]._position.x = _physicsTransform.getTranslation().x;
            _cameras[0]._position.y = _physicsTransform.getTranslation().y;
            _cameras[0]._position.z = _physicsTransform.getTranslation().z + CAMERA_Z_DISTANCE;
            _cameras[0].calculateViewMatrix();
        }

        if (_model.getNumPlayers() == 2 && _model.getPlayerState(1).getRigidBody() != null) {
            _physicsTransform = _model.getPlayerState(1).getRigidBody().getMotionState().physicsTransform(_physicsTransform);
            _cameras[1]._position.x = _physicsTransform.getTranslation().x;
            _cameras[1]._position.y = _physicsTransform.getTranslation().y;
            _cameras[1]._position.z = _physicsTransform.getTranslation().z + CAMERA_Z_DISTANCE;
            _cameras[1].calculateViewMatrix();
        }
    }

    private void synchroniseDisplayMeshWithCollisionMesh(RigidBodyObject rbo) {
        _physicsTransform = rbo._rigidBody.getMotionState().physicsTransform(_physicsTransform);
        _physicsMatrix = _physicsTransform.toTransformMatrix(_physicsMatrix);

        rbo._modelMatrix.m00(_physicsMatrix.m00);
        rbo._modelMatrix.m01(_physicsMatrix.m10);
        rbo._modelMatrix.m02(_physicsMatrix.m20);
        rbo._modelMatrix.m03(_physicsMatrix.m30);

        rbo._modelMatrix.m10(_physicsMatrix.m01);
        rbo._modelMatrix.m11(_physicsMatrix.m11);
        rbo._modelMatrix.m12(_physicsMatrix.m21);
        rbo._modelMatrix.m13(_physicsMatrix.m31);

        rbo._modelMatrix.m20(_physicsMatrix.m02);
        rbo._modelMatrix.m21(_physicsMatrix.m12);
        rbo._modelMatrix.m22(_physicsMatrix.m22);
        rbo._modelMatrix.m23(_physicsMatrix.m32);

        rbo._modelMatrix.m30(_physicsMatrix.m03);
        rbo._modelMatrix.m31(_physicsMatrix.m13);
        rbo._modelMatrix.m32(_physicsMatrix.m23);
        rbo._modelMatrix.m33(_physicsMatrix.m33);
    }
}
