package com.lunargravity.world.model;

import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector3f;
import com.lunargravity.engine.timeouts.TimeoutManager;

public class Crate {
    private static final int DROPPED_FOR_DELIVERY_EXPIRY = 3000;

    private final ICrateObserver _observer;
    private State _state;
    private PhysicsRigidBody _rigidBody;
    private final TimeoutManager _timeoutManager;
    private int _timeoutId;

    public Crate(TimeoutManager timeoutManager, ICrateObserver observer) {
        _state = State.IDLE;
        _timeoutManager = timeoutManager;
        _observer = observer;
        _timeoutId = 0;
    }

    public enum State { IDLE, COLLECTED, DROPPED_FOR_DELIVERY, DELIVERING, DELIVERED }
    public void setState(State state) {
        _state = state;
    }

    public boolean isDelivered() {
        return _state == State.DELIVERED;
    }

    public boolean isDroppedForDelivery() {
        return _state == State.DROPPED_FOR_DELIVERY;
    }

    public void setRigidBody(PhysicsRigidBody rigidBody) {
        _rigidBody = rigidBody;
    }
    public PhysicsRigidBody getRigidBody() {
        return _rigidBody;
    }

    public void dropForDelivery(Vector3f playerPosition) {
        if (_state != State.COLLECTED) {
            return;
        }

        _state = State.DROPPED_FOR_DELIVERY;
        _observer.crateStartedDelivering(this, playerPosition);

        _timeoutId = _timeoutManager.addTimeout(DROPPED_FOR_DELIVERY_EXPIRY, callCount -> {
            if (_state == State.DROPPED_FOR_DELIVERY) {
                _state = State.IDLE;
            }
            _timeoutId = 0;
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }

    public void removeTimeouts(TimeoutManager timeoutManager) {
        if (_timeoutId != 0) {
            timeoutManager.removeTimeout(_timeoutId);
            _timeoutId = 0;
        }
    }
}
