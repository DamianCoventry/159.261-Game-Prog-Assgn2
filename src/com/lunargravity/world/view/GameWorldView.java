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
import com.lunargravity.engine.core.IEngine;
import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.physics.CollisionMeshCache;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.timeouts.TimeoutManager;
import com.lunargravity.engine.widgetsystem.WidgetCreateInfo;
import com.lunargravity.world.controller.GameWorldController;
import com.lunargravity.world.controller.IGameWorldControllerObserver;
import com.lunargravity.world.model.Crate;
import com.lunargravity.world.model.IGameWorldModel;
import com.lunargravity.world.model.Player;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.ArrayList;

public class GameWorldView implements IGameWorldView, IGameWorldControllerObserver, ISceneAssetOwner {
    private static final float CAMERA_Z_DISTANCE = 15.0f;
    private static final int MIN_EXPLOSION_FORCE = 250;
    private static final int MAX_EXPLOSION_FORCE = 400;

    private final IEngine _engine;
    private final Renderer _renderer;
    private final IGameWorldModel _model;
    
    private final DisplayMeshCache _displayMeshCache;
    private DisplayMesh _worldDisplayMesh;
    private final CollisionMeshCache _collisionMeshCache;
    private final MaterialCache _materialCache;
    private final TextureCache _textureCache;
    private final Vector3f _jomlVector;
    private final com.jme3.math.Vector3f _jmeVector;

    private static final com.jme3.math.Vector3f PREVENT_XY_AXES_ROTATION = new com.jme3.math.Vector3f(0.0f, 0.0f, 1.0f);
    private static final com.jme3.math.Vector3f PREVENT_Z_AXIS_MOVEMENT = new com.jme3.math.Vector3f(1.0f, 1.0f, 0.0f);

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

    private final ArrayList<RigidBodyObject> _rigidBodyObjects;
    private final ArrayList<RigidBodyObject> _deadRigidBodyObjects;
    private final ArrayList<PhysicsRigidBody> _deliveryZoneRigidBodies;
    private final ArrayList<RigidBodyObject> _debrisRigidBodies;
    private PhysicsRigidBody _worldRigidBody;
    private com.jme3.math.Transform _physicsTransform;
    private com.jme3.math.Matrix4f _physicsMatrix;

    private final Transform[] _cameras;
    private final ArrayList<Vector3f> _crateStartPoints;
    private final ArrayList<Vector3f> _deliveryZoneStartPoints;
    private final Vector3f[] _lunarLanderStartPoints;
    private final Matrix4f _mvpMatrix;

