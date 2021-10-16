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

import com.lunargravity.campaign.controller.ICampaignController;
import com.lunargravity.campaign.statemachine.EpisodeOutroState;
import com.lunargravity.campaign.statemachine.MissionCompletedState;
import com.lunargravity.campaign.statemachine.MissionIntroState;
import com.lunargravity.campaign.view.MissionBuilderObserver;
import com.lunargravity.engine.core.IManualFrameUpdater;
import com.lunargravity.engine.graphics.Renderer;
import com.lunargravity.engine.graphics.ViewportConfig;
import com.lunargravity.engine.timeouts.TimeoutManager;
import org.joml.Matrix4f;

import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_9;

public class StateBase implements IState {
    private final IStateMachineContext _context;

    public StateBase(IStateMachineContext context) {
        _context = context;
    }

    protected IStateMachineContext getContext() {
        return _context;
    }

    protected void changeState(IState state) {
        _context.changeState(state);
    }

    protected int addTimeout(long timeoutMs, Function<Integer, TimeoutManager.CallbackResult> callback) {
        return _context.getEngine().getTimeoutManager().addTimeout(timeoutMs, callback);
    }

    protected void removeTimeout(int timeoutId) {
        _context.getEngine().getTimeoutManager().removeTimeout(timeoutId);
    }

    @Override
    public void begin() throws Exception {
        // TODO
    }

    @Override
    public void end() {
        // TODO
    }

    @Override
    public void think() {
        // TODO
    }

    @Override
    public void draw3d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void draw2d(Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public IManualFrameUpdater getManualFrameUpdater() {
        return _context.getEngine();
    }

    @Override
    public Renderer getRenderer() {
        return _context.getEngine().getRenderer();
    }

    @Override
    public ViewportConfig onViewportSizeChanged(int viewport, ViewportConfig viewportConfig, int windowWidth, int windowHeight) {
        //  1. if we're in the menu then
        //       there's one viewport and it occupies the entire window always
        //  2. else if it's a single player game then
        //       there's one viewport and it occupies the entire window always
        //  3. else if it's a two players game then
        //       if the current state is 'get ready', running, 'player died' or completed then
        //           there's two viewports that split the window vertically
        //       else
        //           there's one viewport and it occupies the entire window
        // TODO
        ViewportConfig vpc = new ViewportConfig();
        vpc._viewportIndex = 0; // TODO: this is temporary
        vpc._positionX = 0;
        vpc._positionY = 0;
        vpc._width = windowWidth;
        vpc._height = windowHeight;
        vpc._verticalFovDegrees = viewportConfig._verticalFovDegrees;
        vpc._perspectiveNcp = viewportConfig._perspectiveNcp;
        vpc._perspectiveFcp = viewportConfig._perspectiveFcp;
        return vpc;
    }

    @Override
    public void keyboardKeyEvent(int key, int scancode, int action, int mods) throws Exception {
        checkCheatKeys(key, action);
    }

    @Override
    public void mouseButtonEvent(int button, int action, int mods) throws Exception {
        // TODO
    }

    @Override
    public void mouseCursorMovedEvent(double xPos, double yPos) {
        // TODO
    }

    @Override
    public void mouseWheelScrolledEvent(double xOffset, double yOffset) {
        // TODO
    }

    private void checkCheatKeys(int key, int action) throws Exception {
        if (action != GLFW_PRESS) {
            return;
        }
        if (getContext().getLogicController() instanceof ICampaignController controller) {
            if (key == GLFW_KEY_1) {
                controller.skipToEpisode(0);
            } else if (key == GLFW_KEY_2) {
                controller.skipToEpisode(1);
            } else if (key == GLFW_KEY_3) {
                controller.skipToEpisode(2);
            } else if (key == GLFW_KEY_4) {
                controller.skipToEpisode(3);
            } else if (key == GLFW_KEY_7) {
                controller.skipToMission(0);
            } else if (key == GLFW_KEY_8) {
                controller.skipToMission(1);
            } else if (key == GLFW_KEY_9) {
                controller.skipToMission(2);
            } else if (key == GLFW_KEY_F5) {
                changeState(new MissionCompletedState(getContext()));
            } else if (key == GLFW_KEY_F6) {
                changeState(new EpisodeOutroState(getContext()));
            }
        }
    }
}
