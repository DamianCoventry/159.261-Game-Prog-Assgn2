package com.lunargravity.model;

public interface IWeapon {
    enum State { IDLE, SHOOTING, COOLING_DOWN }
    int getId();
    WeaponType getType();
    boolean isIdle();
    boolean isShooting();
    boolean isCoolingDown();
    boolean shoot();
    boolean startCoolingDown();
    boolean completeCoolingDown();
}
