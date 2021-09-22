package com.lunargravity.race.statemachine;

import com.lunargravity.application.*;
import com.lunargravity.race.view.RaceBuilderObserver;

public class LoadingRaceState extends StateBase {
    private final int _numPlayers;
    public LoadingRaceState(IStateMachineContext context, int numPlayers) {
        super(context);
        _numPlayers = numPlayers;
    }

    @Override
    public void begin() {
        RaceBuilderObserver raceBuilderObserver = new RaceBuilderObserver(getManualFrameUpdater());

        getContext().startRaceGame(raceBuilderObserver, _numPlayers);

        changeState(new GetReadyState(getContext()));
    }
}
