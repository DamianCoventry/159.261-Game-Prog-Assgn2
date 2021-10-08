package com.lunargravity.world.model;

import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector3f;
import com.lunargravity.application.PlayerInputBindings;

public interface IGameWorldModel extends IWorldModel {
    int getNumPlayers();
    Player getPlayerState(int i);
    void setPlayerObserver(IPlayerObserver observer);
    void resetPlayers();

    void clearCrates();
    void addCrate(PhysicsRigidBody rigidBody, Vector3f startPosition);
    void setCrateObserver(ICrateObserver observer);
    boolean areAllCratesDelivered();
    void updateCrateMovingStates(long nowMs);
    void setCrateLimit(int size);
    int getTotalCrates();
    int getNumIdleCrates();
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

    PlayerInputBindings getPlayerInputBindings();
}
