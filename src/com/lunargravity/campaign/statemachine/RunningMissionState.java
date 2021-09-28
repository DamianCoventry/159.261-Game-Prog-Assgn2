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

package com.lunargravity.campaign.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.LargeNumberFont;
import com.lunargravity.application.StateBase;
import com.lunargravity.campaign.model.ICampaignModel;
import com.lunargravity.campaign.view.ICampaignView;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;

public class RunningMissionState extends StateBase {
    private static final Vector4f WHITE = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    private LargeNumberFont _font;

    public RunningMissionState(IStateMachineContext context) {
        super(context);
    }

    @Override
    public void begin() throws IOException {
        _font = new LargeNumberFont(getRenderer()); // temp
        getCampaignView().showMissionStatusBar();
    }

    @Override
    public void end() {
        _font.freeResources(); // temp
    }

    @Override
    public void draw2d(int viewport, Matrix4f projectionMatrix) { // temp
        // temp
        _font.drawNumber(projectionMatrix, getCampaignModel().getNumPlayers(), 10.0f, 138.0f, 1.0f, WHITE);
        _font.drawNumber(projectionMatrix, getCampaignModel().getEpisode(), 10.0f, 74.0f, 1.0f, WHITE);
        _font.drawNumber(projectionMatrix, getCampaignModel().getMission(), 10.0f, 10.0f, 1.0f, WHITE);
    }

    @Override
    public void keyboardKeyEvent(int key, int scancode, int action, int mods) {
        if (action != GLFW_PRESS) {
            return;
        }
        switch (key) {
            // Detecting this key press is temporary
            case GLFW_KEY_1 -> changeState(new PlayerDiedState(getContext(), ICampaignView.WhichPlayer.PLAYER_1));
            // Detecting this key press is temporary
            case GLFW_KEY_2 -> changeState(new MissionCompletedState(getContext()));
            // Detecting this key press is temporary
            case GLFW_KEY_3 -> changeState(new GameWonState(getContext()));
            // Detecting this key press is temporary
            case GLFW_KEY_4 -> changeState(new GameOverState(getContext()));
            case GLFW_KEY_ESCAPE -> changeState(new MissionPausedState(getContext()));
        }
    }

    private ICampaignView getCampaignView() {
        return (ICampaignView)getContext().getLogicView();
    }

    // temp
    private ICampaignModel getCampaignModel() {
        return (ICampaignModel)getContext().getLogicModel();
    }
}
