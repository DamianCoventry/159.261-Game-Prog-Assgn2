package com.lunargravity.campaign.view;

import com.lunargravity.campaign.controller.ICampaignController;
import com.lunargravity.campaign.model.ICampaignModel;
import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.widgetsystem.*;
import org.joml.Matrix4f;

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
    private static final String BOTH_PLAYERS_DIED = "bothPlayersDied";
    private static final String PLAYER1_DIED = "player1Died";
    private static final String PLAYER2_DIED = "player2Died";

    private final WidgetManager _widgetManager;
    private final ICampaignController _controller;
    private final ICampaignModel _model;

    // These 5 widgets are announcements that fill the viewport
    private Widget _episodeIntro;
    private Widget _episodeOutro;
    private Widget _gameOver;
    private Widget _gameWon;
    private Widget _missionIntro;

    // This widget is a dialog box, with 2 buttons and a background image
    private Widget _missionPaused;

    // These 5 widgets are images centered within the viewport
    private Widget _missionCompleted;
    private Widget _getReady;
    private Widget _bothPlayersDied;
    private Widget _player1Died;
    private Widget _player2Died;

    public CampaignView(WidgetManager widgetManager, ICampaignController controller, ICampaignModel model) {
        _widgetManager = widgetManager;
        _controller = controller;
        _model = model;
    }

    @Override
    public void onViewThink() {
        // TODO
    }

    @Override
    public void onDrawView3d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void onDrawView2d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void temp() {
        // TODO
    }

    @Override
    public void onObjectLoaded(String name, String type, GlTransform transform) {
        // TODO
    }

    @Override
    public void onStaticMeshLoaded(GlStaticMesh staticMesh) {
        // TODO
    }

    @Override
    public void onMaterialLoaded(GlMaterial material) {
        // TODO
    }

    @Override
    public void onTextureLoaded(GlTexture texture) {
        // TODO
    }

    @Override
    public void onWidgetLoaded(WidgetCreateInfo wci) {
        if (wci._id.equals(MISSION_PAUSED) && wci._type.equals("MissionPausedWidget")) {
            _missionPaused = new Widget(wci, new MissionPausedWidget(_widgetManager, this));
        }
        else if (wci._id.equals(EPISODE_INTRO_ANNOUNCEMENT) && wci._type.equals("AnnouncementWidget")) {
            _episodeIntro = new Widget(wci, new AnnouncementWidget(_widgetManager, this));
        }
        else if (wci._id.equals(EPISODE_OUTRO_ANNOUNCEMENT) && wci._type.equals("AnnouncementWidget")) {
            _episodeOutro = new Widget(wci, new AnnouncementWidget(_widgetManager, this));
        }
        else if (wci._id.equals(GAME_OVER_ANNOUNCEMENT) && wci._type.equals("AnnouncementWidget")) {
            _missionIntro = new Widget(wci, new AnnouncementWidget(_widgetManager, this));
        }
        else if (wci._id.equals(GAME_WON_ANNOUNCEMENT) && wci._type.equals("AnnouncementWidget")) {
            _gameOver = new Widget(wci, new AnnouncementWidget(_widgetManager, this));
        }
        else if (wci._id.equals(MISSION_INTRO_ANNOUNCEMENT) && wci._type.equals("AnnouncementWidget")) {
            _gameWon = new Widget(wci, new AnnouncementWidget(_widgetManager, this));
        }
        else if (wci._id.equals(MISSION_COMPLETED) && wci._type.equals("ImageWidget")) {
            _missionCompleted = new Widget(wci, new ImageWidget(_widgetManager));
        }
        else if (wci._id.equals(GET_READY) && wci._type.equals("ImageWidget")) {
            _getReady = new Widget(wci, new ImageWidget(_widgetManager));
        }
        else if (wci._id.equals(BOTH_PLAYERS_DIED) && wci._type.equals("ImageWidget")) {
            _bothPlayersDied = new Widget(wci, new ImageWidget(_widgetManager));
        }
        else if (wci._id.equals(PLAYER1_DIED) && wci._type.equals("ImageWidget")) {
            _player1Died = new Widget(wci, new ImageWidget(_widgetManager));
        }
        else if (wci._id.equals(PLAYER2_DIED) && wci._type.equals("ImageWidget")) {
            _player2Died = new Widget(wci, new ImageWidget(_widgetManager));
        }
    }

    @Override
    public void resumeMissionButtonClicked() {
        // TODO: changeState();
    }

    @Override
    public void quitCampaignButtonClicked() {
        // TODO: changeState();
    }

    @Override
    public void announcementClicked(String widgetId) {
        if (widgetId == EPISODE_INTRO_ANNOUNCEMENT) {
            // TODO: changeState();
        }
        else if (widgetId == EPISODE_OUTRO_ANNOUNCEMENT) {
            // TODO: changeState();
        }
        else if (widgetId == GAME_OVER_ANNOUNCEMENT) {
            // TODO: changeState();
        }
        else if (widgetId == GAME_WON_ANNOUNCEMENT) {
            // TODO: changeState();
        }
        else if (widgetId == MISSION_INTRO_ANNOUNCEMENT) {
            // TODO: changeState();
        }
    }
}
