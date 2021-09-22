package com.lunargravity.dogfight.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.dogfight.view.DogfightBuilderObserver;

public class LoadingDogfightState extends StateBase {
    private final int _numPlayers;
    public LoadingDogfightState(IStateMachineContext context, int numPlayers) {
        super(context);
        _numPlayers = numPlayers;
    }

    @Override
    public void begin() {
        DogfightBuilderObserver raceBuilderObserver = new DogfightBuilderObserver(getManualFrameUpdater());

        getContext().startDogfightGame(raceBuilderObserver, _numPlayers);

        changeState(new GetReadyState(getContext()));
    }
}
