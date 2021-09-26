package com.lunargravity.dogfight.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.dogfight.view.DogfightBuilderObserver;

import java.io.IOException;

public class LoadingDogfightState extends StateBase {
    private final int _numPlayers;
    public LoadingDogfightState(IStateMachineContext context, int numPlayers) {
        super(context);
        _numPlayers = numPlayers;
    }

    @Override
    public void begin() throws IOException, InterruptedException {
        DogfightBuilderObserver dogfightBuilderObserver = new DogfightBuilderObserver(getContext().getEngine(), getManualFrameUpdater());

        getContext().startDogfightGame(dogfightBuilderObserver, _numPlayers);

        changeState(new GetReadyState(getContext()));

        dogfightBuilderObserver.freeResources();
    }
}
