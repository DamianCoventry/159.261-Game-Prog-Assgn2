package com.lunargravity.campaign.view;

import com.lunargravity.campaign.controller.ICampaignController;
import com.lunargravity.campaign.model.ICampaignModel;
import com.lunargravity.campaign.statemachine.GetReadyState;
import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.widgetsystem.*;
import org.joml.Matrix4f;

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
    private static final String GAME_WON_ANNOUNCEMENT = "gameCompleted";
    private static final String MISSION_INTRO_ANNOUNCEMENT = "missionIntro";
    private static final String MISSION_COMPLETED = "missionCompleted";
    private static final String GET_READY = "getReady";
    private static final String PLAYER_DIED = "playerDied";

    private final WidgetManager _widgetManager;
    private final ICampaignController _controller;
    private final ICampaignModel _model;

    private Widget _episodeIntro;
    private Widget _episodeOutro;
    private Widget _gameOver;
    private Widget _gameWon;
    private Widget _missionIntro;
    private Widget _missionPaused;
    private Widget _missionCompleted;
    private Widget _getReady;
    private Widget _playerDied;

    public CampaignView(WidgetManager widgetManager, ICampaignController controller, ICampaignModel model) {
        _widgetManager = widgetManager;
        _controller = controller;
        _model = model;
    }

    @Override
    public void initialLoadCompleted() {
        // Nothing to do?
        //_widgetManager.show(_episodeIntro, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void viewThink() {
        // TODO
    }

    @Override
    public void drawView3d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void drawView2d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void showEpisodeIntro() {
        _widgetManager.hideAll();
        _widgetManager.show(_episodeIntro, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showEpisodeOutro() {
        _widgetManager.hideAll();
        _widgetManager.show(_episodeOutro, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showMissionIntro() {
        _widgetManager.hideAll();
        _widgetManager.show(_missionIntro, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showGameWon() {
        _widgetManager.hideAll();
        _widgetManager.show(_gameWon, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showGameOver() {
        _widgetManager.hideAll();
        _widgetManager.show(_gameOver, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showMissionPaused() {
        _widgetManager.hideAll();
        _widgetManager.show(_missionPaused, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showMissionCompleted() {
        _widgetManager.hideAll();
        _widgetManager.show(_missionCompleted, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showMissionStatusBar() {
        _widgetManager.hideAll();
        // TODO: show mission status bar
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
    public void objectLoaded(String name, String type, Transform transform) {
        // TODO
    }

    @Override
    public void staticMeshLoaded(GlStaticMesh staticMesh) {
        // TODO
    }

    @Override
    public void materialLoaded(GlMaterial material) {
        // TODO
    }

    @Override
    public void textureLoaded(GlTexture texture) {
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
        else if (wci._id.equals(EPISODE_INTRO_ANNOUNCEMENT) && wci._type.equals("AnnouncementWidget")) {
            _episodeIntro = new Widget(viewportConfig, wci, new AnnouncementWidget(_widgetManager, this));
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
    public void announcementClicked(String widgetId) throws IOException, InterruptedException {
        switch (widgetId) {
            case EPISODE_INTRO_ANNOUNCEMENT -> _controller.episodeIntroAborted();
            case EPISODE_OUTRO_ANNOUNCEMENT -> _controller.episodeOutroAborted();
            case GAME_OVER_ANNOUNCEMENT -> _controller.gameOverAborted();
            case GAME_WON_ANNOUNCEMENT -> _controller.gameWonAborted();
            case MISSION_INTRO_ANNOUNCEMENT -> _controller.missionIntroAborted();
        }
    }
}
