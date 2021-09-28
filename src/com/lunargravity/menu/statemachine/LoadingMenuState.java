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

package com.lunargravity.menu.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.menu.view.MenuBuilderObserver;

import java.io.IOException;

public class LoadingMenuState extends StateBase {
    public LoadingMenuState(IStateMachineContext context) {
        super(context);
    }

    @Override
    public void begin() throws IOException, InterruptedException {
        MenuBuilderObserver menuBuilderObserver = new MenuBuilderObserver(getContext().getEngine(), getManualFrameUpdater());

        getContext().startMenu(menuBuilderObserver);

        menuBuilderObserver.freeResources();
    }

    @Override
    public void think() {
        changeState(new RunningMenuState(getContext()));
    }
}
