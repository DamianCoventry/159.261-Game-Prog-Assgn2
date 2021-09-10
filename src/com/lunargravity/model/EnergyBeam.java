package com.lunargravity.model;

public class EnergyBeam {
    static private int ID_COUNTER = 0;
    private final int _id;
    private final int _damagePerSecond;
    private State _state;

    public EnergyBeam(int damagePerSecond) {
        _id = ++ID_COUNTER;
        _state = State.INACTIVE;
        _damagePerSecond = damagePerSecond;
    }

    public enum State { INACTIVE, ACTIVE }

    public int getId() {
        return _id;
    }
    public int getDamagePerSecond() {
        return _damagePerSecond;
    }

    public boolean isActive() {
        return _state == State.ACTIVE;
    }
    public void activate() {
        _state = State.ACTIVE;
    }
    public void deactivate() {
        _state = State.INACTIVE;
    }
}
