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

package com.lunargravity.application;

import com.lunargravity.campaign.controller.CampaignController;
import com.lunargravity.campaign.controller.ICampaignController;
import com.lunargravity.campaign.controller.ICampaignControllerObserver;
import com.lunargravity.campaign.model.CampaignModel;
import com.lunargravity.campaign.model.ICampaignModel;
import com.lunargravity.campaign.model.SavedGameFile;
import com.lunargravity.campaign.view.CampaignView;
import com.lunargravity.dogfight.controller.DogfightController;
import com.lunargravity.dogfight.controller.IDogfightController;
import com.lunargravity.dogfight.model.DogfightModel;
import com.lunargravity.dogfight.model.IDogfightModel;
import com.lunargravity.dogfight.view.DogfightView;
import com.lunargravity.engine.core.*;
import com.lunargravity.engine.desktopwindow.GlfwWindowConfig;
import com.lunargravity.engine.graphics.ViewportConfig;
import com.lunargravity.engine.scene.ISceneBuilderObserver;
import com.lunargravity.engine.scene.SceneBuilder;
import com.lunargravity.engine.widgetsystem.WidgetManager;
import com.lunargravity.menu.controller.IMenuController;
import com.lunargravity.menu.controller.MenuController;
import com.lunargravity.menu.model.IMenuModel;
import com.lunargravity.menu.model.MenuModel;
import com.lunargravity.menu.statemachine.LoadingMenuState;
import com.lunargravity.menu.view.MenuView;
import com.lunargravity.mvc.IController;
import com.lunargravity.mvc.IModel;
import com.lunargravity.mvc.IView;
import com.lunargravity.race.controller.IRaceController;
import com.lunargravity.race.controller.RaceController;
import com.lunargravity.race.model.IRaceModel;
import com.lunargravity.race.model.RaceModel;
import com.lunargravity.race.view.RaceView;
import com.lunargravity.world.controller.*;
import com.lunargravity.world.model.*;
import com.lunargravity.world.view.GameWorldView;
import com.lunargravity.world.view.IWorldView;
import com.lunargravity.world.view.MenuWorldView;
import org.joml.Matrix4f;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Application implements
        IFrameObserver,
        IInputObserver,
        IViewportSizeObserver,
        IStateMachineContext,
        IMenuWorldControllerObserver
{
    static final private String WINDOW_TITLE = "Lunar Gravity v1.0";
    static final private String PLAYER_INPUT_BINDINGS_FILE_NAME = "playerInputBindings.json";
    static final private String MENU_SCENE_FILE_NAME = "scenes/menuScene.json";
    static final private String MENU_WORLD_SCENE_FILE_NAME = "scenes/menuWorldScene.json";

    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 960;
    private static final int ARROW_MOUSE_CURSOR = 1;
    private static final int GRAB_MOUSE_CURSOR = 2;
    private static final int HAND_MOUSE_CURSOR = 3;

    private static final int FIRST_EPISODE = 0;
    private static final int FIRST_MISSION = 0;
    private static final int FIRST_LEVEL = 0;

    private final IEngine _engine;
    private PlayerInputBindings _playerInputBindings;

    private long _frameNo;
    private long _nowMs;
    private double _frameDelta;

    private IState _currentState;
    private IState _pendingState;

    private IWorldModel _worldModel;
    private IWorldView _worldView;
    private IWorldController _worldController;

    private IModel _logicModel;
    private IView _logicView;
    private IController _logicController;

    private final WidgetManager _widgetManager;

    public Application() throws Exception {
        _frameNo = _nowMs = 0;
        _frameDelta = 0.0;

        _currentState = null;
        _pendingState = null;

        _worldModel = null;
        _worldView = null;
        _worldController = null;

        _logicModel = null;
        _logicView = null;
        _logicController = null;

        _engine = new Engine(this, this, this, createWindowConfig());

        _widgetManager = new WidgetManager(_engine);

        initialisePlayerInputBindings();

        changeStateNow(new LoadingMenuState(this));
    }

    @Override
    public void onFrameBegin(long frameNo, long nowMs, double frameDelta) {
        _frameNo = frameNo;
        _nowMs = nowMs;
        _frameDelta = frameDelta;
    }

    @Override
    public void onFrameEnd() throws Exception {
        if (_worldView != null) {
            _worldView.onFrameEnd();
        }
        if (_logicView != null) {
            _logicView.onFrameEnd();
        }
        if (_pendingState != null) {
            changeStateNow(_pendingState);
            _pendingState = null;
        }
    }

    @Override
    public void onFrameThink() {
        if (_worldController != null) {
            _worldController.controllerThink();
        }
        if (_logicController != null) {
            _logicController.controllerThink();
        }
        if (_worldView != null) {
            _worldView.viewThink();
        }
        if (_logicView != null) {
            _logicView.viewThink();
        }
        _widgetManager.think();
        _currentState.think();
    }

    @Override
    public void onFrameDraw3d(int viewport, Matrix4f projectionMatrix) {
        if (_worldView != null) {
            _worldView.drawView3d(viewport, projectionMatrix);
        }
        if (_logicView != null) {
            _logicView.drawView3d(viewport, projectionMatrix);
        }
        _currentState.draw3d(viewport, projectionMatrix);
    }

    @Override
    public void onFrameDraw2d(Matrix4f projectionMatrix) {
        if (_worldView != null) {
            _worldView.drawView2d(projectionMatrix);
        }
        if (_logicView != null) {
            _logicView.drawView2d(projectionMatrix);
        }
        _widgetManager.draw(projectionMatrix);
        _currentState.draw2d(projectionMatrix);
    }

    @Override
    public void keyboardKeyEvent(int key, int scancode, int action, int mods) {
        _currentState.keyboardKeyEvent(key, scancode, action, mods); // TODO: consider adding a 'consumed' return code
        _widgetManager.keyboardKeyEvent(key, scancode, action, mods);
    }

    @Override
    public void mouseButtonEvent(int button, int action, int mods) throws Exception {
        _currentState.mouseButtonEvent(button, action, mods); // TODO: consider adding a 'consumed' return code
        _engine.getRenderer().getOrthographicViewport().activate();
        _widgetManager.mouseButtonEvent(button, action, mods);
    }

    @Override
    public void mouseCursorMovedEvent(double xPos, double yPos) {
        _currentState.mouseCursorMovedEvent(xPos, yPos); // TODO: consider adding a 'consumed' return code
        _engine.getRenderer().getOrthographicViewport().activate();
        _widgetManager.mouseCursorMovedEvent(xPos, yPos);
    }

    @Override
    public void mouseWheelScrolledEvent(double xOffset, double yOffset) {
        _currentState.mouseWheelScrolledEvent(xOffset, yOffset); // TODO: consider adding a 'consumed' return code
        _engine.getRenderer().getOrthographicViewport().activate();
        _widgetManager.mouseWheelScrolledEvent(xOffset, yOffset);
    }

    @Override
    public ViewportConfig onViewportSizeChanged(int viewport, ViewportConfig viewportConfig, int windowWidth, int windowHeight) {
        ViewportConfig vpc = _currentState.onViewportSizeChanged(viewport, viewportConfig, windowWidth, windowHeight);
        return _widgetManager.onViewportSizeChanged(viewport, vpc, windowWidth, windowHeight);
    }

    @Override
    public void changeState(IState state) {
        _pendingState = state;
    }

    @Override
    public IEngine getEngine() {
        return _engine;
    }

    @Override
    public IModel getWorldModel() {
        return _worldModel;
    }

    @Override
    public IView getWorldView() {
        return _worldView;
    }

    @Override
    public IController getWorldController() {
        return _worldController;
    }

    @Override
    public IModel getLogicModel() {
        return _logicModel;
    }

    @Override
    public IView getLogicView() {
        return _logicView;
    }

    @Override
    public IController getLogicController() {
        return _logicController;
    }

    @Override
    public void exitApplication() {
        _engine.exit();
    }

    @Override
    public long getFrameNo() {
        return _frameNo;
    }

    @Override
    public long getNowMs() {
        return _nowMs;
    }

    @Override
    public double getFrameDelta() {
        return _frameDelta;
    }

    @Override
    public void startMenu(ISceneBuilderObserver sceneBuilderObserver) throws Exception {
        resetState();

        _worldModel = new MenuWorldModel();
        _worldController = new MenuWorldController(this, (IMenuWorldModel)_worldModel);
        _worldView = new MenuWorldView((IMenuWorldModel)_worldModel);

        _logicModel = new MenuModel();
        _logicController = new MenuController(_engine, _playerInputBindings, (IMenuModel)_logicModel);
        _logicView = new MenuView(_widgetManager, (IMenuController)_logicController, (IMenuModel)_logicModel);

        _engine.setDefaultViewport();
        _widgetManager.closeAll();
        ViewportConfig viewportConfig = _engine.getRenderer().getOrthographicViewport().getConfig();

        SceneBuilder worldSceneBuilder = new SceneBuilder(sceneBuilderObserver, _worldView, _worldView);
        worldSceneBuilder.build(viewportConfig, MENU_WORLD_SCENE_FILE_NAME);

        SceneBuilder logicSceneBuilder = new SceneBuilder(sceneBuilderObserver, _logicView, _logicView);
        logicSceneBuilder.build(viewportConfig, MENU_SCENE_FILE_NAME);

        _worldView.initialLoadCompleted();
        _logicView.initialLoadCompleted();
    }

    @Override
    public void createCampaignMvc(String savedGameFileName) throws IOException {
        resetState();

        SavedGameFile savedGameFile = new SavedGameFile(savedGameFileName); // throws

        _logicModel = new CampaignModel(savedGameFile.getEpisode(), savedGameFile.getMission(), savedGameFile.getNumPlayers());
        _logicController = new CampaignController(_engine, (ICampaignModel)_logicModel);
        _logicView = new CampaignView(_widgetManager, (ICampaignController)_logicController, (ICampaignModel)_logicModel);

        _worldModel = new GameWorldModel(_engine.getTimeoutManager(), savedGameFile.getNumPlayers());
        _worldView = new GameWorldView(_engine, (IGameWorldModel)_worldModel);
        GameWorldController gameWorldController = new GameWorldController(_engine, _logicController, (IGameWorldModel)_worldModel);
        gameWorldController.addObserver((IGameWorldControllerObserver)_worldView);
        _worldController = gameWorldController;

        ((ICampaignController) _logicController).addObserver(gameWorldController);
    }

    @Override
    public void createCampaignMvc(int numPlayers) {
        resetState();

        _logicModel = new CampaignModel(FIRST_EPISODE, FIRST_MISSION, numPlayers);
        _logicController = new CampaignController(_engine, (ICampaignModel)_logicModel);
        _logicView = new CampaignView(_widgetManager, (ICampaignController)_logicController, (ICampaignModel)_logicModel);

        _worldModel = new GameWorldModel(_engine.getTimeoutManager(), numPlayers);
        _worldView = new GameWorldView(_engine, (IGameWorldModel)_worldModel);
        GameWorldController gameWorldController = new GameWorldController(_engine, _logicController, (IGameWorldModel)_worldModel);
        gameWorldController.addObserver((IGameWorldControllerObserver)_worldView);
        _worldController = gameWorldController;

        ((ICampaignController) _logicController).addObserver(gameWorldController);
    }

    @Override
    public void loadCampaignEpisode(ISceneBuilderObserver sceneBuilderObserver) throws Exception {
        _engine.setDefaultViewport();
        _widgetManager.closeAll();

        ICampaignModel model = (ICampaignModel)_logicModel;
        ViewportConfig viewportConfig = _engine.getRenderer().getOrthographicViewport().getConfig();

        _worldView.resetState();
        _logicView.resetState();

        SceneBuilder logicSceneBuilder = new SceneBuilder(sceneBuilderObserver, _logicView, _logicView);
        logicSceneBuilder.build(viewportConfig, model.getEpisodeIntroScene());

        _worldView.initialLoadCompleted();
        _logicView.initialLoadCompleted();
    }

    @Override
    public void loadCampaignMission(ISceneBuilderObserver sceneBuilderObserver) throws Exception {
        _engine.setDefaultViewport();
        _widgetManager.closeAll();

        _worldView.resetState();
        _logicView.resetState();

        ICampaignModel model = (ICampaignModel)_logicModel;
        ViewportConfig viewportConfig = _engine.getRenderer().getOrthographicViewport().getConfig();

        SceneBuilder worldSceneBuilder = new SceneBuilder(sceneBuilderObserver, _worldView, _worldView);
        worldSceneBuilder.build(viewportConfig, model.getWorldMissionScene());

        SceneBuilder logicSceneBuilder = new SceneBuilder(sceneBuilderObserver, _logicView, _logicView);
        logicSceneBuilder.build(viewportConfig, model.getLogicMissionScene());

        _worldView.initialLoadCompleted();
        _logicView.initialLoadCompleted();
    }

    @Override
    public void startRaceGame(ISceneBuilderObserver sceneBuilderObserver, int numPlayers) throws Exception {
        resetState();

        _logicModel = new RaceModel(FIRST_LEVEL, numPlayers);
        _logicController = new RaceController((IRaceModel)_logicModel);
        _logicView = new RaceView(_widgetManager, (IRaceController)_logicController, (IRaceModel)_logicModel);

        _worldModel = new GameWorldModel(_engine.getTimeoutManager(), numPlayers);
        _worldView = new GameWorldView(_engine, (IGameWorldModel)_worldModel);
        GameWorldController gameWorldController = new GameWorldController(_engine, _logicController, (IGameWorldModel)_worldModel);
        gameWorldController.addObserver((IGameWorldControllerObserver)_worldView);
        _worldController = gameWorldController;

        loadRaceLevel(sceneBuilderObserver, numPlayers);
    }

    @Override
    public void loadRaceLevel(ISceneBuilderObserver sceneBuilderObserver, int numPlayers) throws Exception {
        _engine.setDefaultViewport();
        _widgetManager.closeAll();

        _worldView.resetState();
        _logicView.resetState();

        IRaceModel model = (IRaceModel)_logicModel;
        ViewportConfig viewportConfig = _engine.getRenderer().getOrthographicViewport().getConfig();

        SceneBuilder worldSceneBuilder = new SceneBuilder(sceneBuilderObserver, _worldView, _worldView);
        worldSceneBuilder.build(viewportConfig, model.getWorldLevelScene());

        SceneBuilder logicSceneBuilder = new SceneBuilder(sceneBuilderObserver, _logicView, _logicView);
        logicSceneBuilder.build(viewportConfig, model.getLogicLevelScene());

        _worldView.initialLoadCompleted();
        _logicView.initialLoadCompleted();
    }

    @Override
    public void startDogfightGame(ISceneBuilderObserver sceneBuilderObserver, int numPlayers) throws Exception {
        resetState();

        _logicModel = new DogfightModel(FIRST_LEVEL, numPlayers);
        _logicController = new DogfightController((IDogfightModel)_logicModel);
        _logicView = new DogfightView(_widgetManager, (IDogfightController)_logicController, (IDogfightModel)_logicModel);

        _worldModel = new GameWorldModel(_engine.getTimeoutManager(), numPlayers);
        _worldView = new GameWorldView(_engine, (IGameWorldModel)_worldModel);
        GameWorldController gameWorldController = new GameWorldController(_engine, _logicController, (IGameWorldModel)_worldModel);
        gameWorldController.addObserver((IGameWorldControllerObserver)_worldView);
        _worldController = gameWorldController;

        loadDogfightLevel(sceneBuilderObserver, numPlayers);
    }

    @Override
    public void loadDogfightLevel(ISceneBuilderObserver sceneBuilderObserver, int numPlayers) throws Exception {
        _engine.setDefaultViewport();
        _widgetManager.closeAll();

        _worldView.resetState();
        _logicView.resetState();

        IDogfightModel model = (IDogfightModel)_logicModel;
        ViewportConfig viewportConfig = _engine.getRenderer().getOrthographicViewport().getConfig();

        SceneBuilder worldSceneBuilder = new SceneBuilder(sceneBuilderObserver, _worldView, _worldView);
        worldSceneBuilder.build(viewportConfig, model.getWorldLevelScene());

        SceneBuilder logicSceneBuilder = new SceneBuilder(sceneBuilderObserver, _logicView, _logicView);
        logicSceneBuilder.build(viewportConfig, model.getLogicLevelScene());

        _worldView.initialLoadCompleted();
        _logicView.initialLoadCompleted();
    }

    public void resetState() {
        if (_logicController instanceof CampaignController campaignController) {
            campaignController.removeObserver((ICampaignControllerObserver)_worldController);
        }
        if (_worldController != null) {
            _worldController.clearPhysicsCollisionListener();
        }
        if (_worldModel != null) {
            _worldModel.removeTimeouts(_engine.getTimeoutManager());
        }
        if (_worldView != null) {
            _worldView.freeResources();
        }
    }

    public void run() throws Exception {
        _engine.run();
    }

    public void freeResources() {
        _widgetManager.freeResources();
        _engine.freeResources();
    }

    public void changeStateNow(IState state) throws Exception {
        if (_currentState != null) {
            _currentState.end();
        }
        _currentState = state;
        if (_currentState != null) {
            _currentState.begin();
        }
    }

    private GlfwWindowConfig createWindowConfig() {
        GlfwWindowConfig windowConfig = new GlfwWindowConfig();
        windowConfig._title = WINDOW_TITLE;
        windowConfig._positionX = 0;
        windowConfig._positionY = 0;
        windowConfig._width = WINDOW_WIDTH;
        windowConfig._height = WINDOW_HEIGHT;
        windowConfig._resizeable = false;
        windowConfig._centered = true;
        windowConfig._iconFileNames = new String[] {
            "images/Moon16x16.png",
            "images/Moon24x24.png",
            "images/Moon32x32.png",
            "images/Moon48x48.png",
            "images/Moon64x64.png"
        };
        windowConfig._mouseCursors = new HashMap<>();
        windowConfig._mouseCursors.put(ARROW_MOUSE_CURSOR,
                new GlfwWindowConfig.MouseCursorConfig("images/ArrowMouseCursor.png", 0, 0));
        windowConfig._mouseCursors.put(GRAB_MOUSE_CURSOR,
                new GlfwWindowConfig.MouseCursorConfig("images/GrabMouseCursor.png", 23, 10));
        windowConfig._mouseCursors.put(HAND_MOUSE_CURSOR,
                new GlfwWindowConfig.MouseCursorConfig("images/HandMouseCursor.png", 19, 3));
        windowConfig._initialMouseCursor = ARROW_MOUSE_CURSOR;
        return windowConfig;
    }

    private void initialisePlayerInputBindings() {
        _playerInputBindings = new PlayerInputBindings();

        File file = new File(PLAYER_INPUT_BINDINGS_FILE_NAME);
        if (file.exists() && file.isFile()) {
            try {
                _playerInputBindings.load(file);
            }
            catch (IOException e) {
                // TODO: Is there something useful we can do with the exception?
            }
        }
    }

    public static void main(String[] args) {
        Application app = null;
        try {
            app = new Application();
            app.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (app != null) {
                app.freeResources();
            }
        }
    }

    @Override
    public void onMenuWorldControllerEvent() {
        // TODO
    }
}
