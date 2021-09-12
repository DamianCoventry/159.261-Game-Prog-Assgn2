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

import java.io.IOException;
import java.util.HashMap;

public class Application implements
        IFrameConsumer,
        IInputConsumer,
        IViewportSizeObserver,
        IStateMachineContext,
        IApplicationModes,
        IGameWorldControllerEvents,
        ICampaignControllerEvents,
        IRaceControllerEvents,
        IDogfightControllerEvents {

    static final private String WINDOW_TITLE = "Lunar Gravity v1.0";
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 960;
    private static final int ARROW_MOUSE_CURSOR = 1;
    private static final int GRAB_MOUSE_CURSOR = 2;
    private static final int HAND_MOUSE_CURSOR = 3;

    private final IEngine _engine;
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

    public Application() throws IOException {
        _engine = new Engine(this, this, this, createWindowConfig());
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
            _worldController.think();
        }
        if (_logicController != null) {
            _logicController.think();
        }
        if (_worldView != null) {
            _worldView.think();
        }
        if (_logicView != null) {
            _logicView.think();
        }
        _currentState.think();
    }

    @Override
    public void onFrameDraw3d(int viewport, Matrix4f projectionMatrix) {
        if (_worldView != null) {
            _worldView.draw3d(viewport, projectionMatrix);
        }
        if (_logicView != null) {
            _logicView.draw3d(viewport, projectionMatrix);
        }
        _currentState.draw3d(viewport, projectionMatrix);
    }

    @Override
    public void onFrameDraw2d(int viewport, Matrix4f projectionMatrix) {
        if (_worldView != null) {
            _worldView.draw2d(viewport, projectionMatrix);
        }
        if (_logicView != null) {
            _logicView.draw2d(viewport, projectionMatrix);
        }
        _currentState.draw2d(viewport, projectionMatrix);
    }

    @Override
    public void onKeyboardKeyEvent(int key, int scancode, int action, int mods) {
        _currentState.onKeyboardKeyEvent(key, scancode, action, mods);
    }

    @Override
    public void onMouseButtonEvent(int button, int action, int mods) {
        _currentState.onMouseButtonEvent(button, action, mods);
    }

    @Override
    public void onMouseCursorMovedEvent(double xPos, double yPos) {
        _currentState.onMouseCursorMovedEvent(xPos, yPos);
    }

    @Override
    public void onMouseWheelScrolledEvent(double xOffset, double yOffset) {
        _currentState.onMouseWheelScrolledEvent(xOffset, yOffset);
    }

    @Override
    public GlViewportConfig onViewportSizeChanged(int viewport, GlViewportConfig currentConfig, int windowWidth, int windowHeight) {
        return _currentState.onViewportSizeChanged(viewport, currentConfig, windowWidth, windowHeight);
    }

    @Override
    public void changeState(IState state) {
        _pendingState = state;
    }

    @Override
    public void changeStateNow(IState state) {
        if (_currentState != null) {
            _currentState.end();
        }
        _currentState = state;
        if (_currentState != null) {
            _currentState.begin();
        }
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
    public void startMenu(ISceneLoadObserver loadingProgress, IMenuWorldControllerEvents worldEventHandler, IMenuControllerEvents eventHandler) {
        _worldModel = new MenuWorldModel();
        _worldController = new MenuWorldController(worldEventHandler, (IMenuWorldModel)_worldModel);
        _worldView = new MenuWorldView((IMenuWorldModel)_worldModel);

        _logicModel = new MenuModel();
        _logicController = new MenuController(eventHandler, (IMenuModel)_logicModel);
        _logicView = new MenuView((IMenuModel)_logicModel);
    }

    @Override
    public void startCampaignGame(ISceneLoadObserver loadingProgress) {
        _worldModel = new GameWorldModel();
        _worldController = new GameWorldController(this, (IGameWorldModel)_worldModel);
        _worldView = new GameWorldView((IGameWorldModel)_worldModel);

        _logicModel = new CampaignModel();
        _logicController = new CampaignController(this, (ICampaignModel)_logicModel);
        _logicView = new CampaignView((ICampaignModel)_logicModel);
    }

    @Override
    public void startRaceGame(ISceneLoadObserver loadingProgress) {
        _worldModel = new GameWorldModel();
        _worldController = new GameWorldController(this, (IGameWorldModel)_worldModel);
        _worldView = new GameWorldView((IGameWorldModel)_worldModel);

        _logicModel = new RaceModel();
        _logicController = new RaceController(this, (IRaceModel)_logicModel);
        _logicView = new RaceView((IRaceModel)_logicModel);
    }

    @Override
    public void startDogfightGame(ISceneLoadObserver loadingProgress) {
        _worldModel = new GameWorldModel();
        _worldController = new GameWorldController(this, (IGameWorldModel)_worldModel);
        _worldView = new GameWorldView((IGameWorldModel)_worldModel);

        _logicModel = new DogfightModel();
        _logicController = new DogfightController(this, (IDogfightModel)_logicModel);
        _logicView = new DogfightView((IDogfightModel)_logicModel);
    }

    @Override
    public void temp() {
        // TODO: forward this to the current state
    }

    public void run() {
        _engine.run();
    }

    public void freeResources() {
        _engine.freeResources();
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
