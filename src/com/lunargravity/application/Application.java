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

import com.lunargravity.campaign.controller.*;
import com.lunargravity.campaign.model.*;
import com.lunargravity.campaign.view.*;
import com.lunargravity.dogfight.controller.*;
import com.lunargravity.dogfight.model.*;
import com.lunargravity.dogfight.view.*;
import com.lunargravity.engine.core.*;
import com.lunargravity.engine.desktopwindow.GlfwWindowConfig;
import com.lunargravity.engine.graphics.GlViewportConfig;
import com.lunargravity.engine.scene.*;
import com.lunargravity.engine.widgetsystem.WidgetManager;
import com.lunargravity.menu.controller.*;
import com.lunargravity.menu.model.*;
import com.lunargravity.menu.statemachine.LoadingMenuState;
import com.lunargravity.menu.view.*;
import com.lunargravity.mvc.*;
import com.lunargravity.race.controller.*;
import com.lunargravity.race.model.*;
import com.lunargravity.race.view.*;
import com.lunargravity.world.controller.*;
import com.lunargravity.world.model.*;
import com.lunargravity.world.view.*;
import org.joml.Matrix4f;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Application implements
        IFrameConsumer,
        IInputConsumer,
        IViewportSizeObserver,
        IStateMachineContext,
        IMenuWorldControllerObserver,
        IGameWorldControllerObserver,
        ICampaignControllerObserver {

    static final private String WINDOW_TITLE = "Lunar Gravity v1.0";
    static final private String PLAYER_INPUT_BINDINGS_FILE_NAME = "playerInputBindings.json";
    static final private String MENU_SCENE_FILE_NAME = "menuScene.json";
    static final private String MENU_WORLD_SCENE_FILE_NAME = "menuWorldScene.json";

    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 960;
    private static final int ARROW_MOUSE_CURSOR = 1;
    private static final int GRAB_MOUSE_CURSOR = 2;
    private static final int HAND_MOUSE_CURSOR = 3;

    private static final int FIRST_EPISODE = 0;
    private static final int FIRST_MISSION = 0;

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

    public Application() throws IOException {
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

        _widgetManager = new WidgetManager();

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
    public void onFrameEnd() {
        if (_pendingState != null) {
            changeStateNow(_pendingState);
            _pendingState = null;
        }
    }

    @Override
    public void onFrameThink() {
        if (_worldController != null) {
            _worldController.onControllerThink();
        }
        if (_logicController != null) {
            _logicController.onControllerThink();
        }
        if (_worldView != null) {
            _worldView.onViewThink();
        }
        if (_logicView != null) {
            _logicView.onViewThink();
        }
        _widgetManager.think();
        _currentState.think();
    }


    @Override
    public void onFrameDraw3d(int viewport, Matrix4f projectionMatrix) {
        if (_worldView != null) {
            _worldView.onDrawView3d(viewport, projectionMatrix);
        }
        if (_logicView != null) {
            _logicView.onDrawView3d(viewport, projectionMatrix);
        }
        _currentState.draw3d(viewport, projectionMatrix);
    }

    @Override
    public void onFrameDraw2d(int viewport, Matrix4f projectionMatrix) {
        if (_worldView != null) {
            _worldView.onDrawView2d(viewport, projectionMatrix);
        }
        if (_logicView != null) {
            _logicView.onDrawView2d(viewport, projectionMatrix);
        }
        _widgetManager.draw2d(viewport, projectionMatrix);
        _currentState.draw2d(viewport, projectionMatrix);
    }

    @Override
    public void onKeyboardKeyEvent(int key, int scancode, int action, int mods) {
        _currentState.onKeyboardKeyEvent(key, scancode, action, mods); // TODO: consider adding a 'consumed' return code
        _widgetManager.onKeyboardKeyEvent(key, scancode, action, mods);
    }

    @Override
    public void onMouseButtonEvent(int button, int action, int mods) {
        _currentState.onMouseButtonEvent(button, action, mods); // TODO: consider adding a 'consumed' return code
        _widgetManager.onMouseButtonEvent(button, action, mods);
    }

    @Override
    public void onMouseCursorMovedEvent(double xPos, double yPos) {
        _currentState.onMouseCursorMovedEvent(xPos, yPos); // TODO: consider adding a 'consumed' return code
        _widgetManager.onMouseCursorMovedEvent(xPos, yPos);
    }

    @Override
    public void onMouseWheelScrolledEvent(double xOffset, double yOffset) {
        _currentState.onMouseWheelScrolledEvent(xOffset, yOffset); // TODO: consider adding a 'consumed' return code
        _widgetManager.onMouseWheelScrolledEvent(xOffset, yOffset);
    }

    @Override
    public GlViewportConfig onViewportSizeChanged(int viewport, GlViewportConfig currentConfig, int windowWidth, int windowHeight) {
        GlViewportConfig newViewportConfig = _currentState.onViewportSizeChanged(viewport, currentConfig, windowWidth, windowHeight);
        return _widgetManager.onViewportSizeChanged(viewport, newViewportConfig, windowWidth, windowHeight);
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
    public void startMenu(ISceneBuilderObserver sceneBuilderObserver) {
        _worldModel = new MenuWorldModel();
        _worldController = new MenuWorldController(this, (IMenuWorldModel)_worldModel);
        _worldView = new MenuWorldView((IMenuWorldModel)_worldModel);

        _logicModel = new MenuModel();
        _logicController = new MenuController(_engine, _playerInputBindings, (IMenuModel)_logicModel);
        _logicView = new MenuView(_widgetManager, (IMenuController)_logicController, (IMenuModel)_logicModel);

        _engine.setDefaultViewport();
        _widgetManager.closeAll();

        SceneBuilder worldSceneBuilder = new SceneBuilder(sceneBuilderObserver, _worldModel, _worldView, _worldController);
        worldSceneBuilder.build(MENU_WORLD_SCENE_FILE_NAME);

        SceneBuilder logicSceneBuilder = new SceneBuilder(sceneBuilderObserver, _logicModel, _logicView, _logicController);
        logicSceneBuilder.build(MENU_SCENE_FILE_NAME);
    }

    @Override
    public void startCampaignGame(ISceneBuilderObserver sceneBuilderObserver, String savedGameFileName) throws IOException {
        SavedGameFile savedGameFile = new SavedGameFile(savedGameFileName); // throws

        _worldModel = new GameWorldModel();
        _worldController = new GameWorldController(this, (IGameWorldModel)_worldModel);
        _worldView = new GameWorldView((IGameWorldModel)_worldModel);

        _logicModel = new CampaignModel(savedGameFile.getNumPlayers());
        _logicController = new CampaignController(this, (ICampaignModel)_logicModel);
        _logicView = new CampaignView(_widgetManager, (ICampaignController)_logicController, (ICampaignModel)_logicModel);

        _engine.setDefaultViewport();
        _widgetManager.closeAll();

        String worldSceneFileName = makeCampaignWorldSceneFileName(savedGameFile.getEpisode(), savedGameFile.getMission());
        SceneBuilder worldSceneBuilder = new SceneBuilder(sceneBuilderObserver, _worldModel, _worldView, _worldController);
        worldSceneBuilder.build(worldSceneFileName);

        String logicSceneFileName = makeCampaignLogicSceneFileName(savedGameFile.getEpisode(), savedGameFile.getMission());
        SceneBuilder logicSceneBuilder = new SceneBuilder(sceneBuilderObserver, _logicModel, _logicView, _logicController);
        logicSceneBuilder.build(logicSceneFileName);
    }

    @Override
    public void startCampaignGame(ISceneBuilderObserver sceneBuilderObserver, int numPlayers) {
        _worldModel = new GameWorldModel();
        _worldController = new GameWorldController(this, (IGameWorldModel)_worldModel);
        _worldView = new GameWorldView((IGameWorldModel)_worldModel);

        _logicModel = new CampaignModel(numPlayers);
        _logicController = new CampaignController(this, (ICampaignModel)_logicModel);
        _logicView = new CampaignView(_widgetManager, (ICampaignController)_logicController, (ICampaignModel)_logicModel);

        _engine.setDefaultViewport();
        _widgetManager.closeAll();

        String worldSceneFileName = makeCampaignWorldSceneFileName(FIRST_EPISODE, FIRST_MISSION);
        SceneBuilder worldSceneBuilder = new SceneBuilder(sceneBuilderObserver, _worldModel, _worldView, _worldController);
        worldSceneBuilder.build(worldSceneFileName);

        String logicSceneFileName = makeCampaignLogicSceneFileName(FIRST_EPISODE, FIRST_MISSION);
        SceneBuilder logicSceneBuilder = new SceneBuilder(sceneBuilderObserver, _logicModel, _logicView, _logicController);
        logicSceneBuilder.build(logicSceneFileName);
    }

    @Override
    public void startRaceGame(ISceneBuilderObserver sceneBuilderObserver, int numPlayers) {
        _worldModel = new GameWorldModel();
        _worldController = new GameWorldController(this, (IGameWorldModel)_worldModel);
        _worldView = new GameWorldView((IGameWorldModel)_worldModel);

        _logicModel = new RaceModel(numPlayers);
        _logicController = new RaceController((IRaceModel)_logicModel);
        _logicView = new RaceView(_widgetManager, (IRaceController)_logicController, (IRaceModel)_logicModel);

        _engine.setDefaultViewport();
        _widgetManager.closeAll();

        String worldSceneFileName = getFirstWorldRaceSceneFileName();
        SceneBuilder worldSceneBuilder = new SceneBuilder(sceneBuilderObserver, _worldModel, _worldView, _worldController);
        worldSceneBuilder.build(worldSceneFileName);

        String logicSceneFileName = getFirstLogicRaceSceneFileName();
        SceneBuilder logicSceneBuilder = new SceneBuilder(sceneBuilderObserver, _logicModel, _logicView, _logicController);
        logicSceneBuilder.build(logicSceneFileName);
    }

    @Override
    public void startDogfightGame(ISceneBuilderObserver sceneBuilderObserver, int numPlayers) {
        _worldModel = new GameWorldModel();
        _worldController = new GameWorldController(this, (IGameWorldModel)_worldModel);
        _worldView = new GameWorldView((IGameWorldModel)_worldModel);

        _logicModel = new DogfightModel(numPlayers);
        _logicController = new DogfightController((IDogfightModel)_logicModel);
        _logicView = new DogfightView(_widgetManager, (IDogfightController)_logicController, (IDogfightModel)_logicModel);

        _engine.setDefaultViewport();
        _widgetManager.closeAll();

        String worldSceneFileName = getFirstWorldDogfightSceneFileName();
        SceneBuilder worldSceneBuilder = new SceneBuilder(sceneBuilderObserver, _worldModel, _worldView, _worldController);
        worldSceneBuilder.build(worldSceneFileName);

        String logicSceneFileName = getFirstLogicDogfightSceneFileName();
        SceneBuilder logicSceneBuilder = new SceneBuilder(sceneBuilderObserver, _logicModel, _logicView, _logicController);
        logicSceneBuilder.build(logicSceneFileName);
    }

    @Override
    public void temp() {
        // TODO
    }

    @Override
    public void onMenuWorldControllerEvent() {
        // TODO
    }

    public void run() {
        _engine.run();
    }

    public void freeResources() {
        _engine.freeResources();
    }

    public void changeStateNow(IState state) {
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
        windowConfig._resizeable = true;
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

    static private String makeCampaignWorldSceneFileName(int episode, int mission) {
        return String.format("scenes/CampaignWorldE%02dM%02d.json", episode, mission);
    }

    static private String makeCampaignLogicSceneFileName(int episode, int mission) {
        return String.format("scenes/CampaignLogicE%02dM%02d.json", episode, mission);
    }

    static private String getFirstWorldRaceSceneFileName() {
        return "scenes/RaceWorld00.json";
    }

    static private String getFirstLogicRaceSceneFileName() {
        return "scenes/RaceLogic00.json";
    }

    static private String getFirstWorldDogfightSceneFileName() {
        return "scenes/DogfightWorld00.json";
    }

    static private String getFirstLogicDogfightSceneFileName() {
        return "scenes/DogfightLogic00.json";
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
}
