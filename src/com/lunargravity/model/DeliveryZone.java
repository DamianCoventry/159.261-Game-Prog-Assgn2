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

public class DeliveryZone {
    static private int ID_COUNTER = 0;
    private final int _id;
    private State _state;

    public DeliveryZone() {
        _id = ++ID_COUNTER;
        _state = State.IDLE;
    }

    public enum State { IDLE, DELIVERING }

    public int getId() {
        return _id;
    }

    public State getState() {
        return _state;
    }
    public void setState(State state) {
        _state = state;
    }
}
