package com.lunargravity.campaign.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.campaign.controller.ICampaignController;
import com.lunargravity.campaign.controller.ICampaignControllerObserver;
import com.lunargravity.campaign.view.ICampaignView;
import com.lunargravity.campaign.view.MissionBuilderObserver;

import java.io.IOException;

public class EpisodeIntroState extends StateBase implements ICampaignControllerObserver {
    private int _timeoutId;

    public EpisodeIntroState(IStateMachineContext context) {
        super(context);
        _timeoutId = 0;
    }

    @Override
    public void begin() throws IOException {
        getCampaignController().addObserver(this);
        getCampaignView().showEpisodeIntro();

//        _timeoutId = addTimeout(3000, (callCount) -> {
//            try {
//                loadCampaignMission();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            _timeoutId = 0;
//            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
//        });
    }

    @Override
    public void end() {
        getCampaignController().removeObserver(this);
        if (_timeoutId > 0) {
            removeTimeout(_timeoutId);
            _timeoutId = 0;
        }
    }

    @Override
    public void startNextEpisode() {
        // Nothing to do
    }

    @Override
    public void mouseButtonEvent(int button, int action, int mods) throws Exception {
        // temp
        loadCampaignMission();
    }

    @Override
    public void episodeIntroAborted() throws Exception {
        loadCampaignMission();
    }

    @Override
    public void episodeOutroAborted() {
        // Nothing to do
    }

    @Override
    public void episodeCompleted() {
        // Nothing to do
    }

    @Override
    public void startNextMission() {
        // Nothing to do
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

    @Override
    public void missionIntroAborted() {
        // Nothing to do
    }

    private ICampaignView getCampaignView() {
        return (ICampaignView)getContext().getLogicView();
    }

    private ICampaignController getCampaignController() {
        return (ICampaignController)getContext().getLogicController();
    }

    private void loadCampaignMission() throws Exception {
        MissionBuilderObserver missionBuilderObserver = new MissionBuilderObserver(getContext().getEngine(), getManualFrameUpdater());

        getContext().loadCampaignMission(missionBuilderObserver);

        changeState(new MissionIntroState(getContext()));

        missionBuilderObserver.freeNativeResources();
    }
}
