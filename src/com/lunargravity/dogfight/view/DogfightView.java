package com.lunargravity.dogfight.view;

import com.lunargravity.dogfight.controller.IDogfightController;
import com.lunargravity.dogfight.model.IDogfightModel;
import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.widgetsystem.*;
import org.joml.Matrix4f;

public class DogfightView implements
        IDogfightView,
        ISceneAssetOwner,
        IDogfightPausedObserver,
        IDogfightScoreboardObserver,
        IDogfightResultsObserver
{
    private static final String DOGFIGHT_RESULTS = "dogfightResults";
    private static final String DOGFIGHT_SCOREBOARD = "dogfightScoreboard";
    private static final String DOGFIGHT_PAUSED = "dogfightPaused";
    private static final String GET_READY = "getReady";
    private static final String DOGFIGHT_COMPLETED = "dogfightCompleted";

    private final WidgetManager _widgetManager;
    private final IDogfightController _controller;
    private final IDogfightModel _model;

    private Widget _dogfightResults;
    private Widget _dogfightScoreboard;
    private Widget _dogfightPaused;
    private Widget _getReady;
    private Widget _dogfightCompleted;

    public DogfightView(WidgetManager widgetManager, IDogfightController controller, IDogfightModel model) {
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
        if (wci._id.equals(DOGFIGHT_SCOREBOARD) && wci._type.equals("DogfightScoreboardWidget")) {
            _dogfightScoreboard = new Widget(wci, new DogfightScoreboardWidget(_widgetManager, this));
        }
        else if (wci._id.equals(DOGFIGHT_RESULTS) && wci._type.equals("DogfightResultsWidget")) {
            _dogfightResults = new Widget(wci, new DogfightResultsWidget(_widgetManager, this));
        }
        else if (wci._id.equals(DOGFIGHT_PAUSED) && wci._type.equals("DogfightPausedWidget")) {
            _dogfightPaused = new Widget(wci, new DogfightPausedWidget(_widgetManager, this));
        }
        else if (wci._id.equals(DOGFIGHT_COMPLETED) && wci._type.equals("ImageWidget")) {
            _dogfightCompleted = new Widget(wci, new ImageWidget(_widgetManager));
        }
        else if (wci._id.equals(GET_READY) && wci._type.equals("ImageWidget")) {
            _getReady = new Widget(wci, new ImageWidget(_widgetManager));
        }
    }

    @Override
    public void resumeDogfightButtonClicked() {
        // TODO: changeState();
    }

    @Override
    public void quitDogfightButtonClicked() {
        // TODO: changeState();
    }

    @Override
    public void startNextDogfightButtonClicked() {
        // TODO: changeState();
    }

    @Override
    public void quitToDogfightScoreboardButtonClicked() {
        // TODO: changeState();
    }

    @Override
    public void resetDogfightScoreboardButtonClicked() {
        // TODO
    }

    @Override
    public void startSinglePlayerDogfightButtonClicked() {
        // TODO: changeState();
    }

    @Override
    public void startTwoPlayersDogfightButtonClicked() {
        // TODO: changeState();
    }

    @Override
    public void mainMenuButtonClicked() {
        // TODO: changeState();
    }
}
