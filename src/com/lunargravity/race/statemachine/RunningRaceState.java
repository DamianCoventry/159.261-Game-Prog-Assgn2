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

package com.lunargravity.race.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.LargeNumberFont;
import com.lunargravity.application.StateBase;
import com.lunargravity.race.controller.IRaceController;
import com.lunargravity.race.model.IRaceModel;
import com.lunargravity.race.view.IRaceView;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;

public class RunningRaceState extends StateBase {
    private static final Vector4f WHITE = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    private LargeNumberFont _font;

    public RunningRaceState(IStateMachineContext context) {
        super(context);
    }
    
    @Override
    public void begin() throws IOException {
        _font = new LargeNumberFont(getRenderer()); // temp
        getRaceView().showLevelStatusBar();
    }

    @Override
    public void end() {
        _font.freeResources(); // temp
    }

    @Override
    public void keyboardKeyEvent(int key, int scancode, int action, int mods) {
        if (action != GLFW_PRESS) {
            return;
        }
        switch (key) {
            // Detecting this key press is temporary
            case GLFW_KEY_1 -> getRaceController().killPlayer(1);
            // Detecting this key press is temporary
            case GLFW_KEY_2 -> getRaceController().killPlayer(2);
            // Detecting this key press is temporary
            case GLFW_KEY_3 -> changeState(new RaceCompletedState(getContext()));
            case GLFW_KEY_ESCAPE -> changeState(new RacePausedState(getContext()));
        }
    }

    @Override
    public void draw2d(Matrix4f projectionMatrix) { // temp
        // temp
        _font.drawNumber(projectionMatrix, getRaceModel().getNumPlayers(), 10.0f, 74.0f, 1.0f, WHITE);
        _font.drawNumber(projectionMatrix, getRaceModel().getLevel(), 10.0f, 10.0f, 1.0f, WHITE);
    }

    private IRaceView getRaceView() {
        return (IRaceView)getContext().getLogicView();
    }

    // temp
    private IRaceController getRaceController() {
        return (IRaceController)getContext().getLogicController();
    }

    // temp
    private IRaceModel getRaceModel() {
        return (IRaceModel)getContext().getLogicModel();
    }
}
