package com.lunargravity.campaign.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;

import java.io.IOException;

public class LoadingCampaignState extends StateBase {
    private final int _numPlayers;
    private final String _fileName;

    public LoadingCampaignState(IStateMachineContext context, int numPlayers) {
        super(context);
        _numPlayers = numPlayers;
        _fileName = null;
    }

    public LoadingCampaignState(IStateMachineContext context, String fileName) {
        super(context);
        _numPlayers = 0; // this value will be loaded from the saved game file
        _fileName = fileName;
    }

    @Override
    public void begin() throws Exception {
        if (_fileName != null) {
            getContext().createCampaignMvc(_fileName);
        } else {
            getContext().createCampaignMvc(_numPlayers);
        }

        getContext().loadCampaignEpisode(new NullBuilderObserver());
    }

    @Override
    public void think() {
        changeState(new EpisodeIntroState(getContext()));
    }
}
