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
import com.lunargravity.dogfight.controller.IDogfightController;
import com.lunargravity.dogfight.controller.IDogfightControllerObserver;
import com.lunargravity.dogfight.view.IDogfightView;
import com.lunargravity.menu.statemachine.LoadingMenuState;

public class DogfightPausedState extends StateBase implements IDogfightControllerObserver {
    public DogfightPausedState(IStateMachineContext context) {
        super(context);
    }

    private IDogfightView getDogfightView() {
        return (IDogfightView)getContext().getLogicView();
    }

    private IDogfightController getDogfightController() {
        return (IDogfightController)getContext().getLogicController();
    }

    @Override
    public void begin() {
        getDogfightController().addObserver(this);
        getDogfightView().showPausedWidget();
    }

    @Override
    public void end() {
        getDogfightController().removeObserver(this);
    }

    @Override
    public void startNextDogfightRequested(int numPlayers) {
        // Nothing to do
    }

    @Override
    public void resumeDogfightRequested() {
        changeState(new GetReadyState(getContext()));
    }

    @Override
    public void mainMenuRequested() {
        changeState(new LoadingMenuState(getContext()));
    }
}
