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

public class Rocket {
    static private int ID_COUNTER = 0;
    private final int _id;
    private int _damageHitPoints;
    private double _radius;

    public Rocket(int damageHitPoints, double radius) {
        _id = ++ID_COUNTER;
        _damageHitPoints = damageHitPoints;
        _radius = radius;
    }

    public int getId() {
        return _id;
    }

    public int getDamageHitPoints() {
        return _damageHitPoints;
    }
    public void setDamageHitPoints(int damageHitPoints) {
        _damageHitPoints = damageHitPoints;
    }

    public double getRadius() {
        return _radius;
    }
    public void setRadius(double radius) {
        _radius = radius;
    }
}
