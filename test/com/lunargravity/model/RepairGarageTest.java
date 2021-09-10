package com.lunargravity.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepairGarageTest {

    @Test
    void repairGarageCreatesProperly() {
        RepairGarage repairGarage = new RepairGarage(100);
        assertNotNull(repairGarage);
        assertEquals(100, repairGarage.getStartingCapacity());
        assertEquals(100, repairGarage.getRemainingCapacity());
    }

    @Test
    void performRepairIgnoresNegativeValues() {
        RepairGarage repairGarage = new RepairGarage(100);
        assertEquals(0, repairGarage.performRepair(-10));
    }

    @Test
    void performRepairIgnoresZeroValues() {
        RepairGarage repairGarage = new RepairGarage(100);
        assertEquals(0, repairGarage.performRepair(0));
    }

    @Test
    void performRepairSubtractsCorrectly() {
        RepairGarage repairGarage = new RepairGarage(100);
        assertEquals(15, repairGarage.performRepair(15));
        assertEquals(15, repairGarage.performRepair(15));
        assertEquals(15, repairGarage.performRepair(15));
    }

    @Test
    void performRepairHandlesBoundaryCondition() {
        RepairGarage repairGarage = new RepairGarage(14);
        assertNotNull(repairGarage);
        assertEquals(5, repairGarage.performRepair(5));
        assertEquals(5, repairGarage.performRepair(5));
        assertEquals(4, repairGarage.performRepair(5));
        assertEquals(0, repairGarage.performRepair(5));
    }
}