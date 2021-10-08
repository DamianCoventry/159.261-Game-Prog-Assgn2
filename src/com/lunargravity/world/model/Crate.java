package com.lunargravity.world.model;

import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector3f;
import com.lunargravity.engine.timeouts.TimeoutManager;

public class Crate {
    private static final int DROPPED_FOR_DELIVERY_EXPIRY = 3000;
    private static final int CONTINUOUS_FALLING_TIMEOUT = 5000;
    private static final float VELOCITY_EPSILON = 0.1f;
    private static final Vector3f ZERO_VELOCITY = new Vector3f(0, 0, 0);

    private final ICrateObserver _observer;
    private State _state;
    private PhysicsRigidBody _rigidBody;
    private Vector3f _startPosition;
    private final Vector3f _angularVelocity;
    private final Vector3f _linearVelocity;
    private final TimeoutManager _timeoutManager;
    private int _droppedForDeliveryTimeoutId;
    private long _lastTimeWasAtRest;

    public Crate(TimeoutManager timeoutManager, ICrateObserver observer) {
        _state = State.IDLE;
        _timeoutManager = timeoutManager;
        _observer = observer;
        _startPosition = null;
        _angularVelocity = new Vector3f();
        _linearVelocity = new Vector3f();
        _droppedForDeliveryTimeoutId = 0;
        _lastTimeWasAtRest = 0;
    }

    public enum State { IDLE, COLLECTED, DROPPED_FOR_DELIVERY, DELIVERING, DELIVERED }
    public void setState(State state) {
        _state = state;
        removeTimeouts(_timeoutManager);
    }

    public boolean isIdle() {
        return _state == State.IDLE;
    }

    public boolean isCollected() {
        return _state == State.COLLECTED;
    }

    public boolean isDelivered() {
        return _state == State.DELIVERED;
    }

    public boolean isDelivering() {
        return _state == State.DELIVERING;
    }

    public boolean isDroppedForDelivery() {
        return _state == State.DROPPED_FOR_DELIVERY;
    }

    public void setRigidBody(PhysicsRigidBody rigidBody, com.jme3.math.Vector3f startPosition) {
        _rigidBody = rigidBody;
        _startPosition = startPosition;
    }
    public PhysicsRigidBody getRigidBody() {
        return _rigidBody;
    }

    public void updateMovingState(long nowMs) {
        boolean moving = isMoving();
        if (!moving) {
            _lastTimeWasAtRest = nowMs;
            return;
        }

        if (nowMs - _lastTimeWasAtRest >= CONTINUOUS_FALLING_TIMEOUT) {
            _lastTimeWasAtRest = nowMs;
            _observer.crateIsContinuouslyMoving(this);
        }
    }

    public void respawnAtStartPosition() {
        if (_startPosition == null) {
            return;
        }
        _rigidBody.clearForces();
        _rigidBody.setLinearVelocity(ZERO_VELOCITY);
        _rigidBody.setAngularVelocity(ZERO_VELOCITY);
        _rigidBody.setPhysicsLocation(_startPosition);
    }

    public void dropForDelivery(Vector3f playerPosition) {
        if (_state != State.COLLECTED) {
            return;
        }

        _state = State.DROPPED_FOR_DELIVERY;
        _observer.crateDroppedForDelivery(this, playerPosition);

        removeTimeouts(_timeoutManager);
        _droppedForDeliveryTimeoutId = _timeoutManager.addTimeout(DROPPED_FOR_DELIVERY_EXPIRY, callCount -> {
            if (_state == State.DROPPED_FOR_DELIVERY) {
                _state = State.IDLE;
            }
            _droppedForDeliveryTimeoutId = 0;
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }

    public void removeTimeouts(TimeoutManager timeoutManager) {
        if (_droppedForDeliveryTimeoutId != 0) {
            timeoutManager.removeTimeout(_droppedForDeliveryTimeoutId);
            _droppedForDeliveryTimeoutId = 0;
        }
    }

    public boolean isMoving() {
        if (_rigidBody == null) {
            return false;
        }
        _rigidBody.getAngularVelocity(_angularVelocity);
        if (_angularVelocity.length() > VELOCITY_EPSILON) {
            return true;
        }
        _rigidBody.getLinearVelocity(_linearVelocity);
        return _linearVelocity.length() > VELOCITY_EPSILON;
    }
}
