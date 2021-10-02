package com.lunargravity.race.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.race.view.RaceBuilderObserver;

import java.io.IOException;

public class LoadingRaceState extends StateBase {
    private final int _numPlayers;
    private final Mode _mode;

    public enum Mode { NEW_GAME, NEXT_LEVEL }
    public LoadingRaceState(IStateMachineContext context, int numPlayers, Mode mode) {
        super(context);
        _numPlayers = numPlayers;
        _mode = mode;
    }

    @Override
    public void begin() throws Exception {
        RaceBuilderObserver raceBuilderObserver = new RaceBuilderObserver(getContext().getEngine(), getManualFrameUpdater());

        if (_mode == Mode.NEW_GAME) {
            getContext().startRaceGame(raceBuilderObserver, _numPlayers);
        }
        else {
            getContext().loadRaceLevel(raceBuilderObserver, _numPlayers);
        }

        raceBuilderObserver.freeResources();
    }

    @Override
    public void think() {
        changeState(new GetReadyState(getContext()));
    }
}
