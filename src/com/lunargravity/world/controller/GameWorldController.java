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
        ICampaignControllerObserver,
        IPlayerShotObserver
{
    public static final float LUNAR_LANDER_MASS = 2.0f;
    public static final float MIN_IMPULSE_TO_CAUSE_DAMAGE = 5.0f;
    public static final float PLAYER_SHIP_DAMAGE_SCALE = 0.75f;

    private final ArrayList<IGameWorldControllerObserver> _observers;
    private final ICampaignController _logicController;
    private final IGameWorldModel _model;
    private final IEngine _engine;
    private final Vector3f _impactPoint;

    public GameWorldController(IEngine engine, IController controller, IGameWorldModel model) {
        _engine = engine;
        _engine.setPhysicsCollisionListener(this::processPhysicsCollisionEvent);
        _logicController = (ICampaignController)controller;
        _observers = new ArrayList<>();
        _impactPoint = new Vector3f();

        _model = model;
        _model.setPlayerObserver(this);
        _model.setCrateObserver(this);
        _model.setDeliveryZoneObserver(this);
        _model.setPlayerShotObserver(this);
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
            if (player == null) {
                continue;
            }
            player.applyInputCommands();
            player.stopCollectingCrateIfMoving();
        }

        _model.updateCrateMovingStates(nowMs);
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
    public void crateCollectingStarted(Player player, Crate crate) {
        for (var observer : _observers) {
            observer.crateCollectingStarted(player, crate);
        }
    }

    @Override
    public void crateCollectionCompleted(Crate crate) {
        for (var observer : _observers) {
            observer.crateCollectionCompleted(crate);
        }
    }

    @Override
    public void crateCollectionAborted(Player player) {
        for (var observer : _observers) {
            observer.crateCollectionAborted(player);
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
    public void playerFiredWeapon(int id) {
        for (var observer : _observers) {
            observer.playerFiredWeapon(id);
        }
    }

    @Override
    public void playerWeaponCooledDown(int id) {
        for (var observer : _observers) {
            observer.playerWeaponCooledDown(id);
        }
    }

    @Override
    public void crateDroppedForDelivery(Crate crate, com.jme3.math.Vector3f playerPosition) {
        for (var observer : _observers) {
            observer.crateDroppedForDelivery(crate, playerPosition);
        }
    }

    @Override
    public void crateIsContinuouslyMoving(Crate crate) {
        for (var observer : _observers) {
            observer.respawnCrateAtStartPosition(crate);
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
            }
            else if (event.getObjectB().getUserObject() instanceof DeliveryZone deliveryZone) {
                processPlayerToDeliveryZoneCollision(player, deliveryZone);
            }
            else if (event.getObjectB().getUserObject() instanceof PlayerShot playerShot) {
                playerShot.explode();
            }
            damagePlayerShip(player, event.getAppliedImpulse(), event.getPositionWorldOnA(_impactPoint));
        }
        else if (event.getObjectB().getUserObject() instanceof Player player) {
            if (event.getObjectA().getUserObject() instanceof Crate crate) {
                processPlayerToCrateCollision(player, crate);
            }
            else if (event.getObjectA().getUserObject() instanceof DeliveryZone deliveryZone) {
                processPlayerToDeliveryZoneCollision(player, deliveryZone);
            }
            else if (event.getObjectA().getUserObject() instanceof PlayerShot playerShot) {
                playerShot.explode();
            }
            damagePlayerShip(player, event.getAppliedImpulse(), event.getPositionWorldOnB(_impactPoint));
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
        else if (event.getObjectA().getUserObject() instanceof PlayerShot playerShot) {
            playerShot.explode();
        }
        else if (event.getObjectB().getUserObject() instanceof PlayerShot playerShot) {
            playerShot.explode();
        }
    }

    private void damagePlayerShip(Player player, float appliedImpulse, Vector3f impactPoint) {
        if (appliedImpulse >= MIN_IMPULSE_TO_CAUSE_DAMAGE) {
            player.takeDamage((int)(appliedImpulse * PLAYER_SHIP_DAMAGE_SCALE));
        }
        for (var observer : _observers) {
            observer.playerShipCollided(player, appliedImpulse, impactPoint);
        }
    }

    private void processPlayerToCrateCollision(Player player, Crate crate) {
        if (player.isIdle() && !player.isMoving() && crate.isIdle()) {
            player.startCollectingCrate(crate);
        }
    }

    private void processPlayerToDeliveryZoneCollision(Player player, DeliveryZone deliveryZone) {
        if (player.isIdle() && !player.isMoving() && !player.getCollectedCrates().isEmpty() && deliveryZone.isIdle()) {
            player.dropCrateForDelivery();
        }
    }

    private void processCrateToDeliveryZoneCollision(Crate crate, DeliveryZone deliveryZone) {
        if (deliveryZone.isIdle() && crate.isDroppedForDelivery()) {
            deliveryZone.startDeliveringCrate(crate);
        }
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
        for (var observer : _observers) {
            observer.episodeCompleted();
        }
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

    @Override
    public void playerShotExploded(PlayerShot playerShot) {
        for (var observer : _observers) {
            observer.playerShotExploded(playerShot);
        }
    }
}
