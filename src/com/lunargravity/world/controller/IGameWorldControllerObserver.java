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

package com.lunargravity.world.controller;

import com.lunargravity.world.model.Crate;
import com.lunargravity.world.model.Player;
import com.lunargravity.world.model.PlayerShot;

public interface IGameWorldControllerObserver {
    void crateCollectionCompleted(Crate crate);
    void crateCollectionAborted();
    void crateStartedDelivering(Crate crate, com.jme3.math.Vector3f playerPosition);
    void crateDeliveryCompleted(Crate crate);
    void respawnCrateAtStartPosition(Crate crate);
    void allCratesDelivered();
    void playerShipCollided(Player player, float appliedImpulse);
    void playerShipTookDamage(int player, int hitPointsDamage, int hitPointsRemaining);
    void playerShipExploding(int player);
    void playerShipDead(int player);
    void playerShipSpawned(int player);
    void playerFiredWeapon(int id);
    void playerWeaponCooledDown(int id);
    void playerShotExploded(PlayerShot playerShot);
}
