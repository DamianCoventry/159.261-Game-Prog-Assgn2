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

public class RepairGarage {
    static private int ID_COUNTER = 0;
    private final int _id;
    private final int _startingCapacity;
    private int _remainingCapacity;

    public RepairGarage(int startingCapacity) {
        _id = ++ID_COUNTER;
        _startingCapacity = startingCapacity;
        _remainingCapacity = startingCapacity;
    }

    public int getId() {
        return _id;
    }

    public int getStartingCapacity() {
        return _startingCapacity;
    }
    public int getRemainingCapacity() {
        return _remainingCapacity;
    }

    public int performRepair(int repair) {
        if (_remainingCapacity == 0 || repair < 1) {
            return 0;
        }
        if (repair >= _remainingCapacity) {
            int actual = _remainingCapacity;
            _remainingCapacity = 0;
            return actual;
        }
        _remainingCapacity -= repair;
        return repair;
    }
}
