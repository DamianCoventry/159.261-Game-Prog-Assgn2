package com.lunargravity.campaign.statemachine;

import com.lunargravity.application.*;
import com.lunargravity.engine.timeouts.TimeoutManager;

import java.io.IOException;

public class EpisodeIntroState extends StateBase {
    private final int _numPlayers;
    private final String _fileName;
    private int _timeoutId;

    public EpisodeIntroState(IStateMachineContext context, int numPlayers) {
        super(context);
        _numPlayers = numPlayers;
        _fileName = null;
        _timeoutId = 0;
    }
    public EpisodeIntroState(IStateMachineContext context, String fileName) {
        super(context);
        _numPlayers = 0; // this value will be loaded from the file
        _fileName = fileName;
        _timeoutId = 0;
    }

    @Override
    public void begin() {
        // TODO: load a few widgets into the widget system to represent the episode intro

        _timeoutId = addTimeout(3000, (callCount) -> {
            MissionBuilderObserver missionBuilderObserver = new MissionBuilderObserver(getManualFrameUpdater());

            try {
                if (_fileName != null) {
                    getContext().startCampaignGame(missionBuilderObserver, _fileName);
                } else {
                    getContext().startCampaignGame(missionBuilderObserver, _numPlayers);
                }

                changeState(new MissionIntroState(getContext()));
            }
            catch (IOException e) {
                // TODO: Is there something useful we can do with the exception?
            }

            _timeoutId = 0;
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }

    @Override
    public void end() {
        if (_timeoutId > 0) {
            removeTimeout(_timeoutId);
            _timeoutId = 0;
        }
    }
}
