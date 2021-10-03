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
import com.lunargravity.campaign.controller.ICampaignController;
import com.lunargravity.campaign.controller.ICampaignControllerObserver;
import com.lunargravity.campaign.model.ICampaignModel;
import com.lunargravity.campaign.view.ICampaignView;
import com.lunargravity.world.controller.IGameWorldController;
import com.lunargravity.world.model.IGameWorldModel;
import com.lunargravity.world.view.IGameWorldView;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;

public class RunningMissionState extends StateBase implements ICampaignControllerObserver {

    public RunningMissionState(IStateMachineContext context) {
        super(context);
    }

    @Override
    public void begin() throws IOException {
        getCampaignController().addObserver(this);
        getGameWorldView().showMissionStatusBar();
    }

    @Override
    public void end() {
        getGameWorldView().hideMissionStatusBar();
        getCampaignController().removeObserver(this);
    }

    @Override
    public void keyboardKeyEvent(int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
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

                // TODO: Use key bindings
                case GLFW_KEY_UP -> getGameWorldController().playerStartThrust(0);
                case GLFW_KEY_LEFT -> getGameWorldController().playerStartRotateCcw(0);
                case GLFW_KEY_RIGHT -> getGameWorldController().playerStartRotateCw(0);
                case GLFW_KEY_RIGHT_CONTROL -> getGameWorldController().playerStartShoot(0);
                case GLFW_KEY_RIGHT_SHIFT -> getGameWorldController().playerKick(0);

                case GLFW_KEY_W -> getGameWorldController().playerStartThrust(1);
                case GLFW_KEY_A -> getGameWorldController().playerStartRotateCcw(1);
                case GLFW_KEY_D -> getGameWorldController().playerStartRotateCw(1);
                case GLFW_KEY_SPACE -> getGameWorldController().playerStartShoot(1);
                case GLFW_KEY_K -> getGameWorldController().playerKick(1);
            }
        }
        else if (action == GLFW_RELEASE) {
            switch (key) {
                // TODO: Use key bindings
                case GLFW_KEY_UP -> getGameWorldController().playerStopThrust(0);
                case GLFW_KEY_LEFT -> getGameWorldController().playerStopRotateCcw(0);
                case GLFW_KEY_RIGHT -> getGameWorldController().playerStopRotateCw(0);
                case GLFW_KEY_RIGHT_CONTROL -> getGameWorldController().playerStopShoot(0);

                case GLFW_KEY_W -> getGameWorldController().playerStopThrust(1);
                case GLFW_KEY_A -> getGameWorldController().playerStopRotateCcw(1);
                case GLFW_KEY_D -> getGameWorldController().playerStopRotateCw(1);
                case GLFW_KEY_SPACE -> getGameWorldController().playerStopShoot(1);
            }
        }
    }

    // temp
    private ICampaignModel getCampaignModel() {
        return (ICampaignModel)getContext().getLogicModel();
    }

    private IGameWorldView getGameWorldView() {
        return (IGameWorldView)getContext().getWorldView();
    }

    private ICampaignController getCampaignController() {
        return (ICampaignController)getContext().getLogicController();
    }

    private IGameWorldModel getGameWorldModel() {
        return (IGameWorldModel)getContext().getWorldModel();
    }

    private IGameWorldController getGameWorldController() {
        return (IGameWorldController)getContext().getWorldController();
    }

    @Override
    public void gameOver() {
        // Nothing to do
    }

    @Override
    public void gameOverAborted() {
        // Nothing to do
    }

    @Override
    public void gameCompleted() {
        // Nothing to do
    }

    @Override
    public void gameCompletedAborted() {
        // Nothing to do
    }

    @Override
    public void startNextEpisode() throws Exception {
        // Nothing to do
    }

    @Override
    public void episodeIntroAborted() throws Exception {
        // Nothing to do
    }

    @Override
    public void missionIntroAborted() {
        // Nothing to do
    }

    @Override
    public void episodeOutroAborted() throws Exception {
        // Nothing to do
    }

    @Override
    public void episodeCompleted() {
        // Nothing to do
    }

    @Override
    public void startNextMission() throws Exception {
        // Nothing to do
    }

    @Override
    public void resumeMission() {
        // Nothing to do
    }

    @Override
    public void missionCompleted() {
        changeState(new MissionCompletedState(getContext()));
    }

    @Override
    public void playerDied(ICampaignView.WhichPlayer whichPlayer) {
        changeState(new PlayerDiedState(getContext(), whichPlayer));
    }

    @Override
    public void playerShipSpawned(ICampaignView.WhichPlayer whichPlayer) {
        // Nothing to do
    }

    @Override
    public void quitCampaign() {
        // Nothing to do
    }
}
