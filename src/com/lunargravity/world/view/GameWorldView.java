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

import static org.lwjgl.opengl.GL11C.glDepthMask;

public class GameWorldView implements
        IGameWorldView,
        IGameWorldControllerObserver,
        ISceneAssetOwner,
        IPlayerShotObserver
{
    private static final float CAMERA_Z_DISTANCE = 15.0f;
    private static final float PLAYER_SHOT_FORCE = 500.0f;
    private static final float PLAYER_SHOT_OFFSET = 0.75f; // Measured using Blender
    private static final float MIN_IMPULSE_TO_PLAY_SOUND = 2.0f;
    private static final float EXPLOSION_FLASH_SIZE = 3.0f;
    private static final float EXPLOSION_FLASH_FADE_INCREMENT = 0.008f;
    private static final float EXPLOSION_WAKE_SIZE = 7.0f;
    private static final float EXPLOSION_WAKE_SCALE_INCREMENT = 0.035f;

    private static final int MAX_PROGRESS_BUFFS = 6;
    private static final float PROGRESS_BUFF_Y_OFFSET = 1.15f;
    private static final float PROGRESS_RING_SIZE = 1.0F;
    private static final float COLLECTING_RING_ROTATE_DELTA = 3.0f;
    private static final float DELIVERING_RING_ROTATE_DELTA = 1.5f;
    private static final float PROGRESS_TEXT_WIDTH = 1.6f;
    private static final float PROGRESS_TEXT_HEIGHT = 0.4f;
    private static final long PROGRESS_TEXT_BLINK_TIME = 250;

    private static final int MIN_EXPLOSION_FORCE = 250;
    private static final int MAX_EXPLOSION_FORCE = 400;
    private static final int NUM_PLAYER_SHOTS = 20;
    private static final int NUM_PLAYER_SHIP_HIT_SOUNDS = 4;
    private static final int NUM_PLAYER_SHIP_DAMAGE_SOUNDS = 2;
    private static final int COLLISION_SOUND_GAP = 250;
    private static final int NUM_ADDITIONAL_DEBRIS_OBJECTS = 8;

    private static final float EXPLOSION_SPARK_SIZE = 0.15f;
    private static final int NUM_EXPLOSION_PARTICLES = 128;
    private static final int EXPLOSION_SPARK_MIN_VELOCITY = 1;
    private static final int EXPLOSION_SPARK_MAX_VELOCITY = 4;
    private static final int EXPLOSION_SPARK_MIN_LIFE_TIME = 2000;
    private static final int EXPLOSION_SPARK_MAX_LIFE_TIME = 4000;

    private static final float SCRAPE_SPARK_SIZE = 0.05f;
    private static final int NUM_SCRAPE_PARTICLES = 16;
    private static final int SCRAPE_SPARK_MIN_VELOCITY = 1;
    private static final int SCRAPE_SPARK_MAX_VELOCITY = 4;
    private static final int SCRAPE_SPARK_MIN_LIFE_TIME = 100;
    private static final int SCRAPE_SPARK_MAX_LIFE_TIME = 250;

    private static final float LARGE_SMOKE_PARTICLE_SIZE = 3.0f;
    private static final int NUM_LARGE_SMOKE_PARTICLES = 64;
    private static final int SMOKE_RADIUS = 3;
    private static final int SMOKE_MIN_LIFE_TIME = 3750;
    private static final int SMOKE_MAX_LIFE_TIME = 5250;

    private static final int NUM_SMALL_SMOKE_PARTICLES = 48;
    private static final float SMALL_SMOKE_PARTICLE_SIZE = 0.7f;
    private static final long[] SMOKE_HIT_POINT_SCALE = { 60, 40, 25, 10 };
    private static final long[] SMOKE_TIMEOUT_MS_SCALE = { 1000, 700, 400, 100 };

    private static final int MAX_DUST_CLOUDS = 10;
    private static final int NUM_DUST_PARTICLES_PER_CLOUD = 3;
    private static final float DUST_CLOUD_SIZE = 1.0f;
    private static final float DUST_CLOUD_ROTATE_INCREMENT = 0.008f;

    private static final com.jme3.math.Vector3f ZERO_VELOCITY = new com.jme3.math.Vector3f(0, 0, 0);
    private static final com.jme3.math.Vector3f PREVENT_XY_AXES_ROTATION = new com.jme3.math.Vector3f(0.0f, 0.0f, 1.0f);
    private static final com.jme3.math.Vector3f PREVENT_Z_AXIS_MOVEMENT = new com.jme3.math.Vector3f(1.0f, 1.0f, 0.0f);
    private static final Vector4f WHITE = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    private static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);

    private static final String SINGLE_PLAYER_STATUS_BAR = "singlePlayerStatusBar";

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

    private static class DustCloud {
        public boolean _active;
        public Vector3f _position;
        public Vector4f _diffuseColour;
        public Matrix4f[] _modelMatrices;
        public float[] _rotations;
    }

    private DisplayMesh _dustCloudDisplayMesh;
    private final DustCloud[] _dustClouds;
    private int _nextDustCloudIndex;

    private static class ProgressBuff {
        public enum Type { COLLECTING, DELIVERING }
        public Type _type;
        public boolean _active;
        public boolean _showText;
        public long _toggleTime;
        public long _expiryTime;
        public float _rotation;
        public float _rotateDelta;
        public Vector3f _position;
        public Matrix4f _modelMatrix;
        public Player _player;
        public Crate _crate;
    }

    public DisplayMesh _progressBuffRing;
    public DisplayMesh _progressBuffText;
    private final ArrayList<ProgressBuff> _progressBuffs;

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

    private final Vector4f _playerExplosionColour;
    private float _explosionWakeScale;
    private DisplayMesh _explosionFlash;
    private DisplayMesh _explosionWake;
    private boolean _playerExploding;
    private boolean _showSparks;

    private final ArrayList<RigidBodyObject> _lunarLanders;
    private final ArrayList<RigidBodyObject> _crates;
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
    private final Matrix4f _mvMatrix;
    private final Matrix4f _vpMatrix;
    private final Matrix4f _explosionFlashModelMatrix;
    private final Matrix4f _explosionWakeModelMatrix;
    private ExplosionParticleSystem _explosionParticleSystem;
    private ExplosionParticleSystem _sparkParticleSystem;
    private LargeSmokeParticleSystem _largeSmokeParticleSystem;
    private SmallSmokeParticleSystem _smallSmokeParticleSystem;
    private boolean _playerSmokeActive;
    private long _lastCollisionTime;

    private SoundSource[] _playerShipDamage;
    private SoundSource _playerShipShoot;
    private SoundSource _playerShipExplode;
    private SoundSource[] _playerShipHits;
    private SoundSource _collectCrate;
    private int _playerShipHitSoundIndex;
    private int _playerShipDamageSoundIndex;
    private GlTexture _blueSparkTexture;
    private GlTexture _crateCollectingTexture;
    private GlTexture _crateDroppedForDeliveryTexture;
    private GlTexture _crateDeliveringTexture;
    private GlTexture _collectingProgressRingTexture;
    private GlTexture _deliveringProgressRingTexture;
    private GlTexture _collectingProgressTextTexture;
    private GlTexture _deliveringProgressTextTexture;

    private long _lastSmokeTime;

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
        _mvMatrix = new Matrix4f();
        _vpMatrix = new Matrix4f();
        _lunarLanders = new ArrayList<>();
        _crates = new ArrayList<>();
        _deliveryZones = new ArrayList<>();
        _debris = new ArrayList<>();
        _playerShots = new ArrayList<>();
        _progressBuffs = new ArrayList<>();

        _physicsTransform = new com.jme3.math.Transform();
        _physicsMatrix = new com.jme3.math.Matrix4f();
        _jomlVector = new Vector3f();
        _jmeVector = new com.jme3.math.Vector3f();
        _explosionFlashModelMatrix = new Matrix4f();
        _explosionWakeModelMatrix = new Matrix4f();
        _lastCollisionTime = 0;
        _explosionWakeScale = 1.0f;

        _cameras = new Transform[2];
        _cameras[0] = new Transform();
        _cameras[1] = new Transform();

        _lunarLanderStartPoints = new Vector3f[2];
        _lunarLanderStartPoints[0] = new Vector3f(0.0f, 0.0f, 0.0f);
        _lunarLanderStartPoints[1] = new Vector3f(0.0f, 0.0f, 0.0f);
        _playerExplosionColour = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

        _playerShipHitSoundIndex = 0;
        _playerShipDamageSoundIndex = 0;
        _crateStartPoints = new ArrayList<>();
        _deliveryZoneStartPoints = new ArrayList<>();

        _nextDustCloudIndex = 0;
        _dustClouds = new DustCloud[MAX_DUST_CLOUDS];
        for (int i = 0; i < MAX_DUST_CLOUDS; ++i) {
            _dustClouds[i] = new DustCloud();
            _dustClouds[i]._active = false;
            _dustClouds[i]._position = new Vector3f();
            _dustClouds[i]._diffuseColour = new Vector4f(WHITE);
            _dustClouds[i]._rotations = new float[NUM_DUST_PARTICLES_PER_CLOUD];
            _dustClouds[i]._modelMatrices = new Matrix4f[NUM_DUST_PARTICLES_PER_CLOUD];
            for (int j = 0; j < NUM_DUST_PARTICLES_PER_CLOUD; ++j) {
                _dustClouds[i]._modelMatrices[j] = new Matrix4f();
            }
        }

        _lastSmokeTime = 0;
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

        _lunarLanders.clear();
        _crates.clear();
        _debris.clear();
        _playerShots.clear();
        _model.clearCrates();
        _model.clearDeliveryZones();

        createCrates();
        createDeliveryZones();
        createLunarLanders();
        createDebris();
        createPlayerShots();

        if (_progressBuffRing != null) {
            _progressBuffRing.freeResources();
        }
        if (_progressBuffText != null) {
            _progressBuffText.freeResources();
        }
        createProgressBuffs();

        if (_dustCloudDisplayMesh != null) {
            _dustCloudDisplayMesh.freeResources();
        }
        _dustCloudDisplayMesh = createDustCloud();

        if (_explosionFlash != null) {
            _explosionFlash.freeResources();
        }
        _explosionFlash = createExplosionFlash();

        if (_explosionWake != null) {
            _explosionWake.freeResources();
        }
        _explosionWake = createExplosionWake();

        if (_explosionParticleSystem != null) {
            _explosionParticleSystem.freeResources();
        }
        _explosionParticleSystem = new ExplosionParticleSystem(_renderer, NUM_EXPLOSION_PARTICLES,
                "explosionParticleSystem", EXPLOSION_SPARK_SIZE, EXPLOSION_SPARK_SIZE,
                WHITE, "images/YellowSpark.png", _materialCache, _textureCache);
        _explosionParticleSystem.setMinVelocity(EXPLOSION_SPARK_MIN_VELOCITY);
        _explosionParticleSystem.setMaxVelocity(EXPLOSION_SPARK_MAX_VELOCITY);

        if (_sparkParticleSystem != null) {
            _sparkParticleSystem.freeResources();
        }
        _sparkParticleSystem = new ExplosionParticleSystem(_renderer, NUM_SCRAPE_PARTICLES,
                "scrapeParticleSystem", SCRAPE_SPARK_SIZE, SCRAPE_SPARK_SIZE,
                WHITE, "images/YellowSpark.png", _materialCache, _textureCache);
        _sparkParticleSystem.setMinVelocity(SCRAPE_SPARK_MIN_VELOCITY);
        _sparkParticleSystem.setMaxVelocity(SCRAPE_SPARK_MAX_VELOCITY);
        _blueSparkTexture = _textureCache.add(new GlTexture(BitmapImage.fromFile("images/BlueSpark.png")));

        if (_largeSmokeParticleSystem != null) {
            _largeSmokeParticleSystem.freeResources();
        }
        _largeSmokeParticleSystem = new LargeSmokeParticleSystem(_renderer, NUM_LARGE_SMOKE_PARTICLES,
                "largeSmokeParticleSystem", LARGE_SMOKE_PARTICLE_SIZE, LARGE_SMOKE_PARTICLE_SIZE,
                WHITE, "images/Smoke0.png", _materialCache, _textureCache);;
        _largeSmokeParticleSystem.setSmokeRadius(SMOKE_RADIUS);

        if (_smallSmokeParticleSystem != null) {
            _smallSmokeParticleSystem.freeResources();
        }
        _smallSmokeParticleSystem = new SmallSmokeParticleSystem(_renderer, NUM_SMALL_SMOKE_PARTICLES,
                "smallSmokeParticleSystem", SMALL_SMOKE_PARTICLE_SIZE, SMALL_SMOKE_PARTICLE_SIZE,
                WHITE, "images/Smoke0.png", _materialCache, _textureCache);;
        
        _nextDustCloudIndex = 0;
        for (int i = 0; i < MAX_DUST_CLOUDS; ++i) {
            _dustClouds[i]._active = false;
        }

        loadSounds();
    }

    private void createProgressBuffs() throws IOException {
        _progressBuffRing = _renderer.createSprite(
                "progressBuffRing", PROGRESS_RING_SIZE, PROGRESS_RING_SIZE,
                WHITE, "images/CollectingProgressRing.png", _materialCache, _textureCache);
        _collectingProgressRingTexture = _progressBuffRing.getFirstDiffuseTexture();
        _deliveringProgressRingTexture = new GlTexture(BitmapImage.fromFile("images/DeliveringProgressRing.png"));
        
        _progressBuffText = _renderer.createSprite(
                "progressBuffText", PROGRESS_TEXT_WIDTH, PROGRESS_TEXT_HEIGHT,
                WHITE, "images/Collecting.png", _materialCache, _textureCache);
        _collectingProgressTextTexture = _progressBuffText.getFirstDiffuseTexture();
        _deliveringProgressTextTexture = new GlTexture(BitmapImage.fromFile("images/Delivering.png"));

        _progressBuffs.clear();
        for (int i = 0; i < MAX_PROGRESS_BUFFS; ++i) {
            ProgressBuff progressBuff = new ProgressBuff();
            progressBuff._active = false;
            progressBuff._showText = true;
            progressBuff._toggleTime = 0;
            progressBuff._expiryTime = 0;
            progressBuff._rotation = 0.0f;
            progressBuff._position = new Vector3f();
            progressBuff._modelMatrix = new Matrix4f();
            _progressBuffs.add(progressBuff);
        }
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

        _playerShipDamage = new SoundSource[NUM_PLAYER_SHIP_DAMAGE_SOUNDS];
        for (int i = 0; i < NUM_PLAYER_SHIP_DAMAGE_SOUNDS; ++i) {
            soundBuffer = SoundBuffer.fromFile(String.format("sounds/Damage%d.wav", i));
            _soundBufferCache.add(soundBuffer);
            _playerShipDamage[i] = new SoundSource(false, false);
            _playerShipDamage[i].setBuffer(soundBuffer);
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
        if (_playerShipDamage != null) {
            for (var a : _playerShipDamage) {
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
        if (_playerExploding) {
            _explosionParticleSystem.think(_engine.getNowMs());

            _explosionWakeScale += EXPLOSION_WAKE_SCALE_INCREMENT;

            _playerExplosionColour.w -= EXPLOSION_FLASH_FADE_INCREMENT;
            if (_playerExplosionColour.w < 0.0f) {
                _playerExplosionColour.w = 0.0f;
                _playerExploding = false;
            }
        }

        if (_showSparks) {
            _sparkParticleSystem.think(_engine.getNowMs());
            _showSparks = _sparkParticleSystem.isAlive();
        }

        if (_playerSmokeActive) {
            _largeSmokeParticleSystem.think(_engine.getNowMs());
            _playerSmokeActive = _largeSmokeParticleSystem.isAlive();
        }

        if (_smallSmokeParticleSystem != null) {
            _smallSmokeParticleSystem.think(_engine.getNowMs());
            emitPeriodicSmokeIfRequired(_engine.getNowMs());
        }

        for (int i = 0; i < MAX_DUST_CLOUDS; ++i) {
            if (!_dustClouds[i]._active) {
                continue;
            }
            _dustClouds[i]._rotations[0] += DUST_CLOUD_ROTATE_INCREMENT;
            _dustClouds[i]._rotations[1] -= DUST_CLOUD_ROTATE_INCREMENT;
            _dustClouds[i]._rotations[2] += DUST_CLOUD_ROTATE_INCREMENT;

            _dustClouds[i]._modelMatrices[0]
                    .identity()
                    .translate(_dustClouds[i]._position)
                    .rotate(_dustClouds[i]._rotations[0], Z_AXIS);
            _dustClouds[i]._modelMatrices[1]
                    .identity()
                    .translate(_dustClouds[i]._position)
                    .rotate(_dustClouds[i]._rotations[1], Z_AXIS);
            _dustClouds[i]._modelMatrices[2]
                    .identity()
                    .translate(_dustClouds[i]._position)
                    .rotate(_dustClouds[i]._rotations[2], Z_AXIS);

            _dustClouds[i]._diffuseColour.w -= EXPLOSION_FLASH_FADE_INCREMENT;
            if (_dustClouds[i]._diffuseColour.w < 0.0f) {
                _dustClouds[i]._diffuseColour.w = 0.0f;
                _dustClouds[i]._active = false;
            }
        }

        progressBuffsThink(_engine.getNowMs());
    }

    private void progressBuffsThink(long nowMs) {
        for (var a : _progressBuffs) {
            if (a._active) {
                progressBuffThink(nowMs, a);
            }
        }
    }

    private void progressBuffThink(long nowMs, ProgressBuff progressBuff) {
        progressBuff._rotation += progressBuff._rotateDelta;
        if (progressBuff._rotation >= 360.0f) {
            progressBuff._rotation -= 360.0f;
        }

        if (progressBuff._type == ProgressBuff.Type.DELIVERING) {
            progressBuff._crate.getRigidBody().getPhysicsLocation(_jmeVector);
            _jomlVector.x = _jmeVector.x;
            _jomlVector.y = _jmeVector.y;
            _jomlVector.z = _jmeVector.z;
            progressBuff._position.set(_jomlVector).add(0.0f, PROGRESS_BUFF_Y_OFFSET, 0.0f);
        }

        progressBuff._modelMatrix
                .identity()
                .translate(progressBuff._position)
                .rotate((float)Math.toRadians(progressBuff._rotation), Z_AXIS);

        if (nowMs >= progressBuff._toggleTime) {
            progressBuff._showText = !progressBuff._showText;
            progressBuff._toggleTime = nowMs + PROGRESS_TEXT_BLINK_TIME;
        }

        progressBuff._active = progressBuff._expiryTime >= nowMs;
    }

    private void emitPeriodicSmokeIfRequired(long nowMs) {
        for (int i = 0; i < _model.getNumPlayers(); ++i) {
            emitPeriodicSmokeIfRequired(nowMs, _model.getPlayerState(i));
        }
    }

    private void emitPeriodicSmokeIfRequired(long nowMs, Player player) {
        if (player.isExplodingOrDead() || player.getRigidBody() == null || player.getHitPoints() > SMOKE_HIT_POINT_SCALE[0]) {
            return;
        }

        int scale = SMOKE_HIT_POINT_SCALE.length - 1;
        for (int i = 1; i < SMOKE_HIT_POINT_SCALE.length; ++i) {
            if (player.getHitPoints() > SMOKE_HIT_POINT_SCALE[i]) {
                scale = i - 1;
                break;
            }
        }
        
        if (nowMs - _lastSmokeTime < SMOKE_TIMEOUT_MS_SCALE[scale]) {
            return;
        }
        _lastSmokeTime = nowMs;
        
        player.getRigidBody().getPhysicsLocation(_jmeVector);
        _jomlVector.x = _jmeVector.x;
        _jomlVector.y = _jmeVector.y;
        _jomlVector.z = _jmeVector.z;

        _smallSmokeParticleSystem.emitSome(nowMs, _jomlVector, SMOKE_MIN_LIFE_TIME, SMOKE_MAX_LIFE_TIME);
        _sparkParticleSystem.emitAll(nowMs, _jomlVector, SCRAPE_SPARK_MIN_LIFE_TIME, SCRAPE_SPARK_MAX_LIFE_TIME, _blueSparkTexture);
        _showSparks = true;

        _playerShipDamage[_playerShipDamageSoundIndex].play();
        if (++_playerShipDamageSoundIndex >= NUM_PLAYER_SHIP_DAMAGE_SOUNDS) {
            _playerShipDamageSoundIndex = 0;
        }
    }

    @Override
    public void drawView3d(int viewport, Matrix4f projectionMatrix) {
        if (viewport < 0 || viewport > 1) {
            return;
        }

        // Synchronise first so that we can attach a camera to each player's lunar lander
        for (var rbo : _lunarLanders) {
            synchroniseDisplayMeshWithCollisionMesh(rbo);
        }
        for (var rbo : _crates) {
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
            _mvMatrix.set(_cameras[viewport].getViewMatrix());
            _worldDisplayMesh.draw(_renderer, _renderer.getDirectionalLightProgram(), _mvMatrix, projectionMatrix);
        }

        for (var rbo : _lunarLanders) {
            _mvMatrix.set(_cameras[viewport].getViewMatrix()).mul(rbo._modelMatrix);
            rbo._displayMesh.draw(_renderer, _renderer.getSpecularDirectionalLightProgram(), _mvMatrix, projectionMatrix);
        }

        for (var rbo : _crates) {
            _mvMatrix.set(_cameras[viewport].getViewMatrix()).mul(rbo._modelMatrix);
            Crate crate = (Crate)rbo._rigidBody.getUserObject();
            if (crate.isDroppedForDelivery()) {
                rbo._displayMesh.draw(_renderer, _renderer.getSpecularDirectionalLightProgram(), _crateDroppedForDeliveryTexture, _mvMatrix, projectionMatrix);
            }
            else if (crate.isDelivering()) {
                rbo._displayMesh.draw(_renderer, _renderer.getSpecularDirectionalLightProgram(), _crateDeliveringTexture, _mvMatrix, projectionMatrix);
            }
            else if (_model.getPlayerState(1).getCollectingCrate() == crate || _model.getPlayerState(0).getCollectingCrate() == crate) {
                rbo._displayMesh.draw(_renderer, _renderer.getSpecularDirectionalLightProgram(), _crateCollectingTexture, _mvMatrix, projectionMatrix);
            }
            else {
                rbo._displayMesh.draw(_renderer, _renderer.getSpecularDirectionalLightProgram(), _mvMatrix, projectionMatrix);
            }
        }

        for (var rbo : _debris) {
            if (rbo._active) {
                _mvMatrix.set(_cameras[viewport].getViewMatrix()).mul(rbo._modelMatrix);
                rbo._displayMesh.draw(_renderer, _renderer.getSpecularDirectionalLightProgram(), _mvMatrix, projectionMatrix);
            }
        }

        for (var rbo : _playerShots) {
            if (rbo._active) {
                _mvMatrix.set(_cameras[viewport].getViewMatrix()).mul(rbo._modelMatrix);
                rbo._displayMesh.draw(_renderer, _renderer.getSpecularDirectionalLightProgram(), _mvMatrix, projectionMatrix);
            }
        }

        glDepthMask(false);
        _vpMatrix.set(projectionMatrix).mul(_cameras[viewport].getViewMatrix());

        if (_playerSmokeActive) {
            _largeSmokeParticleSystem.draw(_vpMatrix);
        }

        if (_smallSmokeParticleSystem != null) {
            _smallSmokeParticleSystem.draw(_vpMatrix);
        }
        
        if (_playerExploding) {
            _mvpMatrix.set(_vpMatrix).mul(_explosionFlashModelMatrix);
            _explosionFlash.draw(_renderer, _renderer.getDiffuseTextureProgram(), _mvpMatrix, _playerExplosionColour);

            _mvpMatrix.set(_vpMatrix).mul(_explosionWakeModelMatrix).scale(_explosionWakeScale);;
            _explosionWake.draw(_renderer, _renderer.getDiffuseTextureProgram(), _mvpMatrix, _playerExplosionColour);

            _explosionParticleSystem.draw(_vpMatrix);
        }

        for (int i = 0; i < MAX_DUST_CLOUDS; ++i) {
            if (!_dustClouds[i]._active) {
                continue;
            }
            for (int j = 0; j < NUM_DUST_PARTICLES_PER_CLOUD; ++j) {
                _mvpMatrix.set(_vpMatrix).mul(_dustClouds[i]._modelMatrices[j]);
                _dustCloudDisplayMesh.draw(_renderer, _renderer.getDiffuseTextureProgram(), _mvpMatrix, _dustClouds[i]._diffuseColour);
            }
        }

        if (_showSparks) {
            _sparkParticleSystem.draw(_vpMatrix);
        }

        for (var progressBuff : _progressBuffs) {
            if (progressBuff._active) {
                GlTexture ring, text;
                if (progressBuff._type == ProgressBuff.Type.COLLECTING) {
                    ring = _collectingProgressRingTexture; text = _collectingProgressTextTexture;
                }
                else {
                    ring = _deliveringProgressRingTexture; text = _deliveringProgressTextTexture;
                }

                progressBuff._modelMatrix
                        .identity()
                        .translate(progressBuff._position)
                        .rotate((float)Math.toRadians(progressBuff._rotation), Z_AXIS);
                _mvpMatrix.set(_vpMatrix).mul(progressBuff._modelMatrix);
                _progressBuffRing.draw(_renderer, _renderer.getDiffuseTextureProgram(), _mvpMatrix, ring, WHITE);

                if (progressBuff._showText) {
                    progressBuff._modelMatrix
                            .identity()
                            .translate(progressBuff._position);
                    _mvpMatrix.set(_vpMatrix).mul(progressBuff._modelMatrix);
                    _progressBuffText.draw(_renderer, _renderer.getDiffuseTextureProgram(), _mvpMatrix, text, WHITE);
                }
            }
        }
        
        glDepthMask(true);
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
        resetRigidBodyObjects(_lunarLanders);
        resetRigidBodyObjects(_crates);
        resetRigidBodyObjects(_debris);
        resetRigidBodyObjects(_playerShots);

        _showSparks = false;
        _playerSmokeActive = false;

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
        _blueSparkTexture = null;
        _crateCollectingTexture = null;
        _crateDeliveringTexture = null;
        _crateDroppedForDeliveryTexture = null;
        _crateStartPoints.clear();
        _deliveryZoneStartPoints.clear();
        _soundBufferCache.freeResources();
        _physicsTransform = new com.jme3.math.Transform();
        _physicsMatrix = new com.jme3.math.Matrix4f();

        if (_explosionFlash != null) {
            _explosionFlash.freeResources();
            _explosionFlash = null;
        }
        if (_explosionWake != null) {
            _explosionWake.freeResources();
            _explosionWake = null;
        }
        if (_explosionParticleSystem != null) {
            _explosionParticleSystem.freeResources();
            _explosionParticleSystem = null;
        }
        if (_dustCloudDisplayMesh != null) {
            _dustCloudDisplayMesh.freeResources();
            _dustCloudDisplayMesh = null;
        }
        if (_sparkParticleSystem != null) {
            _sparkParticleSystem.freeResources();
            _sparkParticleSystem = null;
        }
        if (_largeSmokeParticleSystem != null) {
            _largeSmokeParticleSystem.freeResources();
            _largeSmokeParticleSystem = null;
        }
        if (_smallSmokeParticleSystem != null) {
            _smallSmokeParticleSystem.freeResources();
            _smallSmokeParticleSystem = null;
        }

        if (_progressBuffRing != null) {
            _progressBuffRing.freeResources();
            _progressBuffRing = null;
        }
        if (_progressBuffText != null) {
            _progressBuffText.freeResources();
            _progressBuffText = null;
        }

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
    public void crateCollectingStarted(Player player, Crate crate) {
        startProgressBuff(player, ProgressBuff.Type.COLLECTING, Player.CRATE_COLLECTION_TIME);
    }

    @Override
    public void crateCollectionCompleted(Crate crate) {
        removeCrate(crate);
        _collectCrate.play();
    }

    @Override
    public void crateCollectionAborted(Player player) {
        for (var progressBuff : _progressBuffs) {
            if (progressBuff._active && progressBuff._player == player) {
                progressBuff._active = false;
                break;
            }
        }
    }

    @Override
    public void crateStartedDelivering(Crate crate, com.jme3.math.Vector3f playerPosition) {
        _jomlVector.x = playerPosition.x;
        _jomlVector.y = playerPosition.y;
        _jomlVector.z = playerPosition.z;
        createCrateRigidBody(_jomlVector, crate);
        startProgressBuff(crate, ProgressBuff.Type.DELIVERING, DeliveryZone.CRATE_DELIVERY_TIME);
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
    public void playerShipCollided(Player player, float appliedImpulse, com.jme3.math.Vector3f impactPoint) {
        if (appliedImpulse < MIN_IMPULSE_TO_PLAY_SOUND || _engine.getNowMs() - _lastCollisionTime < COLLISION_SOUND_GAP) {
            return;
        }
        _lastCollisionTime = _engine.getNowMs();

        _playerShipHits[_playerShipHitSoundIndex].play();
        if (++_playerShipHitSoundIndex >= NUM_PLAYER_SHIP_HIT_SOUNDS) {
            _playerShipHitSoundIndex = 0;
        }

        _jomlVector.x = impactPoint.x;
        _jomlVector.y = impactPoint.y;
        _jomlVector.z = impactPoint.z;

        _showSparks = true;
        _sparkParticleSystem.emitAll(_engine.getNowMs(), _jomlVector, SCRAPE_SPARK_MIN_LIFE_TIME, SCRAPE_SPARK_MAX_LIFE_TIME);

        _dustClouds[_nextDustCloudIndex]._active = true;
        _dustClouds[_nextDustCloudIndex]._position.set(_jomlVector);
        _dustClouds[_nextDustCloudIndex]._diffuseColour.w = 1.0f;
        float angleDegrees = randomInteger(0, 359);
        for (int j = 0; j < NUM_DUST_PARTICLES_PER_CLOUD; ++j) {
            float r = (float)Math.toRadians(angleDegrees);
            _dustClouds[_nextDustCloudIndex]._rotations[j] = r;
            _dustClouds[_nextDustCloudIndex]._modelMatrices[j].identity().translate(_jomlVector).rotate(r, Z_AXIS);
            angleDegrees += 120.0f; // <-- 360/3
        }

        if (++_nextDustCloudIndex >= MAX_DUST_CLOUDS) {
            _nextDustCloudIndex = 0;
        }
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
        removePlayerShip(player);

        _explosionParticleSystem.emitAll(_engine.getNowMs(), _jomlVector,
                EXPLOSION_SPARK_MIN_LIFE_TIME, EXPLOSION_SPARK_MAX_LIFE_TIME);
        _playerExploding = true;

        _explosionFlashModelMatrix.identity().translate(_jomlVector);
        _explosionWakeModelMatrix.identity().translate(_jomlVector);

        _explosionWakeScale = 0.1f;
        _playerExplosionColour.w = 1.0f;

        _largeSmokeParticleSystem.emitAll(_engine.getNowMs(), _jomlVector,
                SMOKE_MIN_LIFE_TIME, SMOKE_MAX_LIFE_TIME);
        _playerSmokeActive = true;

        _playerShipExplode.play();
    }

    @Override
    public void playerShipDead(int player) {
        //_playerExploding = false;
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

        int numDropped = 0;
        for (var crate : player.getCollectedCrates()) {
            crate.removeTimeouts(_engine.getTimeoutManager());
            crate.setState(Crate.State.IDLE);
            createCrateRigidBody(_jomlVector, crate);
            if (++numDropped >= _model.getNumCratesRemaining()) {
                break;
            }
        }

        player.clearCollectedCrates();
    }

    private void removePlayerShip(Player player) {
        for (var rbo : _lunarLanders) {
            if (rbo._rigidBody == player.getRigidBody()) {
                _engine.getPhysicsSpace().removeCollisionObject(rbo._rigidBody);
                rbo._rigidBody.setUserObject(null);
                player.setRigidBody(null);
                _lunarLanders.remove(rbo);
                break;
            }
        }
    }

    private void removeCrate(Crate crate) {
        for (var rbo : _crates) {
            if (rbo._rigidBody.getUserObject() == crate) {
                _engine.getPhysicsSpace().removeCollisionObject(rbo._rigidBody);
                rbo._rigidBody.setUserObject(null);
                crate.setRigidBody(null, null);
                _crates.remove(rbo);

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

        _lunarLanders.add(new RigidBodyObject(rigidBody, displayMesh, true));
        _engine.getPhysicsSpace().addCollisionObject(rigidBody);

        Player player = _model.getPlayerState(i);
        player.reset();
        player.setRigidBody(rigidBody);
        rigidBody.setUserObject(player);
    }

    private void createCrates() throws IOException {
        _model.setCrateLimit(_crateStartPoints.size());
        for (var point : _crateStartPoints) {
            createCrateRigidBody(point, null);
        }
        _crateCollectingTexture = _textureCache.add(new GlTexture(BitmapImage.fromFile("images/CrateCollecting.png")));
        _crateDeliveringTexture = _textureCache.add(new GlTexture(BitmapImage.fromFile("images/CrateDelivering.png")));
        _crateDroppedForDeliveryTexture = _textureCache.add(new GlTexture(BitmapImage.fromFile("images/CrateDroppedForDelivery.png")));
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

        _crates.add(new RigidBodyObject(rigidBody, displayMesh, true));
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

        displayMesh = _displayMeshCache.getByExactName("EngineCube.Display");
        collisionMesh = _collisionMeshCache.getByExactName("EngineCube.BoxShape");
        if (displayMesh != null && collisionMesh != null) {
            for (i = 0; i < NUM_ADDITIONAL_DEBRIS_OBJECTS; ++i) {
                PhysicsRigidBody rigidBody = new PhysicsRigidBody(collisionMesh, 1.0f); // TODO: what about mass?
                RigidBodyObject rbo = new RigidBodyObject(rigidBody, displayMesh, false);
                _debris.add(rbo); // save for later insertion
            }
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

    private DisplayMesh createExplosionFlash() throws IOException {
        return _renderer.createSprite(
                "ExplosionFlash", EXPLOSION_FLASH_SIZE, EXPLOSION_FLASH_SIZE,
                WHITE, "images/ExplosionFlash.png",
                _materialCache, _textureCache
        );
    }

    private DisplayMesh createExplosionWake() throws IOException {
        return _renderer.createSprite(
                "ExplosionWake", EXPLOSION_WAKE_SIZE, EXPLOSION_WAKE_SIZE,
                WHITE, "images/ExplosionWake.png",
                _materialCache, _textureCache
        );
    }

    private DisplayMesh createDustCloud() throws IOException {
        return _renderer.createSprite(
                "DustCloud", DUST_CLOUD_SIZE, DUST_CLOUD_SIZE,
                WHITE, "images/DustCloud.png",
                _materialCache, _textureCache
        );
    }

    public void startProgressBuff(Player player, ProgressBuff.Type type, long timeoutMs) {
        player.getRigidBody().getPhysicsLocation(_jmeVector);
        _jomlVector.x = _jmeVector.x;
        _jomlVector.y = _jmeVector.y;
        _jomlVector.z = _jmeVector.z;

        for (var progressBuff : _progressBuffs) {
            if (!progressBuff._active) {
                progressBuff._active = true;
                progressBuff._type = type;
                progressBuff._player = player;
                progressBuff._crate = null;
                progressBuff._position.set(_jomlVector).add(0.0f, PROGRESS_BUFF_Y_OFFSET, 0.0f);
                progressBuff._rotation = 0.0f;
                progressBuff._rotateDelta = COLLECTING_RING_ROTATE_DELTA;
                progressBuff._showText = true;
                progressBuff._toggleTime = _engine.getNowMs() + PROGRESS_TEXT_BLINK_TIME;
                progressBuff._expiryTime = _engine.getNowMs() + timeoutMs;
                break;
            }
        }
    }

    public void startProgressBuff(Crate crate, ProgressBuff.Type type, long timeoutMs) {
        crate.getRigidBody().getPhysicsLocation(_jmeVector);
        _jomlVector.x = _jmeVector.x;
        _jomlVector.y = _jmeVector.y;
        _jomlVector.z = _jmeVector.z;

        for (var progressBuff : _progressBuffs) {
            if (!progressBuff._active) {
                progressBuff._active = true;
                progressBuff._type = type;
                progressBuff._player = null;
                progressBuff._crate = crate;
                progressBuff._position.set(_jomlVector).add(0.0f, PROGRESS_BUFF_Y_OFFSET, 0.0f);
                progressBuff._rotation = 0.0f;
                progressBuff._rotateDelta = DELIVERING_RING_ROTATE_DELTA;
                progressBuff._showText = true;
                progressBuff._toggleTime = _engine.getNowMs() + PROGRESS_TEXT_BLINK_TIME;
                progressBuff._expiryTime = _engine.getNowMs() + timeoutMs;
                break;
            }
        }
    }
}
