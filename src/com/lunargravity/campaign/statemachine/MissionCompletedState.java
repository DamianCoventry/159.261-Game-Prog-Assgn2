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
import com.lunargravity.application.StateBase;
import com.lunargravity.campaign.controller.ICampaignController;
import com.lunargravity.campaign.controller.ICampaignControllerObserver;
import com.lunargravity.campaign.view.ICampaignView;
import com.lunargravity.campaign.view.MissionBuilderObserver;
import com.lunargravity.engine.timeouts.TimeoutManager;

import java.io.IOException;

public class MissionCompletedState extends StateBase implements ICampaignControllerObserver {
    private int _timeoutId;

    public MissionCompletedState(IStateMachineContext context) {
        super(context);
        _timeoutId = 0;
    }

    @Override
    public void begin() throws IOException {
        getCampaignController().addObserver(this);
        getCampaignView().showMissionCompleted();

        _timeoutId = addTimeout(3000, (callCount) -> {
            try {
                getCampaignController().completeMission();
            } catch (Exception e) {
                e.printStackTrace();
            }
            _timeoutId = 0;
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }

    @Override
    public void end() {
        getCampaignController().removeObserver(this);
        if (_timeoutId != 0) {
            removeTimeout(_timeoutId);
            _timeoutId = 0;
        }
    }

    private ICampaignView getCampaignView() {
        return (ICampaignView)getContext().getLogicView();
    }

    private ICampaignController getCampaignController() {
        return (ICampaignController)getContext().getLogicController();
    }

    @Override
    public void startNextEpisode() throws IOException, InterruptedException {
        // Nothing to do
    }

    @Override
    public void gameOver() {
        // Nothing to do
    }

    @Override
    public void gameCompleted() {
        // Nothing to do
    }

    @Override
    public void episodeIntroAborted() {
        // Nothing to do
    }

    @Override
    public void missionIntroAborted() {
        // Nothing to do
    }

    @Override
    public void episodeOutroAborted() throws IOException, InterruptedException {
        // Nothing to do
    }

    @Override
    public void episodeCompleted() {
        changeState(new EpisodeOutroState(getContext()));
    }

    @Override
    public void startNextMission() throws Exception {
        MissionBuilderObserver missionBuilderObserver = new MissionBuilderObserver(getContext().getEngine(), getManualFrameUpdater());

        getContext().loadCampaignMission(missionBuilderObserver);

        changeState(new MissionIntroState(getContext()));

        missionBuilderObserver.freeResources();
    }

    @Override
    public void gameOverAborted() {
        // Nothing to do
    }

    @Override
    public void gameCompletedAborted() {
        // Nothing to do
    }

    @Override
    public void resumeMission() {
        // Nothing to do
    }

    @Override
    public void missionCompleted() {
        // Nothing to do
    }

    @Override
    public void playerDied(ICampaignView.WhichPlayer whichPlayer) {
        // Nothing to do
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
