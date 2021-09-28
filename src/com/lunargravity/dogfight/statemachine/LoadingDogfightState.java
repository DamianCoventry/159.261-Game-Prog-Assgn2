package com.lunargravity.dogfight.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.dogfight.view.DogfightBuilderObserver;

import java.io.IOException;

public class LoadingDogfightState extends StateBase {
    private final int _numPlayers;
    private final Mode _mode;

    public enum Mode { NEW_GAME, NEXT_LEVEL }
    public LoadingDogfightState(IStateMachineContext context, int numPlayers, Mode mode) {
        super(context);
        _numPlayers = numPlayers;
        _mode = mode;
    }

    @Override
    public void begin() throws IOException, InterruptedException {
        DogfightBuilderObserver dogfightBuilderObserver = new DogfightBuilderObserver(getContext().getEngine(), getManualFrameUpdater());

        if (_mode == LoadingDogfightState.Mode.NEW_GAME) {
            getContext().startDogfightGame(dogfightBuilderObserver, _numPlayers);
        }
        else {
            getContext().loadDogfightLevel(dogfightBuilderObserver, _numPlayers);
        }

        dogfightBuilderObserver.freeResources();
    }

    @Override
    public void think() {
        changeState(new GetReadyState(getContext()));
    }
}
