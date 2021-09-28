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

package com.lunargravity.application;

import com.lunargravity.engine.core.IEngine;
import com.lunargravity.mvc.IController;
import com.lunargravity.mvc.IModel;
import com.lunargravity.mvc.IView;

public interface IStateMachineContext extends ICurrentFrame, IApplicationModes {
    void changeState(IState state);

    IEngine getEngine(); // used by StateBase to provide convenient addition/removal of timeouts

    IModel getWorldModel(); // TODO: should these be here?
    IView getWorldView();
    IController getWorldController();

    IModel getLogicModel(); // TODO: should these be here?
    IView getLogicView(); 
    IController getLogicController(); // used by RunningMenuState to add/remove a MenuController observer

    void exitApplication(); // TODO: should this be here?
}
