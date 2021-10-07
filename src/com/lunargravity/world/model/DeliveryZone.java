package com.lunargravity.world.model;

import com.jme3.bullet.objects.PhysicsRigidBody;
import com.lunargravity.engine.timeouts.TimeoutManager;

public class DeliveryZone {
    public static final int CRATE_DELIVERY_TIME = 4000;

    private State _state;
    private PhysicsRigidBody _rigidBody;
    private int _deliveryTimeoutId;
    private final TimeoutManager _timeoutManager;
    private final IDeliveryZoneObserver _observer;

    public DeliveryZone(TimeoutManager timeoutManager, IDeliveryZoneObserver observer) {
        _timeoutManager = timeoutManager;
        _observer = observer;
        _state = State.IDLE;
        _deliveryTimeoutId = 0;
    }

    public enum State { IDLE, DELIVERING }
    public void setState(State state) {
        _state = state;
    }
    public boolean isIdle() {
        return _state == State.IDLE;
    }

    public void setRigidBody(PhysicsRigidBody rigidBody) {
        _rigidBody = rigidBody;
    }

    public void startDeliveringCrate(Crate crate) {
        if (_deliveryTimeoutId != 0 || _state != State.IDLE) {
            return;
        }

        _state = State.DELIVERING;
        crate.setState(Crate.State.DELIVERING);

        _deliveryTimeoutId = _timeoutManager.addTimeout(CRATE_DELIVERY_TIME, callCount -> {
            _state = State.IDLE;
            crate.setState(Crate.State.DELIVERED);
            _deliveryTimeoutId = 0;

            if (_observer != null) {
                _observer.crateDeliveryCompleted(crate);
            }
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }

    public void removeTimeouts(TimeoutManager timeoutManager) {
        if (_deliveryTimeoutId != 0) {
            timeoutManager.removeTimeout(_deliveryTimeoutId);
            _deliveryTimeoutId = 0;
        }
    }
}
