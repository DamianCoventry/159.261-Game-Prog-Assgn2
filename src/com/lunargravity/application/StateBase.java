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

public class StateBase implements IState {
    IContext _context;
    protected StateBase(IContext context) {
        _context = context;
    }

    @Override
    public void Temp() {

    }

    protected IContext getContext() {
        return _context;
    }
}
