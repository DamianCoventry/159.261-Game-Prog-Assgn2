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

package com.lunargravity.model;

public class FuelCanister {
    static private int ID_COUNTER = 0;
    private final int _id;
    private final int _startingLitres;
    private int _remainingLitres;

    public FuelCanister(int startingLitres) {
        _id = ++ID_COUNTER;
        _startingLitres = startingLitres;
        _remainingLitres = startingLitres;
    }

    public int getId() {
        return _id;
    }

    public int getStartingLitres() {
        return _startingLitres;
    }
    public int getRemainingLitres() {
        return _remainingLitres;
    }

    public int takeFuel(int fuelLitres) {
        if (_remainingLitres == 0 || fuelLitres < 1) {
            return 0;
        }
        if (fuelLitres >= _remainingLitres) {
            int actual = _remainingLitres;
            _remainingLitres = 0;
            return actual;
        }
        _remainingLitres -= fuelLitres;
        return fuelLitres;
    }
}
