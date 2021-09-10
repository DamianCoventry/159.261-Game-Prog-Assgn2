package com.lunargravity.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void playerCreatesProperly() {
        Player player = new Player();
        assertNotNull(player);
        assertEquals(Player.MAX_LUNAR_LANDERS, player.getNumLunarLanders());
        assertNotNull(player.getCurrentLunarLander());
    }

    @Test
    void playerLunarLandersCanBeDestroyed() {
        Player player = new Player();
        assertEquals(Player.MAX_LUNAR_LANDERS, player.getNumLunarLanders());
        assertNotNull(player.getCurrentLunarLander());
        assertTrue(player.destroyCurrentLunarLander());
        assertEquals(2, player.getNumLunarLanders());
        assertNotNull(player.getCurrentLunarLander());
        assertTrue(player.destroyCurrentLunarLander());
        assertEquals(1, player.getNumLunarLanders());
        assertNotNull(player.getCurrentLunarLander());
        assertTrue(player.destroyCurrentLunarLander());
        assertEquals(0, player.getNumLunarLanders());
        assertNull(player.getCurrentLunarLander());
    }

    @Test
    void destroyCurrentLunarLanderHandlesBoundaryCase() {
        Player player = new Player();
        assertTrue(player.destroyCurrentLunarLander());
        assertTrue(player.destroyCurrentLunarLander());
        assertTrue(player.destroyCurrentLunarLander());
        assertFalse(player.destroyCurrentLunarLander());
    }
}