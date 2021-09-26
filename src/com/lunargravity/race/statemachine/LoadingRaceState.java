package com.lunargravity.race.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.race.view.RaceBuilderObserver;

import java.io.IOException;

public class LoadingRaceState extends StateBase {
    private final int _numPlayers;
    public LoadingRaceState(IStateMachineContext context, int numPlayers) {
        super(context);
        _numPlayers = numPlayers;
    }

    @Override
    public void begin() throws IOException, InterruptedException {
        RaceBuilderObserver raceBuilderObserver = new RaceBuilderObserver(getContext().getEngine(), getManualFrameUpdater());

        getContext().startRaceGame(raceBuilderObserver, _numPlayers);

        changeState(new GetReadyState(getContext()));

        raceBuilderObserver.freeResources();
    }
}
