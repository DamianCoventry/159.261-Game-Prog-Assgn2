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

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector3f;
import com.lunargravity.campaign.controller.ICampaignController;
import com.lunargravity.campaign.controller.ICampaignControllerObserver;
import com.lunargravity.campaign.view.ICampaignView;
import com.lunargravity.engine.core.IEngine;
import com.lunargravity.mvc.IController;
import com.lunargravity.world.model.*;

import java.util.ArrayList;

public class GameWorldController implements
        IGameWorldController,
        IPlayerObserver,
        ICrateObserver,
        IDeliveryZoneObserver,
        ICampaignControllerObserver
{
    public static final float LUNAR_LANDER_MASS = 2.0f;
    public static final float LUNAR_LANDER_DAMAGE_SCALE = 0.1f;
    public static final float MIN_VELOCITY_FOR_DAMAGE = 3.5f;
    public static final float MIN_KINETIC_ENERGY_FOR_DAMAGE = 5.0f;
    public static final float DAMAGE_TIME_WINDOW = 250.0f;
    public static final int KINETIC_ENERGY_BATCH_SIZE = 100;

    private final ArrayList<IGameWorldControllerObserver> _observers;
    private final ICampaignController _logicController;
    private final IGameWorldModel _model;
    private final IEngine _engine;
    private final Vector3f _velocity;
    private final float[][] _kineticEnergyBatches;
    private final int[] _kineticEnergyCounts;
    private final long[] _hitPointDamageTimestamps;

    public GameWorldController(IEngine engine, IController controller, IGameWorldModel model) {
        _engine = engine;
        _engine.setPhysicsCollisionListener(this::processPhysicsCollisionEvent);
        _logicController = (ICampaignController)controller;
        _observers = new ArrayList<>();
        _velocity = new Vector3f();

        _hitPointDamageTimestamps = new long[2];
        _kineticEnergyCounts = new int[2];
        _kineticEnergyBatches = new float[2][KINETIC_ENERGY_BATCH_SIZE];

        _model = model;
        _model.setPlayerObserver(this);
        _model.setCrateObserver(this);
        _model.setDeliveryZoneObserver(this);
    }

    public void addObserver(IGameWorldControllerObserver observer) {
        _observers.add(observer);
    }

    public void removeObserver(IGameWorldControllerObserver observer) {
        _observers.remove(observer);
    }

    @Override
    public void controllerThink() {
        long nowMs = _engine.getNowMs();
        for (int i = 0; i < _model.getNumPlayers(); ++i) {
            Player player = _model.getPlayerState(i);
            if (player != null) {
                player.applyInputCommands();
                player.stopCollectingCrateIfMoving();
                if (nowMs - _hitPointDamageTimestamps[i] >= DAMAGE_TIME_WINDOW) {
                    applyPlayerDamage(player);
                    _hitPointDamageTimestamps[i] = nowMs;
                }
            }
        }
    }

    @Override
    public void levelCompleted() {
        // Nothing to do
    }

    @Override
    public void clearPhysicsCollisionListener() {
        _engine.setPhysicsCollisionListener(null);
    }

    @Override
    public void playerStartThrust(int player) {
        Player playerState = _model.getPlayerState(player);
        if (playerState != null) {
            playerState.setThrustActive(true);
        }
    }

    @Override
    public void playerStartRotateCcw(int player) {
        Player playerState = _model.getPlayerState(player);
        if (playerState != null) {
            playerState.setRotateCcwActive(true);
        }
    }

    @Override
    public void playerStartRotateCw(int player) {
        Player playerState = _model.getPlayerState(player);
        if (playerState != null) {
            playerState.setRotateCwActive(true);
        }
    }

    @Override
    public void playerStartShoot(int player) {
        Player playerState = _model.getPlayerState(player);
        if (playerState != null) {
            playerState.setShootActive(true);
        }
    }

    @Override
    public void playerStopThrust(int player) {
        Player playerState = _model.getPlayerState(player);
        if (playerState != null) {
            playerState.setThrustActive(false);
        }
    }

    @Override
    public void playerStopRotateCcw(int player) {
        Player playerState = _model.getPlayerState(player);
        if (playerState != null) {
            playerState.setRotateCcwActive(false);
        }
    }

    @Override
    public void playerStopRotateCw(int player) {
        Player playerState = _model.getPlayerState(player);
        if (playerState != null) {
            playerState.setRotateCwActive(false);
        }
    }

    @Override
    public void playerStopShoot(int player) {
        Player playerState = _model.getPlayerState(player);
        if (playerState != null) {
            playerState.setShootActive(false);
        }
    }

    @Override
    public void playerKick(int player) {
        Player playerState = _model.getPlayerState(player);
        if (playerState != null) {
            playerState.kick();
        }
    }

    @Override
    public void crateCollectionCompleted(Crate crate) {
        for (var observer : _observers) {
            observer.crateCollectionCompleted(crate);
        }
    }

    @Override
    public void crateCollectionAborted() {
        for (var observer : _observers) {
            observer.crateCollectionAborted();
        }
    }

    @Override
    public void playerShipTookDamage(int player, int hitPointsDamage, int hitPointsRemaining) {
        for (var observer : _observers) {
            observer.playerShipTookDamage(player, hitPointsDamage, hitPointsRemaining);
        }
    }

    @Override
    public void playerShipExploding(int player) {
        for (var observer : _observers) {
            observer.playerShipExploding(player);
        }
    }

    @Override
    public void playerShipDead(int player) {
        for (var observer : _observers) {
            observer.playerShipDead(player);
        }
        _logicController.playerDied(player == 0 ?
                ICampaignView.WhichPlayer.PLAYER_1 : ICampaignView.WhichPlayer.PLAYER_2);
    }

    @Override
    public void crateStartedDelivering(Crate crate, com.jme3.math.Vector3f playerPosition) {
        for (var observer : _observers) {
            observer.crateStartedDelivering(crate, playerPosition);
        }
    }

    @Override
    public void crateDeliveryCompleted(Crate crate) {
        for (var observer : _observers) {
            observer.crateDeliveryCompleted(crate);
        }
        if (_model.areAllCratesDelivered()) {
            for (var observer : _observers) {
                observer.allCratesDelivered();
            }
            _logicController.levelCompleted();
        }
    }

    private void processPhysicsCollisionEvent(PhysicsCollisionEvent event) {
        if (event.getObjectA().getUserObject() instanceof Player player) {
            if (event.getObjectB().getUserObject() instanceof Crate crate) {
                processPlayerToCrateCollision(player, crate);
                damagePlayerShip(player, crate);
            }
            else if (event.getObjectB().getUserObject() instanceof DeliveryZone deliveryZone) {
                processPlayerToDeliveryZoneCollision(player, deliveryZone);
                damagePlayerShip(player); // delivery zone doesn't move, therefore has no kinetic energy
            }
            else { // player collided with another object, possibly the world object
                damagePlayerShip(player);
            }
        }
        else if (event.getObjectB().getUserObject() instanceof Player player) {
            if (event.getObjectA().getUserObject() instanceof Crate crate) {
                processPlayerToCrateCollision(player, crate);
                damagePlayerShip(player, crate);
            }
            else if (event.getObjectA().getUserObject() instanceof DeliveryZone deliveryZone) {
                processPlayerToDeliveryZoneCollision(player, deliveryZone);
                damagePlayerShip(player); // delivery zone doesn't move, therefore has no kinetic energy
            }
            else { // player collided with another object, possibly the world object
                damagePlayerShip(player);
            }
        }
        else if (event.getObjectA().getUserObject() instanceof DeliveryZone deliveryZone) {
            if (event.getObjectB().getUserObject() instanceof Crate crate) {
                processCrateToDeliveryZoneCollision(crate, deliveryZone);
            }
        }
        else if (event.getObjectB().getUserObject() instanceof DeliveryZone deliveryZone) {
            if (event.getObjectA().getUserObject() instanceof Crate crate) {
                processCrateToDeliveryZoneCollision(crate, deliveryZone);
            }
        }
    }

    private void damagePlayerShip(Player player, Crate crate) {
        if (haveReachDamageBatchSizeLimit(player)) {
            return;
        }
        float playerVelocity = getVelocityAsScalar(player.getRigidBody());
        float crateVelocity = getVelocityAsScalar(crate.getRigidBody());
        if (playerVelocity < MIN_VELOCITY_FOR_DAMAGE && crateVelocity < MIN_VELOCITY_FOR_DAMAGE) {
            return;
        }
        float playerKineticEnergy = calculateKineticEnergy(player.getRigidBody().getMass(), playerVelocity);
        float crateKineticEnergy = calculateKineticEnergy(crate.getRigidBody().getMass(), crateVelocity);
        float kineticEnergy = playerKineticEnergy + crateKineticEnergy;
        if (kineticEnergy >= MIN_KINETIC_ENERGY_FOR_DAMAGE) {
            collateKineticEnergy(player, kineticEnergy);
        }
    }

    private void damagePlayerShip(Player player) {
        if (haveReachDamageBatchSizeLimit(player)) {
            return;
        }
        float playerVelocity = getVelocityAsScalar(player.getRigidBody());
        if (playerVelocity < MIN_VELOCITY_FOR_DAMAGE) {
            return;
        }
        float kineticEnergy = calculateKineticEnergy(player.getRigidBody().getMass(), playerVelocity);
        if (kineticEnergy >= MIN_KINETIC_ENERGY_FOR_DAMAGE) {
            collateKineticEnergy(player, kineticEnergy);
        }
    }

    private static float calculateKineticEnergy(float mass, float velocity) {
        return 0.5f * mass * (velocity * velocity);
    }

    private float getVelocityAsScalar(PhysicsRigidBody rigidBody) {
        rigidBody.getAngularVelocity(_velocity);
        float angularVelocity = _velocity.length();
        rigidBody.getLinearVelocity(_velocity);
        return angularVelocity + _velocity.length();
    }

    private void processPlayerToCrateCollision(Player player, Crate crate) {
        if (player.isIdle() && !player.isMoving() && !crate.isDroppedForDelivery()) {
            player.startCollectingCrate(crate);
        }
    }

    private void processPlayerToDeliveryZoneCollision(Player player, DeliveryZone deliveryZone) {
        if (player.isIdle() && !player.isMoving() && player.hasCollectedAtLeastOneCrate() && deliveryZone.isIdle()) {
            player.dropCrateForDelivery();
        }
    }

    private void processCrateToDeliveryZoneCollision(Crate crate, DeliveryZone deliveryZone) {
        if (deliveryZone.isIdle() && crate.isDroppedForDelivery()) {
            deliveryZone.startDeliveringCrate(crate);
        }
    }

    private boolean haveReachDamageBatchSizeLimit(Player player) {
        return _kineticEnergyCounts[player.getId()] >= KINETIC_ENERGY_BATCH_SIZE;
    }

    private void collateKineticEnergy(Player player, float kineticEnergy) {
        int index = _kineticEnergyCounts[player.getId()];
        _kineticEnergyBatches[player.getId()][index] = kineticEnergy;
        ++_kineticEnergyCounts[player.getId()];
    }

    private void applyPlayerDamage(Player player) {
        if (_kineticEnergyCounts[player.getId()] == 0) {
            return;
        }

        float kineticEnergy = 0;
        for (int i = 0; i < _kineticEnergyCounts[player.getId()]; ++i) {
            kineticEnergy += _kineticEnergyBatches[player.getId()][i];
        }
        kineticEnergy /= _kineticEnergyCounts[player.getId()];

        int damage = (int)(LUNAR_LANDER_DAMAGE_SCALE * kineticEnergy);
        //System.out.println("damage = " + damage);
        player.takeDamage(damage);

        _kineticEnergyCounts[player.getId()] = 0; // reset for next time
    }

    @Override
    public void gameOver() {
        // Nothing to do
    }

    @Override
    public void gameOverAborted() {
        // Nothing to do
    }

    @Override
    public void gameCompleted() {
        // Nothing to do
    }

    @Override
    public void gameCompletedAborted() {
        // Nothing to do
    }

    @Override
    public void startNextEpisode() throws Exception {
        // Nothing to do
    }

    @Override
    public void episodeIntroAborted() throws Exception {
        // Nothing to do
    }

    @Override
    public void missionIntroAborted() {
        // Nothing to do
    }

    @Override
    public void episodeOutroAborted() throws Exception {
        // Nothing to do
    }

    @Override
    public void episodeCompleted() {
        // Nothing to do
    }

    @Override
    public void startNextMission() throws Exception {
        // Nothing to do
    }

    @Override
    public void resumeMission() {
        // Nothing to do
    }

    @Override
    public void missionCompleted() {
        // Nothing to do
    }

    @Override
    public void playerDied(ICampaignView.WhichPlayer whichPlayer) {
        // Nothing to do
    }

    @Override
    public void playerShipSpawned(ICampaignView.WhichPlayer whichPlayer) {
        int player = whichPlayer == ICampaignView.WhichPlayer.PLAYER_1 ? 0 : 1;
        for (var observer : _observers) {
            observer.playerShipSpawned(player);
        }
    }

    @Override
    public void quitCampaign() {
        // Nothing to do
    }
}
