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
import com.lunargravity.dogfight.view.IDogfightView;
import com.lunargravity.engine.timeouts.TimeoutManager;

import java.io.IOException;

public class DogfightCompletedState extends StateBase {
    public DogfightCompletedState(IStateMachineContext context) {
        super(context);
    }

    @Override
    public void begin() throws IOException {
        getDogfightView().showCompletedWidget();

        addTimeout(3500, (callCount) -> {
            changeState(new DogfightResultsState(getContext()));
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }

    private IDogfightView getDogfightView() {
        return (IDogfightView)getContext().getLogicView();
    }
}
