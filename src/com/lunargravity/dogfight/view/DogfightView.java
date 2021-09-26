package com.lunargravity.dogfight.view;

import com.lunargravity.dogfight.controller.IDogfightController;
import com.lunargravity.dogfight.model.IDogfightModel;
import com.lunargravity.engine.graphics.GlMaterial;
import com.lunargravity.engine.graphics.GlStaticMesh;
import com.lunargravity.engine.graphics.GlTexture;
import com.lunargravity.engine.graphics.Transform;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.widgetsystem.ImageWidget;
import com.lunargravity.engine.widgetsystem.Widget;
import com.lunargravity.engine.widgetsystem.WidgetCreateInfo;
import com.lunargravity.engine.widgetsystem.WidgetManager;
import org.joml.Matrix4f;

import java.io.IOException;

public class DogfightView implements
        IDogfightView,
        ISceneAssetOwner,
        IDogfightPausedObserver,
        IDogfightResultsObserver
{
    private static final String RACE_RESULTS = "dogfightResults";
    private static final String RACE_PAUSED = "dogfightPaused";
    private static final String GET_READY = "getReady";
    private static final String RACE_COMPLETED = "dogfightCompleted";

    private final WidgetManager _widgetManager;
    private final IDogfightController _controller;
    private final IDogfightModel _model;

    private Widget _dogfightResults;
    private Widget _dogfightPaused;
    private Widget _getReady;
    private Widget _dogfightCompleted;

    public DogfightView(WidgetManager widgetManager, IDogfightController controller, IDogfightModel model) {
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
        _widgetManager.show(_dogfightResults, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showPausedWidget() {
        _widgetManager.hideAll();
        _widgetManager.show(_dogfightPaused, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showGetReadyWidget(int countdown) throws IOException {
        ImageWidget imageWidget = (ImageWidget)_getReady.getObserver();
        imageWidget.setBackgroundImage(String.format("images/DogfightGetReady%02d.png", countdown));
        _widgetManager.hideAll();
        _widgetManager.show(_getReady, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showCompletedWidget() {
        _widgetManager.hideAll();
        _widgetManager.show(_dogfightCompleted, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void widgetLoaded(WidgetCreateInfo wci) throws IOException {
        if (wci == null) {
            System.out.print("DogfightView.widgetLoaded() was passed a null WidgetCreateInfo object");
            return;
        }
        if (wci._id.equals(RACE_RESULTS) && wci._type.equals("DogfightResultsWidget")) {
            _dogfightResults = new Widget(wci, new DogfightResultsWidget(_widgetManager, this));
        }
        else if (wci._id.equals(RACE_PAUSED) && wci._type.equals("DogfightPausedWidget")) {
            _dogfightPaused = new Widget(wci, new DogfightPausedWidget(_widgetManager, this));
        }
        else if (wci._id.equals(RACE_COMPLETED) && wci._type.equals("ImageWidget")) {
            _dogfightCompleted = new Widget(wci, new ImageWidget(_widgetManager));
        }
        else if (wci._id.equals(GET_READY) && wci._type.equals("ImageWidget")) {
            _getReady = new Widget(wci, new ImageWidget(_widgetManager));
        }
    }

    @Override
    public void resumeDogfightButtonClicked() {
        _controller.resumeDogfight();
    }

    @Override
    public void mainMenuButtonClicked() {
        _controller.mainMenuRequested();
    }

    @Override
    public void startNextDogfightButtonClicked() {
        _controller.startNextDogfight();
    }
}
