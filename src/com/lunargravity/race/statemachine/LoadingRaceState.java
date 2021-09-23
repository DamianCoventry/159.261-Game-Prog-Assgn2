package com.lunargravity.race.statemachine;

import com.lunargravity.application.*;
import com.lunargravity.race.view.RaceBuilderObserver;

import java.io.IOException;

public class LoadingRaceState extends StateBase {
    private final int _numPlayers;
    public LoadingRaceState(IStateMachineContext context, int numPlayers) {
        super(context);
        _numPlayers = numPlayers;
    }

    @Override
    public void begin() throws IOException {
        RaceBuilderObserver raceBuilderObserver = new RaceBuilderObserver(getManualFrameUpdater());

        try {
            getContext().startRaceGame(raceBuilderObserver, _numPlayers);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        changeState(new GetReadyState(getContext()));

        raceBuilderObserver.freeResources();
    }
}
