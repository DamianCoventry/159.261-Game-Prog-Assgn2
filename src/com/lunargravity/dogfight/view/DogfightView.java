package com.lunargravity.dogfight.view;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.lunargravity.campaign.statemachine.GetReadyState;
import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.widgetsystem.ImageWidget;
import com.lunargravity.engine.widgetsystem.Widget;
import com.lunargravity.engine.widgetsystem.WidgetCreateInfo;
import com.lunargravity.engine.widgetsystem.WidgetManager;
import com.lunargravity.dogfight.controller.IDogfightController;
import com.lunargravity.dogfight.model.IDogfightModel;
import org.joml.Matrix4f;

import java.io.IOException;

public class DogfightView implements
        IDogfightView,
        ISceneAssetOwner,
        IDogfightPausedObserver,
        IDogfightResultsObserver
{
    private static final String DOGFIGHT_RESULTS = "dogfightResults";
    private static final String DOGFIGHT_PAUSED = "dogfightPaused";
    private static final String GET_READY = "getReady";
    private static final String DOGFIGHT_COMPLETED = "dogfightCompleted";

    private final WidgetManager _widgetManager;
    private final IDogfightController _controller;
    private final IDogfightModel _model;
    private final DisplayMeshCache _displayMeshCache;
    private final MaterialCache _materialCache;
    private final TextureCache _textureCache;

    private Widget _dogfightResults;
    private Widget _dogfightPaused;
    private Widget _getReady;
    private Widget _dogfightCompleted;

    public DogfightView(WidgetManager widgetManager, IDogfightController controller, IDogfightModel model) {
        _widgetManager = widgetManager;
        _controller = controller;
        _model = model;
        _displayMeshCache = new DisplayMeshCache();
        _materialCache = new MaterialCache();
        _textureCache = new TextureCache();
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
        // TODO
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
    public void showResultsWidget() throws IOException {
        _widgetManager.hideAll();
        _widgetManager.show(_dogfightResults, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showPausedWidget() throws IOException {
        _widgetManager.hideAll();
        _widgetManager.show(_dogfightPaused, WidgetManager.ShowAs.FIRST);
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
    public void showCompletedWidget() throws IOException {
        _widgetManager.hideAll();
        _widgetManager.show(_dogfightCompleted, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showLevelStatusBar() {
        _widgetManager.hideAll();
        // TODO: show the level status bar
    }

    @Override
    public void widgetLoaded(ViewportConfig viewportConfig, WidgetCreateInfo wci) throws IOException {
        if (wci == null) {
            System.out.print("DogfightView.widgetLoaded() was passed a null WidgetCreateInfo object");
            return;
        }
        if (wci._id.equals(DOGFIGHT_RESULTS) && wci._type.equals("DogfightResultsWidget")) {
            _dogfightResults = new Widget(viewportConfig, wci, new DogfightResultsWidget(_widgetManager, this));
        }
        else if (wci._id.equals(DOGFIGHT_PAUSED) && wci._type.equals("DogfightPausedWidget")) {
            _dogfightPaused = new Widget(viewportConfig, wci, new DogfightPausedWidget(_widgetManager, this));
        }
        else if (wci._id.equals(DOGFIGHT_COMPLETED) && wci._type.equals("ImageWidget")) {
            _dogfightCompleted = new Widget(viewportConfig, wci, new ImageWidget(_widgetManager));
        }
        else if (wci._id.equals(GET_READY) && wci._type.equals("ImageWidget")) {
            _getReady = new Widget(viewportConfig, wci, new ImageWidget(_widgetManager));
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
