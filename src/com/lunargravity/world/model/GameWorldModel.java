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

package com.lunargravity.world.model;

import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector3f;
import com.lunargravity.campaign.model.ICampaignModel;
import com.lunargravity.engine.timeouts.TimeoutManager;

import java.util.ArrayList;

public class GameWorldModel implements IGameWorldModel {
    private final int _numPlayers;
    private final Player[] _players;
    private final ArrayList<Crate> _crates;
    private final ArrayList<DeliveryZone> _deliveryZones;
    private final ArrayList<PlayerShot> _playerShots;
    private final TimeoutManager _timeoutManager;
    private final ICampaignModel _campaignModel;

    private ICrateObserver _crateObserver;
    private IDeliveryZoneObserver _deliveryZoneObserver;
    private IPlayerShotObserver _playerShotObserver;

    public GameWorldModel(TimeoutManager timeoutManager, int numPlayers, ICampaignModel campaignModel) {
        _timeoutManager = timeoutManager;
        _campaignModel = campaignModel;
        _numPlayers = numPlayers;
        _players = new Player[2];
        _players[0] = new Player(0, _timeoutManager);
        _players[1] = new Player(1, _timeoutManager);
        _crates = new ArrayList<>();
        _deliveryZones = new ArrayList<>();
        _playerShots = new ArrayList<>();
    }

    @Override
    public String modelToJson() {
        // TODO
        return null;
    }

    @Override
    public void modelFromJson(String json) {
        // TODO
    }

    @Override
    public void removeTimeouts(TimeoutManager timeoutManager) {
        for (var a : _players) {
            a.removeTimeouts(timeoutManager);
        }
        for (var a : _deliveryZones) {
            a.removeTimeouts(timeoutManager);
        }
        for (var a : _crates) {
            a.removeTimeouts(timeoutManager);
        }
        for (var a : _playerShots) {
            a.removeTimeouts(timeoutManager);
        }
    }

    @Override
    public int getNumPlayers() {
        return _numPlayers;
    }

    @Override
    public void setPlayerObserver(IPlayerObserver observer) {
        for (var player : _players) {
            player.setObserver(observer);
        }
    }

    @Override
    public void resetPlayers() {
        for (var player : _players) {
            player.reset();
        }
    }

    @Override
    public void setCrateObserver(ICrateObserver observer) {
        _crateObserver = observer;
    }

    @Override
    public boolean areAllCratesDelivered() {
        for (var crate : _crates) {
            if (!crate.isDelivered()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void updateCrateMovingStates(long nowMs) {
        for (var crate : _crates) {
            crate.updateMovingState(nowMs);
        }
    }

    @Override
    public int getNumCratesRemaining() {
        int count = 0;
        for (var crate : _crates) {
            if (crate.isIdle()) {
                ++count;
            }
        }
        return count;
    }

    @Override
    public int getNumCratesCollected() {
        int count = 0;
        for (var crate : _crates) {
            if (crate.isCollected()) {
                ++count;
            }
        }
        return count;
    }

    @Override
    public int getNumCratesDelivered() {
        int count = 0;
        for (var crate : _crates) {
            if (crate.isDelivered()) {
                ++count;
            }
        }
        return count;
    }

    @Override
    public void clearCrates() {
        _crates.clear();
    }

    @Override
    public void addCrate(PhysicsRigidBody rigidBody, Vector3f startPosition) {
        if (_crateObserver == null) {
            throw new RuntimeException("No crate observer set");
        }
        Crate crate = new Crate(_timeoutManager, _crateObserver);
        crate.setRigidBody(rigidBody, startPosition);
        _crates.add(crate);
        rigidBody.setUserObject(crate);
    }

    @Override
    public void clearDeliveryZones() {
        _deliveryZones.clear();
    }

    @Override
    public void addDeliveryZone(PhysicsRigidBody rigidBody) {
        if (_deliveryZoneObserver == null) {
            throw new RuntimeException("No delivery zone observer set");
        }
        DeliveryZone deliveryZone = new DeliveryZone(_timeoutManager, _deliveryZoneObserver);
        deliveryZone.setRigidBody(rigidBody);
        _deliveryZones.add(deliveryZone);
        rigidBody.setUserObject(deliveryZone);
    }

    @Override
    public void setDeliveryZoneObserver(IDeliveryZoneObserver observer) {
        _deliveryZoneObserver = observer;
    }

    @Override
    public void clearPlayerShots() {
        _playerShots.clear();
    }

    @Override
    public void addPlayerShot(Player player, PhysicsRigidBody rigidBody) {
        if (_playerShotObserver == null) {
            throw new RuntimeException("No player shot observer set");
        }
        PlayerShot playerShot = new PlayerShot(player, rigidBody, _playerShotObserver, _timeoutManager);
        _playerShots.add(playerShot);
        rigidBody.setUserObject(playerShot);
    }

    @Override
    public void setPlayerShotObserver(IPlayerShotObserver observer) {
        _playerShotObserver = observer;
    }

    @Override
    public long getEpisode() {
        return _campaignModel.getEpisode();
    }

    @Override
    public long getMission() {
        return _campaignModel.getMission();
    }

    @Override
    public long getShipsRemaining(int playerId) {
        return _campaignModel.getShipsRemaining(playerId);
    }

    @Override
    public Player getPlayerState(int i) {
        if (i == 0 || i == 1) {
            return _players[i];
        }
        return null;
    }
}
