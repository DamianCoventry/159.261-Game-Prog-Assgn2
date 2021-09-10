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

public class LunarLander {
    static public final int MAX_HIT_POINTS = 150;
    static public final int MAX_FUEL_LITRES = 100;

    static private int ID_COUNTER = 0;
    private final int _id;
    private State _state;
    private int _hitPoints;
    private int _fuelLitres;
    private int _numCollectedCrates;
    private IWeapon _weapon;

    public LunarLander() {
        _id = ++ID_COUNTER;
        _state = State.NOT_YET_BORN;
        _hitPoints = MAX_HIT_POINTS;
        _fuelLitres = MAX_FUEL_LITRES;
        _numCollectedCrates = 0;
        _weapon = new PlasmaGun();
    }

    public enum State { NOT_YET_BORN, SPAWNING, IDLE, OUT_OF_FUEL, EXPLODING, DEAD, COLLECTING, REFUELLING, REPAIRING }

    public int getId() {
        return _id;
    }

    public State getState() {
        return _state;
    }
    public int getHitPoints() {
        return _hitPoints;
    }
    public int getFuelLitres() {
        return _fuelLitres;
    }
    public int getNumCollectedCrates() {
        return _numCollectedCrates;
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
        _numCollectedCrates = 0;
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

    public boolean startCollecting() {
        if (_state != State.IDLE) {
            return false;
        }
        _state = State.COLLECTING;
        return true;
    }
    public boolean completeCollecting() {
        if (_state != State.COLLECTING) {
            return false;
        }
        ++_numCollectedCrates;
        _state = State.IDLE;
        return true;
    }

    public boolean startDelivering() {
        if (_state == State.IDLE && _numCollectedCrates > 0) {
            --_numCollectedCrates;
            return true;
        }
        return false;
    }

    public boolean startRefuelling() {
        if (_state == State.IDLE && _fuelLitres < MAX_FUEL_LITRES) {
            _state = State.REFUELLING;
            return true;
        }
        return false;
    }
    public boolean refuel(int fuelLitres) {
        if (_state != State.REFUELLING || _fuelLitres == MAX_FUEL_LITRES) {
            return false;
        }
        _fuelLitres += fuelLitres;
        if (_fuelLitres >= MAX_FUEL_LITRES) {
            _state = State.IDLE;
            _fuelLitres = MAX_FUEL_LITRES;
        }
        return true;
    }
    public boolean useFuel(int fuelLitres) {
        if (_state == State.IDLE && _fuelLitres > 0) {
            _fuelLitres -= fuelLitres;
            if (_fuelLitres <= 0) {
                _state = State.OUT_OF_FUEL;
                _fuelLitres = 0;
            }
            return true;
        }
        return false;
    }
    public boolean completeRefuelling() {
        if (_state != State.REFUELLING) {
            return false;
        }
        _state = State.IDLE;
        return true;
    }

    public boolean startRepairing() {
        if (_state == State.IDLE && _hitPoints < MAX_HIT_POINTS) {
            _state = State.REPAIRING;
            return true;
        }
        return false;
    }
    public boolean repair(int hitPoints) {
        if (_state != State.REPAIRING || _hitPoints == MAX_HIT_POINTS) {
            return false;
        }
        _hitPoints += hitPoints;
        if (_hitPoints >= MAX_HIT_POINTS) {
            _state = State.IDLE;
            _hitPoints = MAX_HIT_POINTS;
        }
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
    public boolean completeRepairing() {
        if (_state != State.REPAIRING) {
            return false;
        }
        _state = State.IDLE;
        return true;
    }
}
