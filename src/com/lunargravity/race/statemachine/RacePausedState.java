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

package com.lunargravity.race.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.menu.statemachine.LoadingMenuState;
import com.lunargravity.race.controller.IRaceController;
import com.lunargravity.race.controller.IRaceControllerObserver;
import com.lunargravity.race.view.IRaceView;

import java.io.IOException;

public class RacePausedState extends StateBase implements IRaceControllerObserver {
    public RacePausedState(IStateMachineContext context) {
        super(context);
    }

    private IRaceView getRaceView() {
        return (IRaceView)getContext().getLogicView();
    }

    private IRaceController getRaceController() {
        return (IRaceController)getContext().getLogicController();
    }

    @Override
    public void begin() throws IOException {
        getRaceController().addObserver(this);
        getRaceView().showPausedWidget();
    }

    @Override
    public void end() {
        getRaceController().removeObserver(this);
    }

    @Override
    public void startNextRaceRequested(int numPlayers) {
        // Nothing to do
    }

    @Override
    public void resumeRaceRequested() {
        changeState(new GetReadyState(getContext()));
    }

    @Override
    public void mainMenuRequested() {
        changeState(new LoadingMenuState(getContext()));
    }
}
