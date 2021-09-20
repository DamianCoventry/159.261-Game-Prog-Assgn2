package com.lunargravity.race.view;

import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.widgetsystem.*;
import com.lunargravity.race.controller.IRaceController;
import com.lunargravity.race.model.IRaceModel;
import org.joml.Matrix4f;

public class RaceView implements
        IRaceView,
        ISceneAssetOwner,
        IRacePausedObserver,
        IRaceScoreboardObserver,
        IRaceResultsObserver
{
    private static final String RACE_RESULTS = "raceResults";
    private static final String RACE_SCOREBOARD = "raceScoreboard";
    private static final String RACE_PAUSED = "racePaused";
    private static final String GET_READY = "getReady";
    private static final String RACE_COMPLETED = "raceCompleted";

    private final WidgetManager _widgetManager;
    private final IRaceController _controller;
    private final IRaceModel _model;

    private Widget _raceResults;
    private Widget _raceScoreboard;
    private Widget _racePaused;
    private Widget _getReady;
    private Widget _raceCompleted;

    public RaceView(WidgetManager widgetManager, IRaceController controller, IRaceModel model) {
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
        if (wci._id.equals(RACE_SCOREBOARD) && wci._type.equals("RaceScoreboardWidget")) {
            _raceScoreboard = new Widget(wci, new RaceScoreboardWidget(_widgetManager, this));
        }
        else if (wci._id.equals(RACE_RESULTS) && wci._type.equals("RaceResultsWidget")) {
            _raceResults = new Widget(wci, new RaceResultsWidget(_widgetManager, this));
        }
        else if (wci._id.equals(RACE_PAUSED) && wci._type.equals("RacePausedWidget")) {
            _racePaused = new Widget(wci, new RacePausedWidget(_widgetManager, this));
        }
        else if (wci._id.equals(RACE_COMPLETED) && wci._type.equals("ImageWidget")) {
            _raceCompleted = new Widget(wci, new ImageWidget(_widgetManager));
        }
        else if (wci._id.equals(GET_READY) && wci._type.equals("ImageWidget")) {
            _getReady = new Widget(wci, new ImageWidget(_widgetManager));
        }
    }

    @Override
    public void resumeRaceButtonClicked() {
        // TODO: changeState();
    }

    @Override
    public void quitRaceButtonClicked() {
        // TODO: changeState();
    }

    @Override
    public void startNextRaceButtonClicked() {
        // TODO: changeState();
    }

    @Override
    public void quitToRaceScoreboardButtonClicked() {
        // TODO: changeState();
    }

    @Override
    public void resetRaceScoreboardButtonClicked() {
        // TODO
    }

    @Override
    public void startSinglePlayerRaceButtonClicked() {
        // TODO: changeState();
    }

    @Override
    public void startTwoPlayersRaceButtonClicked() {
        // TODO: changeState();
    }

    @Override
    public void mainMenuButtonClicked() {
        // TODO: changeState();
    }
}
