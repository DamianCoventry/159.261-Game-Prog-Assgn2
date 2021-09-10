package com.lunargravity.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnemyShipTest {

    @Test
    void enemyShipCreatesProperly() {
        EnemyShip enemyShip = new EnemyShip(EnemyShipType.IMP);
        assertNotNull(enemyShip);
        assertEquals(EnemyShip.State.NOT_YET_BORN, enemyShip.getState());
        assertEquals(EnemyShipHitPoints.get(EnemyShipType.IMP), enemyShip.getHitPoints());
        assertNotNull(enemyShip.getWeapon());
    }

    @Test
    void spawnReturnsFalseIfInInvalidState() {
        EnemyShip enemyShip = new EnemyShip(EnemyShipType.IMP);
        assertEquals(EnemyShip.State.NOT_YET_BORN, enemyShip.getState());
        assertTrue(enemyShip.spawn());
        assertEquals(EnemyShip.State.SPAWNING, enemyShip.getState());
        assertTrue(enemyShip.completeSpawning());
        assertEquals(EnemyShip.State.IDLE, enemyShip.getState());
        assertTrue(enemyShip.explode());
        assertEquals(EnemyShip.State.EXPLODING, enemyShip.getState());
        assertFalse(enemyShip.spawn());
    }

    @Test
    void spawnWorksCorrectlyWhenInCorrectState() {
        EnemyShip enemyShip = new EnemyShip(EnemyShipType.IMP);
        assertEquals(EnemyShip.State.NOT_YET_BORN, enemyShip.getState());
        assertTrue(enemyShip.spawn());
        assertEquals(EnemyShip.State.SPAWNING, enemyShip.getState());
    }

    @Test
    void completeSpawningReturnsFalseIfInInvalidState() {
        EnemyShip enemyShip = new EnemyShip(EnemyShipType.IMP);
        assertEquals(EnemyShip.State.NOT_YET_BORN, enemyShip.getState());
        assertFalse(enemyShip.completeSpawning());
    }

    @Test
    void completeSpawningWorksCorrectlyWhenInCorrectState() {
        EnemyShip enemyShip = new EnemyShip(EnemyShipType.IMP);
        assertEquals(EnemyShip.State.NOT_YET_BORN, enemyShip.getState());
        assertTrue(enemyShip.spawn());
        assertEquals(EnemyShip.State.SPAWNING, enemyShip.getState());
        assertTrue(enemyShip.completeSpawning());
        assertEquals(EnemyShip.State.IDLE, enemyShip.getState());
    }

    @Test
    void explodeReturnsFalseIfInInvalidState() {
        EnemyShip enemyShip = new EnemyShip(EnemyShipType.IMP);
        assertEquals(EnemyShip.State.NOT_YET_BORN, enemyShip.getState());
        assertFalse(enemyShip.explode());
    }

    @Test
    void explodeWorksCorrectlyWhenInCorrectState() {
        EnemyShip enemyShip = new EnemyShip(EnemyShipType.IMP);
        assertEquals(EnemyShip.State.NOT_YET_BORN, enemyShip.getState());
        assertTrue(enemyShip.spawn());
        assertEquals(EnemyShip.State.SPAWNING, enemyShip.getState());
        assertTrue(enemyShip.completeSpawning());
        assertEquals(EnemyShip.State.IDLE, enemyShip.getState());
        assertTrue(enemyShip.explode());
        assertEquals(EnemyShip.State.EXPLODING, enemyShip.getState());
    }

    @Test
    void completeExplodingReturnsFalseIfInInvalidState() {
        EnemyShip enemyShip = new EnemyShip(EnemyShipType.IMP);
        assertEquals(EnemyShip.State.NOT_YET_BORN, enemyShip.getState());
        assertFalse(enemyShip.completeExploding());
    }

    @Test
    void completeExplodingWorksCorrectlyWhenInCorrectState() {
        EnemyShip enemyShip = new EnemyShip(EnemyShipType.IMP);
        assertEquals(EnemyShip.State.NOT_YET_BORN, enemyShip.getState());
        assertTrue(enemyShip.spawn());
        assertEquals(EnemyShip.State.SPAWNING, enemyShip.getState());
        assertTrue(enemyShip.completeSpawning());
        assertEquals(EnemyShip.State.IDLE, enemyShip.getState());
        assertTrue(enemyShip.explode());
        assertEquals(EnemyShip.State.EXPLODING, enemyShip.getState());
        assertTrue(enemyShip.completeExploding());
        assertEquals(EnemyShip.State.DEAD, enemyShip.getState());
    }

    @Test
    void takeDamageReturnsFalseIfInInvalidState() {
        EnemyShip enemyShip = new EnemyShip(EnemyShipType.IMP);
        assertEquals(EnemyShip.State.NOT_YET_BORN, enemyShip.getState());
        assertFalse(enemyShip.takeDamage(10));
    }

    @Test
    void takeDamageWorksCorrectlyWhenInCorrectState() {
        EnemyShip enemyShip = new EnemyShip(EnemyShipType.IMP);
        assertEquals(EnemyShip.State.NOT_YET_BORN, enemyShip.getState());
        assertTrue(enemyShip.spawn());
        assertEquals(EnemyShip.State.SPAWNING, enemyShip.getState());
        assertTrue(enemyShip.completeSpawning());
        assertEquals(EnemyShip.State.IDLE, enemyShip.getState());
        assertEquals(EnemyShipHitPoints.get(EnemyShipType.IMP), enemyShip.getHitPoints());
        assertTrue(enemyShip.takeDamage(10));
        assertEquals(EnemyShipHitPoints.get(EnemyShipType.IMP) - 10, enemyShip.getHitPoints());
    }
}