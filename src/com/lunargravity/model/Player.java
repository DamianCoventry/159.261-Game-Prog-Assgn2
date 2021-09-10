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

public class Player {
    static public final int MAX_LUNAR_LANDERS = 3;

    static private int ID_COUNTER = 0;
    private final int _id;
    private int _numLunarLanders;
    private final LunarLander[] _lunarLanders;

    public Player() {
        _id = ++ID_COUNTER;
        _lunarLanders = new LunarLander[MAX_LUNAR_LANDERS];
        for (int i = 0; i < MAX_LUNAR_LANDERS; ++i) {
            _lunarLanders[i] = new LunarLander();
        }
        _numLunarLanders = MAX_LUNAR_LANDERS;
    }

    public int getId() {
        return _id;
    }

    public int getNumLunarLanders() {
        return _numLunarLanders;
    }

    public LunarLander getCurrentLunarLander() {
        if (_numLunarLanders == 0) {
            return null;
        }
        return _lunarLanders[0];
    }

    public boolean destroyCurrentLunarLander() {
        if (_numLunarLanders == 0) {
            return false;
        }
        --_numLunarLanders;
        return true;
    }
}
