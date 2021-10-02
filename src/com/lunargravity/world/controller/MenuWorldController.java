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

package com.lunargravity.world.controller;

import com.lunargravity.world.model.IMenuWorldModel;

public class MenuWorldController implements IMenuWorldController {
    private final IMenuWorldControllerObserver _observer;
    private final IMenuWorldModel _model;

    public MenuWorldController(IMenuWorldControllerObserver observer, IMenuWorldModel model) {
        _observer = observer;
        _model = model;
    }

    @Override
    public void controllerThink() {
        // TODO
    }

    @Override
    public void levelCompleted() {
        // Nothing to do
    }

    @Override
    public void clearPhysicsCollisionListener() {
        // TODO
    }

    @Override
    public void doMenuWorldControllerStuff() {
        // TODO
    }
}
