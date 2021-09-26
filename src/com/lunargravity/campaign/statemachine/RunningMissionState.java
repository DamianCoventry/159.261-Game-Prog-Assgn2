package com.lunargravity.campaign.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.campaign.view.ICampaignView;

import static org.lwjgl.glfw.GLFW.*;

public class RunningMissionState extends StateBase {
    public RunningMissionState(IStateMachineContext context) {
        super(context);
    }

    @Override
    public void begin() {

    }

    @Override
    public void end() {

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
            case GLFW_KEY_ESCAPE -> changeState(new MissionPausedState(getContext()));
        }
    }
}
