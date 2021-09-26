package com.lunargravity.campaign.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.campaign.controller.ICampaignController;
import com.lunargravity.campaign.controller.ICampaignControllerObserver;
import com.lunargravity.campaign.view.ICampaignView;
import com.lunargravity.engine.timeouts.TimeoutManager;
import com.lunargravity.menu.statemachine.LoadingMenuState;
import com.lunargravity.race.statemachine.GetReadyState;

public class MissionPausedState extends StateBase implements ICampaignControllerObserver {
    private int _timeoutId;

    public MissionPausedState(IStateMachineContext context) {
        super(context);
        _timeoutId = 0;
    }

    @Override
    public void begin() {
        getCampaignController().addObserver(this);
        getCampaignView().showMissionPaused();

        _timeoutId = addTimeout(3000, (callCount) -> {
            changeState(new LoadingMenuState(getContext()));
            _timeoutId = 0;
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
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
    public void episodeIntroAborted() {
        // Nothing to do
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
        changeState(new GetReadyState(getContext()));
    }

    @Override
    public void playerShipSpawned() {
        // Nothing to do
    }

    @Override
    public void quitCampaign() {
        changeState(new LoadingMenuState(getContext()));
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
}
