package com.lunargravity.campaign.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.campaign.controller.ICampaignController;
import com.lunargravity.campaign.controller.ICampaignControllerObserver;
import com.lunargravity.campaign.view.ICampaignView;
import com.lunargravity.engine.timeouts.TimeoutManager;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class EpisodeOutroState extends StateBase implements ICampaignControllerObserver {
    private int _timeoutId;

    public EpisodeOutroState(IStateMachineContext context) {
        super(context);
        _timeoutId = 0;
    }

    @Override
    public void begin() throws IOException {
        getCampaignController().addObserver(this);
        getCampaignView().showEpisodeOutro();

        _timeoutId = addTimeout(30000, (callCount) -> {
            try {
                getCampaignController().completeEpisode();
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
        if (_timeoutId > 0) {
            removeTimeout(_timeoutId);
            _timeoutId = 0;
        }
    }

    @Override
    public void startNextEpisode() throws Exception {
        getContext().loadCampaignEpisode(new NullBuilderObserver());
        changeState(new EpisodeIntroState(getContext()));
    }

    @Override
    public void episodeIntroAborted() {
        // Nothing to do
    }

    @Override
    public void mouseButtonEvent(int button, int action, int mods) throws Exception {
        if (action == GLFW_PRESS) {
            getCampaignController().completeEpisode();
        }
    }

    @Override
    public void episodeOutroAborted() throws Exception {
        getCampaignController().completeEpisode();
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
        changeState(new GameWonState(getContext()));
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
}
