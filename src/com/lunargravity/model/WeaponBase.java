package com.lunargravity.model;

public class WeaponBase implements IWeapon {
    static private int ID_COUNTER = 0;
    private final int _id;
    private State _state;

    public WeaponBase() {
        _id = ++ID_COUNTER;
        _state = State.IDLE;
    }

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public WeaponType getType() {
        return null;
    }

    @Override
    public boolean isIdle() {
        return _state == State.IDLE;
    }

    @Override
    public boolean isShooting() {
        return _state == State.SHOOTING;
    }

    @Override
    public boolean isCoolingDown() {
        return _state == State.COOLING_DOWN;
    }

    @Override
    public boolean shoot() {
        if (_state == State.IDLE) {
            _state = State.SHOOTING;
            return true;
        }
        return false;
    }

    @Override
    public boolean startCoolingDown() {
        if (_state != State.SHOOTING) {
            return false;
        }
        _state = State.COOLING_DOWN;
        return true;
    }

    @Override
    public boolean completeCoolingDown() {
        if (_state != State.COOLING_DOWN) {
            return false;
        }
        _state = State.IDLE;
        return true;
    }
}