    public GameWorldView(IEngine engine, IGameWorldModel model) {
        _engine = engine;
        _renderer = engine.getRenderer();
        _model = model;
        
        _displayMeshCache = new DisplayMeshCache();

        _collisionMeshCache = new CollisionMeshCache();
        _materialCache = new MaterialCache();
        _textureCache = new TextureCache();

        _mvpMatrix = new Matrix4f();
        _rigidBodyObjects = new ArrayList<>();
        _deadRigidBodyObjects = new ArrayList<>();
        _deliveryZoneRigidBodies = new ArrayList<>();
        _debrisRigidBodies = new ArrayList<>();
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
    public void initialLoadCompleted() {
        int width = (int)_engine.getDesktopWindowWidth();
        int height = (int)_engine.getDesktopWindowHeight();
        if (_model.getNumPlayers() == 1) {
            _renderer.setPerspectiveViewports(new ViewportConfig[] {
                    ViewportConfig.createFullWindow(width, height)
            });
        }
        else {
            _renderer.setPerspectiveViewports(new ViewportConfig[] {
                    ViewportConfig.createDefault(0, 0, width/2, height),
                    ViewportConfig.createDefault(width/2, 0, width/2, height)
            });
        }

        _rigidBodyObjects.clear();
        _deadRigidBodyObjects.clear();
        _debrisRigidBodies.clear();
        _model.clearCrates();
        _model.clearDeliveryZones();

        createCrateRigidBodies();
        createDeliveryZoneRigidBodies();
        createLunarLanderRigidBodies();
        createDebrisRigidBodies();
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
        for (var rbo : _rigidBodyObjects) {
            synchroniseDisplayMeshWithCollisionMesh(rbo);
        }

        for (var rbo : _debrisRigidBodies) {
            if (rbo._active) {
                synchroniseDisplayMeshWithCollisionMesh(rbo);
            }
        }

        attachCamerasToLunarLanders();

        if (_worldDisplayMesh != null) {
            _mvpMatrix.identity().mul(projectionMatrix).mul(_cameras[viewport].getViewMatrix());
            _worldDisplayMesh.draw(_renderer, _renderer.getDiffuseTextureProgram(), _mvpMatrix);
        }

        for (var rbo : _rigidBodyObjects) {
            _mvpMatrix.identity().mul(projectionMatrix).mul(_cameras[viewport].getViewMatrix()).mul(rbo._modelMatrix);
            rbo._displayMesh.draw(_renderer, _renderer.getDiffuseTextureProgram(), _mvpMatrix);
        }

        for (var rbo : _debrisRigidBodies) {
            if (rbo._active) {
                _mvpMatrix.identity().mul(projectionMatrix).mul(_cameras[viewport].getViewMatrix()).mul(rbo._modelMatrix);
                rbo._displayMesh.draw(_renderer, _renderer.getDiffuseTextureProgram(), _mvpMatrix);
            }
        }
    }

    @Override
    public void drawView2d(Matrix4f projectionMatrix) {
        // TODO
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
        if (_deadRigidBodyObjects.isEmpty()) {
            return;
        }

        for (var rbo : _deadRigidBodyObjects) {
            _engine.getPhysicsSpace().remove(rbo._rigidBody);
        }
        _deadRigidBodyObjects.clear();

        if (_model.getPlayerState(0).getRigidBody() != null) {
            _model.getPlayerState(0).getRigidBody().activate();
        }
        if (_model.getNumPlayers() == 2 && _model.getPlayerState(1).getRigidBody() != null) {
            _model.getPlayerState(1).getRigidBody().activate();
        }
    }

    @Override
    public void resetState() {
        resetRigidBodyObjects(_rigidBodyObjects); // <-- player ship rigid bodies, and crate rigid bodies, are in here
        resetRigidBodyObjects(_deadRigidBodyObjects);
        resetRigidBodyObjects(_debrisRigidBodies);

        for (int i = 0; i < _model.getNumPlayers(); ++i){
            _model.getPlayerState(i).setRigidBody(null);
        }

        for (var rigidBody : _deliveryZoneRigidBodies) {
            _engine.getPhysicsSpace().remove(rigidBody);
        }
        _deliveryZoneRigidBodies.clear();

        _engine.getPhysicsSpace().remove(_worldRigidBody);
        _worldRigidBody = null;

        _worldDisplayMesh = null;
        _displayMeshCache.clear();
        _collisionMeshCache.clear();
        _materialCache.clear();
        _textureCache.clear();
        _crateStartPoints.clear();
        _deliveryZoneStartPoints.clear();
        _physicsTransform = new com.jme3.math.Transform();
        _physicsMatrix = new com.jme3.math.Matrix4f();

        _model.resetPlayers();
        _model.clearCrates();
        _model.clearDeliveryZones();
    }

    @Override
    public void freeResources() {
        _collisionMeshCache.clear();
    }

    @Override
    public void doGameWorldViewStuff() {
        // TODO
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
        // TODO
    }

    @Override
    public void crateCollectionCompleted(Crate crate) {
        removeCrate(crate);
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
    public void allCratesDelivered() {
        // TODO
    }

    @Override
    public void playerShipTookDamage(int player, int hitPointsDamage, int hitPointsRemaining) {
        // TODO: draw sparks?
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
        removePlayerShipRigidBody(player);
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

    private void removePlayerShipRigidBody(Player player) {
        for (var rbo : _rigidBodyObjects) {
            if (rbo._rigidBody == player.getRigidBody()) {
                _deadRigidBodyObjects.add(rbo);
                _rigidBodyObjects.remove(rbo);
                rbo._rigidBody.setUserObject(null);
                player.setRigidBody(null);
                break;
            }
        }
    }

    private void spawnPlayerShipDebris(Player player) {
        for (var rbo : _debrisRigidBodies) {
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

    private void createLunarLanderRigidBodies() {
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

        _rigidBodyObjects.add(new RigidBodyObject(rigidBody, displayMesh, true));
        _engine.getPhysicsSpace().addCollisionObject(rigidBody);

        Player player = _model.getPlayerState(i);
        player.reset();
        player.setRigidBody(rigidBody);
        rigidBody.setUserObject(player);
    }

    private void createCrateRigidBodies() {
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
        rigidBody.setPhysicsLocation(new com.jme3.math.Vector3f(point.x, point.y, point.z));

        rigidBody.setLinearFactor(PREVENT_Z_AXIS_MOVEMENT);

        _rigidBodyObjects.add(new RigidBodyObject(rigidBody, displayMesh, true));
        _engine.getPhysicsSpace().addCollisionObject(rigidBody);

        if (existingCrate == null) {
            _model.addCrate(rigidBody);
        }
        else {
            existingCrate.setRigidBody(rigidBody);
            rigidBody.setUserObject(existingCrate);
        }
    }

    private void createDeliveryZoneRigidBodies() {
        CollisionShape collisionMesh = _collisionMeshCache.getByExactName("DeliveryZone.BoxShape");
        if (collisionMesh != null) {
            for (var point : _deliveryZoneStartPoints) {
                PhysicsRigidBody rigidBody = new PhysicsRigidBody(collisionMesh, PhysicsBody.massForStatic);
                rigidBody.setPhysicsLocation(new com.jme3.math.Vector3f(point.x, point.y, point.z));
                _engine.getPhysicsSpace().addCollisionObject(rigidBody);
                _deliveryZoneRigidBodies.add(rigidBody);
                _model.addDeliveryZone(rigidBody);
            }
        }
    }

    // We'll allocate them now, but not insert them into the simulation
    private void createDebrisRigidBodies() {
        int i = 1;
        DisplayMesh displayMesh = _displayMeshCache.getByPartialName(String.format("Debris.Display.%03d", i));
        CollisionShape collisionMesh = _collisionMeshCache.getByPartialName(String.format("Debris.BoxShape.%03d", i));

        while (displayMesh != null && collisionMesh != null) {
            PhysicsRigidBody rigidBody = new PhysicsRigidBody(collisionMesh, 1.0f); // TODO: what about mass?
            RigidBodyObject rbo = new RigidBodyObject(rigidBody, displayMesh, false);
            _debrisRigidBodies.add(rbo); // save for later insertion

            displayMesh = _displayMeshCache.getByPartialName(String.format("Debris.Display.%03d", i));
            collisionMesh = _collisionMeshCache.getByPartialName(String.format("Debris.BoxShape.%03d", i));
            ++i;
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

    private void removeCrate(Crate crate) {
        for (var rbo : _rigidBodyObjects) {
            if (rbo._rigidBody.getUserObject() == crate) {
                crate.setRigidBody(null);
                rbo._rigidBody.setUserObject(null);
                _deadRigidBodyObjects.add(rbo);
                _rigidBodyObjects.remove(rbo);
                break;
            }
        }
    }
}
