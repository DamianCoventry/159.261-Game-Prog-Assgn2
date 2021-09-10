package com.lunargravity.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LunarLanderTest {

    @Test
    void lunarLanderCreatesProperly() {
        LunarLander lunarLander = new LunarLander();
        assertNotNull(lunarLander);
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertEquals(LunarLander.MAX_HIT_POINTS, lunarLander.getHitPoints());
        assertEquals(LunarLander.MAX_FUEL_LITRES, lunarLander.getFuelLitres());
        assertEquals(0, lunarLander.getNumCollectedCrates());
        assertNotNull(lunarLander.getWeapon());
    }

    @Test
    void spawnReturnsFalseIfInInvalidState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertTrue(lunarLander.startCollecting());
        assertEquals(LunarLander.State.COLLECTING, lunarLander.getState());
        assertFalse(lunarLander.spawn());
    }

    @Test
    void spawnWorksCorrectlyWhenInCorrectState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
    }

    @Test
    void completeSpawningReturnsFalseIfInInvalidState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertFalse(lunarLander.completeSpawning());
    }

    @Test
    void completeSpawningWorksCorrectlyWhenInCorrectState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
    }

    @Test
    void explodeReturnsFalseIfInInvalidState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertFalse(lunarLander.explode());
    }

    @Test
    void explodeWorksCorrectlyWhenInCorrectState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertTrue(lunarLander.explode());
        assertEquals(LunarLander.State.EXPLODING, lunarLander.getState());
    }

    @Test
    void completeExplodingReturnsFalseIfInInvalidState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertFalse(lunarLander.completeExploding());
    }

    @Test
    void completeExplodingWorksCorrectlyWhenInCorrectState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertTrue(lunarLander.explode());
        assertEquals(LunarLander.State.EXPLODING, lunarLander.getState());
        assertTrue(lunarLander.completeExploding());
        assertEquals(LunarLander.State.DEAD, lunarLander.getState());
    }

    @Test
    void startCollectingReturnsFalseIfInInvalidState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertFalse(lunarLander.startCollecting());
    }

    @Test
    void startCollectingWorksCorrectlyWhenInCorrectState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertTrue(lunarLander.startCollecting());
        assertEquals(LunarLander.State.COLLECTING, lunarLander.getState());
    }

    @Test
    void completeCollectingReturnsFalseIfInInvalidState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertFalse(lunarLander.completeCollecting());
    }

    @Test
    void completeCollectingWorksCorrectlyWhenInCorrectState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertEquals(0, lunarLander.getNumCollectedCrates());
        assertTrue(lunarLander.startCollecting());
        assertEquals(0, lunarLander.getNumCollectedCrates());
        assertEquals(LunarLander.State.COLLECTING, lunarLander.getState());
        assertTrue(lunarLander.completeCollecting());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertEquals(1, lunarLander.getNumCollectedCrates());
    }

    @Test
    void startDeliveringReturnsFalseIfInInvalidState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertFalse(lunarLander.startDelivering());
    }

    @Test
    void startDeliveringReturnsFalseIfNoCollectedCrates() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertFalse(lunarLander.startDelivering());
    }

    @Test
    void startDeliveringWorksCorrectlyWhenInCorrectStateAndCollectedACrate() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertEquals(0, lunarLander.getNumCollectedCrates());
        assertTrue(lunarLander.startCollecting());
        assertEquals(0, lunarLander.getNumCollectedCrates());
        assertEquals(LunarLander.State.COLLECTING, lunarLander.getState());
        assertTrue(lunarLander.completeCollecting());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertEquals(1, lunarLander.getNumCollectedCrates());
        assertTrue(lunarLander.startDelivering());
        assertEquals(0, lunarLander.getNumCollectedCrates());
    }

    @Test
    void startRefuellingReturnsFalseIfInInvalidState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertFalse(lunarLander.startRefuelling());
    }

    @Test
    void startRefuellingWorksCorrectlyWhenInCorrectState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertTrue(lunarLander.useFuel(10));
        assertTrue(lunarLander.startRefuelling());
        assertEquals(LunarLander.State.REFUELLING, lunarLander.getState());
    }

    @Test
    void refuelReturnsFalseIfInInvalidState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertFalse(lunarLander.refuel(10));
    }

    @Test
    void refuelWorksCorrectlyWhenInCorrectState1() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertTrue(lunarLander.useFuel(10));
        assertTrue(lunarLander.startRefuelling());
        assertEquals(LunarLander.State.REFUELLING, lunarLander.getState());
        assertTrue(lunarLander.refuel(10));
    }

    @Test
    void refuelWorksCorrectlyWhenInCorrectState2() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertEquals(LunarLander.MAX_FUEL_LITRES, lunarLander.getFuelLitres());
        assertTrue(lunarLander.useFuel(20));
        assertEquals(LunarLander.MAX_FUEL_LITRES - 20, lunarLander.getFuelLitres());
        assertTrue(lunarLander.startRefuelling());
        assertEquals(LunarLander.State.REFUELLING, lunarLander.getState());
        assertTrue(lunarLander.refuel(10));
        assertTrue(lunarLander.completeRefuelling());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertEquals(LunarLander.MAX_FUEL_LITRES - 10, lunarLander.getFuelLitres());
    }

    @Test
    void useFuelReturnsFalseIfInInvalidState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertFalse(lunarLander.useFuel(10));
    }

    @Test
    void useFuelWorksCorrectlyWhenInCorrectState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertEquals(LunarLander.MAX_FUEL_LITRES, lunarLander.getFuelLitres());
        assertTrue(lunarLander.useFuel(10));
        assertEquals(LunarLander.MAX_FUEL_LITRES - 10, lunarLander.getFuelLitres());
    }

    @Test
    void completeRefuellingReturnsFalseIfInInvalidState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertFalse(lunarLander.completeRefuelling());
    }

    @Test
    void completeRefuellingWorksCorrectlyWhenInCorrectState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertEquals(LunarLander.MAX_FUEL_LITRES, lunarLander.getFuelLitres());
        assertTrue(lunarLander.useFuel(20));
        assertTrue(lunarLander.startRefuelling());
        assertEquals(LunarLander.State.REFUELLING, lunarLander.getState());
        assertTrue(lunarLander.refuel(10));
        assertTrue(lunarLander.completeRefuelling());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
    }

    @Test
    void startRepairingReturnsFalseIfInInvalidState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertFalse(lunarLander.startRepairing());
    }

    @Test
    void startRepairingWorksCorrectlyWhenInCorrectState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertTrue(lunarLander.takeDamage(10));
        assertTrue(lunarLander.startRepairing());
        assertEquals(LunarLander.State.REPAIRING, lunarLander.getState());
    }

    @Test
    void repairReturnsFalseIfInInvalidState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertFalse(lunarLander.repair(10));
    }

    @Test
    void repairWorksCorrectlyWhenInCorrectState1() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertTrue(lunarLander.takeDamage(10));
        assertTrue(lunarLander.startRepairing());
        assertEquals(LunarLander.State.REPAIRING, lunarLander.getState());
        assertTrue(lunarLander.repair(10));
    }

    @Test
    void repairWorksCorrectlyWhenInCorrectState2() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertEquals(LunarLander.MAX_HIT_POINTS, lunarLander.getHitPoints());
        assertTrue(lunarLander.takeDamage(20));
        assertEquals(LunarLander.MAX_HIT_POINTS - 20, lunarLander.getHitPoints());
        assertTrue(lunarLander.startRepairing());
        assertEquals(LunarLander.State.REPAIRING, lunarLander.getState());
        assertTrue(lunarLander.repair(10));
        assertTrue(lunarLander.completeRepairing());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertEquals(LunarLander.MAX_HIT_POINTS - 10, lunarLander.getHitPoints());
    }

    @Test
    void takeDamageReturnsFalseIfInInvalidState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertFalse(lunarLander.takeDamage(10));
    }

    @Test
    void takeDamageWorksCorrectlyWhenInCorrectState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertEquals(LunarLander.MAX_HIT_POINTS, lunarLander.getHitPoints());
        assertTrue(lunarLander.takeDamage(10));
        assertEquals(LunarLander.MAX_HIT_POINTS - 10, lunarLander.getHitPoints());
    }

    @Test
    void completeRepairingReturnsFalseIfInInvalidState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertFalse(lunarLander.completeRepairing());
    }

    @Test
    void completeRepairingWorksCorrectlyWhenInCorrectState() {
        LunarLander lunarLander = new LunarLander();
        assertEquals(LunarLander.State.NOT_YET_BORN, lunarLander.getState());
        assertTrue(lunarLander.spawn());
        assertEquals(LunarLander.State.SPAWNING, lunarLander.getState());
        assertTrue(lunarLander.completeSpawning());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
        assertEquals(LunarLander.MAX_HIT_POINTS, lunarLander.getHitPoints());
        assertTrue(lunarLander.takeDamage(20));
        assertTrue(lunarLander.startRepairing());
        assertEquals(LunarLander.State.REPAIRING, lunarLander.getState());
        assertTrue(lunarLander.repair(10));
        assertTrue(lunarLander.completeRepairing());
        assertEquals(LunarLander.State.IDLE, lunarLander.getState());
    }
}
