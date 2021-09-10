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

public class EnemyShip {
    static private int ID_COUNTER = 0;
    private final int _id;
    private State _state;
    private int _hitPoints;
    private final EnemyShipType _type;
    private IWeapon _weapon;

    public EnemyShip(EnemyShipType enemyShipType) {
        _id = ++ID_COUNTER;
        _state = State.NOT_YET_BORN;
        _type = enemyShipType;
        _hitPoints = EnemyShipHitPoints.get(_type);
        _weapon = new PlasmaGun();
    }

    public enum State { NOT_YET_BORN, SPAWNING, IDLE, EXPLODING, DEAD }

    public int getId() {
        return _id;
    }
    public State getState() {
        return _state;
    }
    public EnemyShipType getType() {
        return _type;
    }
    public int getHitPoints() {
        return _hitPoints;
    }

    public IWeapon getWeapon() {
        return _weapon;
    }
    void equipWeapon(IWeapon weapon) {
        _weapon = weapon;
    }
    void dropWeapon() {
        _weapon = null;
    }

    public boolean spawn() {
        if (_state != State.NOT_YET_BORN) {
            return false;
        }
        _state = State.SPAWNING;
        return true;
    }
    public boolean completeSpawning() {
        if (_state != State.SPAWNING) {
            return false;
        }
        _state = State.IDLE;
        return true;
    }
    public boolean explode() {
        if (_state == State.NOT_YET_BORN || _state == State.SPAWNING) {
            return false;
        }
        _state = State.EXPLODING;
        return true;
    }
    public boolean completeExploding() {
        if (_state != State.EXPLODING) {
            return false;
        }
        _state = State.DEAD;
        return true;
    }
    public boolean takeDamage(int damageHitPoints) {
        if (_state == State.IDLE && _hitPoints > 0) {
            _hitPoints -= damageHitPoints;
            if (_hitPoints < 0) {
                _state = State.EXPLODING;
                _hitPoints = 0;
            }
            return true;
        }
        return false;
    }
}
