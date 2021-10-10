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
import com.lunargravity.application.PlayerInputBindings;
import com.lunargravity.application.StateBase;
import com.lunargravity.campaign.controller.ICampaignController;
import com.lunargravity.campaign.controller.ICampaignControllerObserver;
import com.lunargravity.campaign.model.ICampaignModel;
import com.lunargravity.campaign.view.ICampaignView;
import com.lunargravity.campaign.view.MissionBuilderObserver;
import com.lunargravity.world.controller.IGameWorldController;
import com.lunargravity.world.model.IGameWorldModel;
import com.lunargravity.world.view.IGameWorldView;

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
        for (int i = 0; i < getGameWorldModel().getNumPlayers(); ++i) {
            getGameWorldModel().getPlayerState(i).resetInputStates();
        }
        getCampaignController().removeObserver(this);
    }

    @Override
    public void keyboardKeyEvent(int key, int scancode, int action, int mods) throws Exception {
        PlayerInputBindings inputBindings = getGameWorldModel().getPlayerInputBindings();

        checkCheatKeys(key, action);

        if (action == GLFW_PRESS) {
            if (key == GLFW_KEY_ESCAPE) {
                changeState(new MissionPausedState(getContext()));
            }

            if (inputBindings.getPlayerThrustKey(0) == key) {
                getGameWorldController().playerStartThrust(0);
            }
            if (inputBindings.getPlayerRotateRightKey(0) == key) {
                getGameWorldController().playerStartRotateCw(0);
            }
            if (inputBindings.getPlayerRotateLeftKey(0) == key) {
                getGameWorldController().playerStartRotateCcw(0);
            }
            if (inputBindings.getPlayerShootKey(0) == key) {
                getGameWorldController().playerStartShoot(0);
            }
            if (inputBindings.getPlayerKickKey(0) == key) {
                getGameWorldController().playerKick(0);
            }
            
            if (getGameWorldModel().getNumPlayers() == 2) {
                if (inputBindings.getPlayerThrustKey(1) == key) {
                    getGameWorldController().playerStartThrust(1);
                }
                if (inputBindings.getPlayerRotateRightKey(1) == key) {
                    getGameWorldController().playerStartRotateCw(1);
                }
                if (inputBindings.getPlayerRotateLeftKey(1) == key) {
                    getGameWorldController().playerStartRotateCcw(1);
                }
                if ( inputBindings.getPlayerShootKey(1) ==key){
                    getGameWorldController().playerStartShoot(1);
                }
                if (inputBindings.getPlayerKickKey(1) == key) {
                    getGameWorldController().playerKick(1);
                }
            }
        }
        else if (action == GLFW_RELEASE) {
            if (inputBindings.getPlayerThrustKey(0) == key) {
                getGameWorldController().playerStopThrust(0);
            }
            if (inputBindings.getPlayerRotateRightKey(0) == key) {
                getGameWorldController().playerStopRotateCw(0);
            }
            if (inputBindings.getPlayerRotateLeftKey(0) == key) {
                getGameWorldController().playerStopRotateCcw(0);
            }
            if (inputBindings.getPlayerShootKey(0) == key) {
                getGameWorldController().playerStopShoot(0);
            }
            if (inputBindings.getPlayerKickKey(0) == key) {
                getGameWorldController().playerKick(0);
            }

            if (getGameWorldModel().getNumPlayers() == 2) {
                if (inputBindings.getPlayerThrustKey(1) == key) {
                    getGameWorldController().playerStopThrust(1);
                }
                if (inputBindings.getPlayerRotateRightKey(1) == key) {
                    getGameWorldController().playerStopRotateCw(1);
                }
                if (inputBindings.getPlayerRotateLeftKey(1) == key) {
                    getGameWorldController().playerStopRotateCcw(1);
                }
                if ( inputBindings.getPlayerShootKey(1) ==key){
                    getGameWorldController().playerStopShoot(1);
                }
                if (inputBindings.getPlayerKickKey(1) == key) {
                    getGameWorldController().playerKick(1);
                }
            }
        }
    }

    private void checkCheatKeys(int key, int action) throws Exception {
        if (action == GLFW_PRESS) {
            if (key == GLFW_KEY_1) {
                getCampaignController().skipToEpisode(0);
            } else if (key == GLFW_KEY_2) {
                getCampaignController().skipToEpisode(1);
            } else if (key == GLFW_KEY_3) {
                getCampaignController().skipToEpisode(2);
            } else if (key == GLFW_KEY_4) {
                getCampaignController().skipToEpisode(3);
            } else if (key == GLFW_KEY_7) {
                getCampaignController().skipToMission(0);
            } else if (key == GLFW_KEY_8) {
                getCampaignController().skipToMission(1);
            } else if (key == GLFW_KEY_9) {
                getCampaignController().skipToMission(2);
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
        // Only here for the cheat keys
        getContext().loadCampaignEpisode(new NullBuilderObserver());
        changeState(new EpisodeIntroState(getContext()));
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
        // Only here for the cheat keys
        MissionBuilderObserver missionBuilderObserver = new MissionBuilderObserver(getContext().getEngine(), getManualFrameUpdater());

        getContext().loadCampaignMission(missionBuilderObserver);

        changeState(new MissionIntroState(getContext()));

        missionBuilderObserver.freeNativeResources();
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
