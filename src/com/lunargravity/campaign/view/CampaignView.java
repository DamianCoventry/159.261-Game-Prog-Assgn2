package com.lunargravity.campaign.view;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.lunargravity.campaign.controller.ICampaignController;
import com.lunargravity.campaign.model.ICampaignModel;
import com.lunargravity.campaign.statemachine.GetReadyState;
import com.lunargravity.engine.animation.FloatLinearInterpLoop;
import com.lunargravity.engine.animation.FloatSineLinearInterp;
import com.lunargravity.engine.animation.Vector3SineLinearInterp;
import com.lunargravity.engine.core.IEngine;
import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.widgetsystem.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.IOException;

public class CampaignView implements
        ICampaignView,
        ISceneAssetOwner,
        IMissionPausedObserver,
        IAnnouncementObserver
{
    private static final String MISSION_PAUSED = "missionPaused";
    private static final String EPISODE_INTRO_ANNOUNCEMENT = "episodeIntro";
    private static final String EPISODE_OUTRO_ANNOUNCEMENT = "episodeOutro";
    private static final String GAME_OVER_ANNOUNCEMENT = "gameOver";
    private static final String GAME_WON_ANNOUNCEMENT = "gameWon";
    private static final String MISSION_INTRO_ANNOUNCEMENT = "missionIntro";
    private static final String MISSION_COMPLETED = "missionCompleted";
    private static final String GET_READY = "getReady";
    private static final String PLAYER_DIED = "playerDied";

    private static final long PLANET_ROTATION_TIME = 600000; // 10 minutes to rotate 360°
    private static final long NATURAL_SATELLITE_ROTATION_TIME = 360000; // 6 minutes to rotate 360°
    private static final long CAMERA_TRANSLATE_TIME = 5000;
    private static final float[] CAMERA_Z_COORDINATES_FAR = { 1650.0f, 1600.0f, 2250.0f, 2250.0f };
    private static final float[] CAMERA_Z_COORDINATES_NEAR = { 150.0f, 100.0f, 750.0f, 850.0f };

    private static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
    private static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
    private static final float OUTRO_Y_COORDINATE_TOP = 400.0f;
    private static final float OUTRO_Y_COORDINATE_BOTTOM = 16.0f;
    private static final long OUTRO_ANIMATE_TIME = 3000;

    private static final float[] PLANET_X_ROTATIONS_DEGREES = { 2.5f, -1.25f, 3.25f, 30.0f };

    private static final Vector3f[] NATURAL_SATELLITE_POSITIONS = {
            new Vector3f(10.0f, 0.0f, 70.0f),
            new Vector3f(4.0f, 0.0f, 70.0f),
            new Vector3f(15.0f, 0.0f, 625.0f),
            new Vector3f(15.0f, 0.0f, 750.0f)
    };
    private static final int NUM_PLANETS_NATURAL_SATELLITES = 4;

    private static final int EARTH_MOON = 0;
    private static final int MARS_PHOBOS = 1;
    private static final int JUPITER_GANYMEDE = 2;
    private static final int SATURN_TITAN = 3;

    private final WidgetManager _widgetManager;
    private final ICampaignController _controller;
    private final ICampaignModel _model;
    private final DisplayMeshCache _displayMeshCache;
    private final MaterialCache _materialCache;
    private final TextureCache _textureCache;
    private final IEngine _engine;

    private DisplayMesh _spaceDisplayMesh;
    private DisplayMesh[] _naturalSatellites;
    private DisplayMesh[] _planets;
    private final Matrix4f _vpMatrix;
    private final Matrix4f _mvMatrix;
    private final Matrix4f _modelMatrix;
    private final Transform _camera;
    private final FloatLinearInterpLoop _planetRotation;
    private final FloatLinearInterpLoop _naturalSatelliteRotation;
    private final Vector3SineLinearInterp _cameraPosition;
    private final FloatSineLinearInterp _episodeOutroYCoordinate;

    private Widget _episodeIntro;
    private Widget _episodeOutro;
    private Widget _gameOver;
    private Widget _gameWon;
    private Widget _missionIntro;
    private Widget _missionPaused;
    private Widget _missionCompleted;
    private Widget _getReady;
    private Widget _playerDied;

    public CampaignView(WidgetManager widgetManager, IEngine engine, ICampaignController controller, ICampaignModel model) {
        _widgetManager = widgetManager;
        _engine = engine;
        _controller = controller;
        _model = model;
        _displayMeshCache = new DisplayMeshCache();
        _materialCache = new MaterialCache();
        _textureCache = new TextureCache();
        _mvMatrix = new Matrix4f();
        _vpMatrix = new Matrix4f();
        _modelMatrix = new Matrix4f();
        _camera = new Transform();

        _planetRotation = new FloatLinearInterpLoop(_engine.getAnimationManager());
        _naturalSatelliteRotation = new FloatLinearInterpLoop(_engine.getAnimationManager());
        _cameraPosition = new Vector3SineLinearInterp(_engine.getAnimationManager());
        _episodeOutroYCoordinate = new FloatSineLinearInterp(_engine.getAnimationManager());
    }

    @Override
    public void initialLoadCompleted() throws Exception {
        GlStaticMeshBuilder builder = new GlStaticMeshBuilder(_displayMeshCache, _materialCache, _textureCache);
        builder.build("meshes/EpisodeIntroOutro.obj");
        for (var a : builder.getMeshes()) {
            _displayMeshCache.add(a);
        };

        _naturalSatellites = new DisplayMesh[NUM_PLANETS_NATURAL_SATELLITES];
        _naturalSatellites[EARTH_MOON] = _displayMeshCache.getByExactName("EarthMoon.Display");
        _naturalSatellites[MARS_PHOBOS] = _displayMeshCache.getByExactName("MarsPhobos.Display");
        _naturalSatellites[JUPITER_GANYMEDE] = _displayMeshCache.getByExactName("JupiterGanymede.Display");
        _naturalSatellites[SATURN_TITAN] = _displayMeshCache.getByExactName("SaturnTitan.Display");

        _planets = new DisplayMesh[NUM_PLANETS_NATURAL_SATELLITES];
        _planets[EARTH_MOON] = _displayMeshCache.getByExactName("Earth.Display");
        _planets[MARS_PHOBOS] = _displayMeshCache.getByExactName("Mars.Display");
        _planets[JUPITER_GANYMEDE] = _displayMeshCache.getByExactName("Jupiter.Display");
        _planets[SATURN_TITAN] = _displayMeshCache.getByExactName("Saturn.Display");

        _spaceDisplayMesh = _displayMeshCache.getByExactName("OuterSpace.Display");
        _planetRotation.start(0.0f, 359.0f, PLANET_ROTATION_TIME);
        _naturalSatelliteRotation.start(0.0f, 359.0f, NATURAL_SATELLITE_ROTATION_TIME);
    }

    @Override
    public void viewThink() {
        // TODO
    }

    @Override
    public void drawView3d(int viewport, Matrix4f projectionMatrix) {
        if (!_widgetManager.isVisible(_episodeIntro) && !_widgetManager.isVisible(_episodeOutro)) {
            return;
        }

        if (_episodeOutro != null) {
            Vector2f position = _episodeOutro.getPosition();
            position.y = _episodeOutroYCoordinate.getCurrentValue();
            _episodeOutro.setPosition(position);
        }

        _camera._position.set(_cameraPosition.getValue());
        _camera.calculateViewMatrix();

        _vpMatrix.set(projectionMatrix).mul(_camera.getViewMatrix());
        _spaceDisplayMesh.draw(_widgetManager.getRenderer(), _widgetManager.getRenderer().getDiffuseTextureProgram(), _vpMatrix);

        int i = Math.min(3, _model.getEpisode());
        
        _modelMatrix
                .identity()
                // origin
                .rotate((float)Math.toRadians(PLANET_X_ROTATIONS_DEGREES[i]), X_AXIS)
                .rotate((float)Math.toRadians(-_planetRotation.getCurrentValue()), Y_AXIS);
        _mvMatrix.set(_camera.getViewMatrix()).mul(_modelMatrix);
        _planets[i].draw(_widgetManager.getRenderer(),
                _widgetManager.getRenderer().getDirectionalLightProgram(), _mvMatrix, projectionMatrix);

        _modelMatrix
                .identity()
                .translate(NATURAL_SATELLITE_POSITIONS[i])
                .rotate((float)Math.toRadians(_naturalSatelliteRotation.getCurrentValue()), Y_AXIS);
        _mvMatrix.set(_camera.getViewMatrix()).mul(_modelMatrix);
        _naturalSatellites[i].draw(_widgetManager.getRenderer(),
                _widgetManager.getRenderer().getDirectionalLightProgram(), _mvMatrix, projectionMatrix);
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
        // TODO
    }

    @Override
    public void setupForNewLevel() {
        // TODO
    }

    @Override
    public void freeNativeResources() {
        if (_displayMeshCache != null) {
            _displayMeshCache.freeNativeResources();
        }
        if (_materialCache != null) {
            _materialCache.clear();
        }
        if (_textureCache != null) {
            _textureCache.freeNativeResources();
        }
        if (_naturalSatellites != null) {
            for (var a : _naturalSatellites) {
                a.freeNativeResources();
            }
        }
        if (_planets != null) {
            for (var a : _planets) {
                a.freeNativeResources();
            }
        }
        if (_planetRotation != null) {
            _planetRotation.unregister();
        }
        if (_naturalSatelliteRotation != null) {
            _naturalSatelliteRotation.unregister();
        }
        if (_cameraPosition != null) {
            _cameraPosition.unregister();
        }
    }

    @Override
    public void showEpisodeIntro() throws IOException {
        int i = Math.min(3, _model.getEpisode());

        _cameraPosition.start(
                new Vector3f(0.0f, 0.0f, CAMERA_Z_COORDINATES_FAR[i]),
                new Vector3f(0.0f, 0.0f, CAMERA_Z_COORDINATES_NEAR[i]),
                CAMERA_TRANSLATE_TIME);
        _widgetManager.hideAll();
        _widgetManager.show(_episodeIntro, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showEpisodeOutro() throws IOException {
        if (_episodeOutro == null) {
            return;
        }
        int i = Math.min(3, _model.getEpisode());
        _cameraPosition.setValue(new Vector3f(0.0f, 0.0f, CAMERA_Z_COORDINATES_NEAR[i]));

        _episodeOutroYCoordinate.start(OUTRO_Y_COORDINATE_TOP, OUTRO_Y_COORDINATE_BOTTOM, OUTRO_ANIMATE_TIME);

        _widgetManager.hideAll();
        _widgetManager.show(_episodeOutro, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showMissionIntro() throws IOException {
        _widgetManager.hideAll();
        _widgetManager.show(_missionIntro, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showGameWon() throws IOException {
        _widgetManager.hideAll();
        _widgetManager.show(_gameWon, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showGameOver() throws IOException {
        _widgetManager.hideAll();
        _widgetManager.show(_gameOver, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showMissionPaused() throws IOException {
        _widgetManager.hideAll();
        _widgetManager.show(_missionPaused, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showMissionCompleted() throws IOException {
        if (_missionCompleted != null) {
            _widgetManager.hideAll();
            _widgetManager.show(_missionCompleted, WidgetManager.ShowAs.FIRST);
        }
    }

    @Override
    public void showGetReady(int i) throws IOException {
        if (_widgetManager.isVisible(_getReady)) {
            int clamped = Math.min(GetReadyState.MAX_SECONDS, Math.max(GetReadyState.MIN_SECONDS, i));
            _getReady.getObserver().setBackgroundImage(String.format("images/GetReady%d.png", clamped));
        }
        else {
            _widgetManager.hideAll();
            _getReady.getObserver().setBackgroundImage("images/GetReady3.png");
            _widgetManager.show(_getReady, WidgetManager.ShowAs.FIRST);
        }
    }

    @Override
    public void showPlayerDied(WhichPlayer whichPlayer) throws IOException {
        switch (whichPlayer) {
            case PLAYER_1 -> _playerDied.getObserver().setBackgroundImage("images/Player1Died.png");
            case PLAYER_2 -> _playerDied.getObserver().setBackgroundImage("images/Player2Died.png");
            case BOTH_PLAYERS -> _playerDied.getObserver().setBackgroundImage("images/BothPlayersDied.png");
        }
        _widgetManager.hideAll();
        _widgetManager.show(_playerDied, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void displayMeshLoaded(DisplayMesh displayMesh) {
        // TODO
    }

    @Override
    public void collisionMeshLoaded(String name, CollisionShape collisionMesh) {
        // TODO
    }

    @Override
    public void widgetLoaded(ViewportConfig viewportConfig, WidgetCreateInfo wci) throws IOException {
        if (wci == null) {
            System.out.print("CampaignView.widgetLoaded() was passed a null WidgetCreateInfo object");
            return;
        }
        if (wci._id.equals(MISSION_PAUSED) && wci._type.equals("MissionPausedWidget")) {
            _missionPaused = new Widget(viewportConfig, wci, new MissionPausedWidget(_widgetManager, this));
        }
        else if (wci._id.equals(EPISODE_INTRO_ANNOUNCEMENT) && wci._type.equals("ImageWidget")) {
            _episodeIntro = new Widget(viewportConfig, wci, new ImageWidget(_widgetManager));
        }
        else if (wci._id.equals(EPISODE_OUTRO_ANNOUNCEMENT) && wci._type.equals("ImageWidget")) {
            _episodeOutro = new Widget(viewportConfig, wci, new ImageWidget(_widgetManager));
        }
        else if (wci._id.equals(GAME_OVER_ANNOUNCEMENT) && wci._type.equals("ImageWidget")) {
            _gameOver = new Widget(viewportConfig, wci, new ImageWidget(_widgetManager));
        }
        else if (wci._id.equals(GAME_WON_ANNOUNCEMENT) && wci._type.equals("ImageWidget")) {
            _gameWon = new Widget(viewportConfig, wci, new ImageWidget(_widgetManager));
        }
        else if (wci._id.equals(MISSION_INTRO_ANNOUNCEMENT) && wci._type.equals("ImageWidget")) {
            _missionIntro = new Widget(viewportConfig, wci, new ImageWidget(_widgetManager));
        }
        else if (wci._id.equals(MISSION_COMPLETED) && wci._type.equals("ImageWidget")) {
            _missionCompleted = new Widget(viewportConfig, wci, new ImageWidget(_widgetManager));
        }
        else if (wci._id.equals(GET_READY) && wci._type.equals("ImageWidget")) {
            _getReady = new Widget(viewportConfig, wci, new ImageWidget(_widgetManager));
        }
        else if (wci._id.equals(PLAYER_DIED) && wci._type.equals("ImageWidget")) {
            _playerDied = new Widget(viewportConfig, wci, new ImageWidget(_widgetManager));
        }
    }

    @Override
    public void resumeMissionButtonClicked() {
        _controller.resumeMission();
    }

    @Override
    public void mainMenuButtonClicked() {
        _controller.quitCampaign();
    }

    @Override
    public void announcementClicked(String widgetId) throws Exception {
        switch (widgetId) {
            case EPISODE_INTRO_ANNOUNCEMENT -> _controller.episodeIntroAborted();
            case EPISODE_OUTRO_ANNOUNCEMENT -> _controller.episodeOutroAborted();
            case GAME_OVER_ANNOUNCEMENT -> _controller.gameOverAborted();
            case GAME_WON_ANNOUNCEMENT -> _controller.gameWonAborted();
            case MISSION_INTRO_ANNOUNCEMENT -> _controller.missionIntroAborted();
        }
    }
}
