package com.lunargravity.world.model;

import com.jme3.bullet.objects.PhysicsRigidBody;
import com.lunargravity.engine.timeouts.TimeoutManager;

public class PlayerShot {
    private static final int EXPLODE_TIMEOUT = 3000;

    private final TimeoutManager _timeoutManager;
    private final Player _player;
    private final PhysicsRigidBody _rigidBody;
    private final IPlayerShotObserver _observer;
    private int _timeoutId;

    public PlayerShot(Player player, PhysicsRigidBody rigidBody, IPlayerShotObserver observer, TimeoutManager timeoutManager) {
        _timeoutManager = timeoutManager;
        _player = player;
        _rigidBody = rigidBody;
        _observer = observer;
        _timeoutId = timeoutManager.addTimeout(EXPLODE_TIMEOUT, callCount -> {
            _observer.playerShotExploded(this);
            _timeoutId = 0;
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }

    public Player getPlayer() {
        return _player;
    }

    public PhysicsRigidBody getRigidBody() {
        return _rigidBody;
    }

    public void explode() {
        _observer.playerShotExploded(this);
        removeTimeouts(_timeoutManager);
    }

    public void removeTimeouts(TimeoutManager timeoutManager) {
        if (_timeoutId != 0) {
            timeoutManager.removeTimeout(_timeoutId);
            _timeoutId = 0;
        }
    }
}
