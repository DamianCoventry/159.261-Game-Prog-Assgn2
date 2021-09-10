package com.lunargravity.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeaponTest {

    @Test
    void shootReturnsFalseIfInInvalidState() {
        IWeapon weapon = new WeaponBase();
        assertTrue(weapon.isIdle());
        assertTrue(weapon.shoot());
        assertTrue(weapon.isShooting());
        assertFalse(weapon.shoot());
    }

    @Test
    void shootWorksCorrectlyWhenInCorrectState() {
        IWeapon weapon = new WeaponBase();
        assertTrue(weapon.isIdle());
        assertTrue(weapon.shoot());
    }

    @Test
    void startCoolingDownReturnsFalseIfInInvalidState1() {
        IWeapon weapon = new WeaponBase();
        assertTrue(weapon.isIdle());
        assertFalse(weapon.startCoolingDown());
    }

    @Test
    void startCoolingDownReturnsFalseIfInInvalidState2() {
        IWeapon weapon = new WeaponBase();
        assertTrue(weapon.isIdle());
        assertTrue(weapon.shoot());
        assertTrue(weapon.isShooting());
        assertTrue(weapon.startCoolingDown());
        assertTrue(weapon.isCoolingDown());
        assertFalse(weapon.startCoolingDown());
    }

    @Test
    void startCoolingDownWorksCorrectlyWhenInCorrectState() {
        IWeapon weapon = new WeaponBase();
        assertTrue(weapon.isIdle());
        assertTrue(weapon.shoot());
        assertTrue(weapon.isShooting());
        assertTrue(weapon.startCoolingDown());
        assertTrue(weapon.isCoolingDown());
    }

    @Test
    void completeCoolingDownReturnsFalseIfInInvalidState1() {
        IWeapon weapon = new WeaponBase();
        assertTrue(weapon.isIdle());
        assertFalse(weapon.completeCoolingDown());
    }

    @Test
    void completeCoolingDownReturnsFalseIfInInvalidState2() {
        IWeapon weapon = new WeaponBase();
        assertTrue(weapon.isIdle());
        assertTrue(weapon.shoot());
        assertTrue(weapon.isShooting());
        assertFalse(weapon.completeCoolingDown());
    }

    @Test
    void completeCoolingDownWorksCorrectlyWhenInCorrectState() {
        IWeapon weapon = new WeaponBase();
        assertTrue(weapon.isIdle());
        assertTrue(weapon.shoot());
        assertTrue(weapon.isShooting());
        assertTrue(weapon.startCoolingDown());
        assertTrue(weapon.isCoolingDown());
        assertTrue(weapon.completeCoolingDown());
        assertTrue(weapon.isIdle());
    }
}
