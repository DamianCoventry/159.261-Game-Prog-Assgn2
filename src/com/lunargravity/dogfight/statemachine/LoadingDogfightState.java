//
// Lunar Gravity
//
// This game is based upon the Amiga video game Gravity Force that was
// released in 1989 by Stephan Wenzler
//
// https://www.mobygames.com/game/gravity-force
// https://www.youtube.com/watch?v=m9mFtCvnko8
//
// This implementation is Copyright (c) 2021, Damian Coventry
// All rights reserved
// Written for Massey University course 159.261 Game Programming (Assignment 2)
//

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
    public void begin() throws Exception {
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
