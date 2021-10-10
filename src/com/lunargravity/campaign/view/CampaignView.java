package com.lunargravity.campaign.view;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.lunargravity.campaign.controller.ICampaignController;
import com.lunargravity.campaign.model.ICampaignModel;
import com.lunargravity.campaign.statemachine.GetReadyState;
import com.lunargravity.engine.animation.LinearInterpolationLoop;
import com.lunargravity.engine.core.IEngine;
import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.widgetsystem.*;
import org.joml.Matrix4f;
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

    private static final long MOON_ROTATION_TIME = 45000; // 45 seconds to rotate 360°
    private static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);

    private final WidgetManager _widgetManager;
    private final ICampaignController _controller;
    private final DisplayMeshCache _displayMeshCache;
    private final MaterialCache _materialCache;
    private final TextureCache _textureCache;
    private final IEngine _engine;

    private DisplayMesh _naturalSatellite;
    private final Matrix4f _mvMatrix;
    private final Matrix4f _naturalSatelliteModelMatrix;
    private final Transform _camera;
    private LinearInterpolationLoop _naturalSatelliteRotation;

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
        _displayMeshCache = new DisplayMeshCache();
        _materialCache = new MaterialCache();
        _textureCache = new TextureCache();
        _mvMatrix = new Matrix4f();
        _naturalSatelliteModelMatrix = new Matrix4f();

        _camera = new Transform();
        _camera._position.z = 40.0f;
        _camera.calculateViewMatrix();

    }

    @Override
    public void initialLoadCompleted() throws Exception {
        GlStaticMeshBuilder builder = new GlStaticMeshBuilder(_displayMeshCache, _materialCache, _textureCache);
        builder.build("meshes/Moon.obj");
        for (var a : builder.getMeshes()) {
            _displayMeshCache.add(a);
        };
        _naturalSatellite = _displayMeshCache.getByExactName("Moon.Display");

        if (_naturalSatelliteRotation != null) {
            _naturalSatelliteRotation.unregister();
        }
        _naturalSatelliteRotation = new LinearInterpolationLoop(_engine.getAnimationManager());
        _naturalSatelliteRotation.start(0.0f, 359.0f, MOON_ROTATION_TIME);
    }

    @Override
    public void viewThink() {
        // TODO
    }

    @Override
    public void drawView3d(int viewport, Matrix4f projectionMatrix) {
        if (_widgetManager.isVisible(_episodeIntro)) {
            _naturalSatelliteModelMatrix.identity().rotate((float)Math.toRadians(_naturalSatelliteRotation.getValue()), Y_AXIS);
            _mvMatrix.set(_camera.getViewMatrix()).mul(_naturalSatelliteModelMatrix);
            _naturalSatellite.draw(_widgetManager.getRenderer(), _widgetManager.getRenderer().getDirectionalLightProgram(), _mvMatrix, projectionMatrix);
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
        if (_naturalSatellite != null) {
            _naturalSatellite.freeNativeResources();
        }
        if (_naturalSatelliteRotation != null) {
            _naturalSatelliteRotation.unregister();
        }
    }

    @Override
    public void showEpisodeIntro() throws IOException {
        _widgetManager.hideAll();
        _widgetManager.show(_episodeIntro, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showEpisodeOutro() throws IOException {
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
        _widgetManager.hideAll();
        _widgetManager.show(_missionCompleted, WidgetManager.ShowAs.FIRST);
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
        else if (wci._id.equals("subTitleImage") && wci._type.equals("ImageWidget")) {
        //else if (wci._id.equals(EPISODE_INTRO_ANNOUNCEMENT) && wci._type.equals("AnnouncementWidget")) {
            //_episodeIntro = new Widget(viewportConfig, wci, new AnnouncementWidget(_widgetManager, this));
            _episodeIntro = new Widget(viewportConfig, wci, new ImageWidget(_widgetManager));
        }
        else if (wci._id.equals(EPISODE_OUTRO_ANNOUNCEMENT) && wci._type.equals("AnnouncementWidget")) {
            _episodeOutro = new Widget(viewportConfig, wci, new AnnouncementWidget(_widgetManager, this));
        }
        else if (wci._id.equals(GAME_OVER_ANNOUNCEMENT) && wci._type.equals("AnnouncementWidget")) {
            _gameOver = new Widget(viewportConfig, wci, new AnnouncementWidget(_widgetManager, this));
        }
        else if (wci._id.equals(GAME_WON_ANNOUNCEMENT) && wci._type.equals("AnnouncementWidget")) {
            _gameWon = new Widget(viewportConfig, wci, new AnnouncementWidget(_widgetManager, this));
        }
        else if (wci._id.equals(MISSION_INTRO_ANNOUNCEMENT) && wci._type.equals("AnnouncementWidget")) {
            _missionIntro = new Widget(viewportConfig, wci, new AnnouncementWidget(_widgetManager, this));
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
