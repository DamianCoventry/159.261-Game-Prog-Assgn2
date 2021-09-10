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

package com.lunargravity.controller;

import com.lunargravity.view.IView;

public class Controller implements IController {
    private IControllerEvents _eventHandler;
    private IView _view;
    public Controller(IControllerEvents eventHandler, IView view) {
        _eventHandler = eventHandler;
        _view = view;
    }
    @Override
    public void Temp() {

    }
}
