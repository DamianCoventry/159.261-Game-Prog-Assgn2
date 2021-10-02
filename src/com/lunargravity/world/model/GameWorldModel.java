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
import com.lunargravity.engine.timeouts.TimeoutManager;

import java.util.ArrayList;

public class GameWorldModel implements IGameWorldModel {
    private final int _numPlayers;
    private final Player[] _players;
    private final ArrayList<Crate> _crates;
    private final ArrayList<DeliveryZone> _deliveryZones;
    private final TimeoutManager _timeoutManager;
    
    private ICrateObserver _crateObserver;
    private IDeliveryZoneObserver _deliveryZoneObserver;

    public GameWorldModel(TimeoutManager timeoutManager, int numPlayers) {
        _timeoutManager = timeoutManager;
        _numPlayers = numPlayers;
        _players = new Player[2];
        _players[0] = new Player(0, _timeoutManager);
        _players[1] = new Player(1, _timeoutManager);
        _crates = new ArrayList<>();
        _deliveryZones = new ArrayList<>();
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
    public void clearCrates() {
        _crates.clear();
    }

    @Override
    public void addCrate(PhysicsRigidBody rigidBody) {
        if (_crateObserver == null) {
            throw new RuntimeException("No crate observer set");
        }
        Crate crate = new Crate(_timeoutManager, _crateObserver);
        crate.setRigidBody(rigidBody);
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
    public Player getPlayerState(int i) {
        if (i == 0 || i == 1) {
            return _players[i];
        }
        return null;
    }
}
