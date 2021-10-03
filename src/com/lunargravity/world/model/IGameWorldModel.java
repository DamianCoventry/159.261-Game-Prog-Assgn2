package com.lunargravity.world.model;

import com.jme3.bullet.objects.PhysicsRigidBody;

public interface IGameWorldModel extends IWorldModel {
    int getNumPlayers();
    Player getPlayerState(int i);
    void setPlayerObserver(IPlayerObserver observer);
    void resetPlayers();

    void clearCrates();
    void addCrate(PhysicsRigidBody rigidBody);
    void setCrateObserver(ICrateObserver observer);
    boolean areAllCratesDelivered();
    int getNumCratesRemaining();
    int getNumCratesCollected();
    int getNumCratesDelivered();

    void clearDeliveryZones();
    void addDeliveryZone(PhysicsRigidBody rigidBody);
    void setDeliveryZoneObserver(IDeliveryZoneObserver observer);

    void clearPlayerShots();
    void addPlayerShot(Player player, PhysicsRigidBody rigidBody);
    void setPlayerShotObserver(IPlayerShotObserver observer);

    long getEpisode();
    long getMission();
    long getShipsRemaining(int playerId);
}
