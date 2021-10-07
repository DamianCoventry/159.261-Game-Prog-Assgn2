package com.lunargravity.world.model;

public interface IPlayerObserver {
    void crateCollectingStarted(Player player, Crate crate);
    void crateCollectionCompleted(Crate crate);
    void crateCollectionAborted(Player player);
    void playerShipTookDamage(int player, int hitPointsDamage, int hitPointsRemaining);
    void playerShipExploding(int player);
    void playerShipDead(int player);
    void playerFiredWeapon(int id);
    void playerWeaponCooledDown(int id);
}
