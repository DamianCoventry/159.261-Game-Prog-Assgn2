package com.lunargravity.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FuelCanisterTest {

    @Test
    void fuelCanisterCreatesProperly() {
        FuelCanister fuelCanister = new FuelCanister(100);
        assertNotNull(fuelCanister);
        assertEquals(100, fuelCanister.getStartingLitres());
        assertEquals(100, fuelCanister.getRemainingLitres());
    }

    @Test
    void takeFuelIgnoresNegativeValues() {
        FuelCanister fuelCanister = new FuelCanister(100);
        assertEquals(0, fuelCanister.takeFuel(-10));
    }

    @Test
    void takeFuelIgnoresZeroValues() {
        FuelCanister fuelCanister = new FuelCanister(100);
        assertEquals(0, fuelCanister.takeFuel(0));
    }

    @Test
    void takeFuelSubtractsCorrectly() {
        FuelCanister fuelCanister = new FuelCanister(100);
        assertEquals(15, fuelCanister.takeFuel(15));
        assertEquals(15, fuelCanister.takeFuel(15));
        assertEquals(15, fuelCanister.takeFuel(15));
    }

    @Test
    void takeFuelHandlesBoundaryCondition() {
        FuelCanister fuelCanister = new FuelCanister(14);
        assertNotNull(fuelCanister);
        assertEquals(5, fuelCanister.takeFuel(5));
        assertEquals(5, fuelCanister.takeFuel(5));
        assertEquals(4, fuelCanister.takeFuel(5));
        assertEquals(0, fuelCanister.takeFuel(5));
    }}