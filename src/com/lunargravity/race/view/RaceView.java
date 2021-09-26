package com.lunargravity.race.view;

import com.lunargravity.engine.graphics.GlMaterial;
import com.lunargravity.engine.graphics.GlStaticMesh;
import com.lunargravity.engine.graphics.GlTexture;
import com.lunargravity.engine.graphics.Transform;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.widgetsystem.ImageWidget;
import com.lunargravity.engine.widgetsystem.Widget;
import com.lunargravity.engine.widgetsystem.WidgetCreateInfo;
import com.lunargravity.engine.widgetsystem.WidgetManager;
import com.lunargravity.race.controller.IRaceController;
import com.lunargravity.race.model.IRaceModel;
import org.joml.Matrix4f;

import java.io.IOException;

public class RaceView implements
        IRaceView,
        ISceneAssetOwner,
        IRacePausedObserver,
        IRaceResultsObserver
{
    private static final String RACE_RESULTS = "raceResults";
    private static final String RACE_PAUSED = "racePaused";
    private static final String GET_READY = "getReady";
    private static final String RACE_COMPLETED = "raceCompleted";

    private final WidgetManager _widgetManager;
    private final IRaceController _controller;
    private final IRaceModel _model;

    private Widget _raceResults;
    private Widget _racePaused;
    private Widget _getReady;
    private Widget _raceCompleted;

    public RaceView(WidgetManager widgetManager, IRaceController controller, IRaceModel model) {
        _widgetManager = widgetManager;
        _controller = controller;
        _model = model;
    }

    @Override
    public void initialLoadCompleted() {
        // TODO
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
    public void showResultsWidget() {
        _widgetManager.hideAll();
        _widgetManager.show(_raceResults, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showPausedWidget() {
        _widgetManager.hideAll();
        _widgetManager.show(_racePaused, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showGetReadyWidget(int countdown) throws IOException {
        ImageWidget imageWidget = (ImageWidget)_getReady.getObserver();
        imageWidget.setBackgroundImage(String.format("images/RaceGetReady%02d.png", countdown));
        _widgetManager.hideAll();
        _widgetManager.show(_getReady, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showCompletedWidget() {
        _widgetManager.hideAll();
        _widgetManager.show(_raceCompleted, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void widgetLoaded(WidgetCreateInfo wci) throws IOException {
        if (wci == null) {
            System.out.print("RaceView.widgetLoaded() was passed a null WidgetCreateInfo object");
            return;
        }
        if (wci._id.equals(RACE_RESULTS) && wci._type.equals("RaceResultsWidget")) {
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
        _controller.resumeRace();
    }

    @Override
    public void mainMenuButtonClicked() {
        _controller.mainMenuRequested();
    }

    @Override
    public void startNextRaceButtonClicked() {
        _controller.startNextRace();
    }
}
