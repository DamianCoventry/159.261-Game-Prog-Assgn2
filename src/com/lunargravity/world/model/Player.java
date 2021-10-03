package com.lunargravity.world.model;

import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.lunargravity.engine.timeouts.TimeoutManager;

import java.util.ArrayList;

public class Player {
    private static final int CRATE_COLLECTION_TIME = 1000;
    private static final int CRATE_DELIVERY_SPEED = 1000; // 1 second must pass between crate deliveries
    private static final int EXPLODING_TIME = 2000;
    private static final int WEAPON_COOL_DOWN_TIME = 500;
    private static final float LUNAR_LANDER_THRUST = 45.0f;
    private static final int MAX_HIT_POINTS = 100;
    private static final int MAX_FUEL = 100;
    private static final float VELOCITY_EPSILON = 0.1f;

    private static final Vector3f LUNAR_LANDER_KICK = new Vector3f(0.0f, 1000.0f, 0.0f);
    private static final Vector3f LUNAR_LANDER_ROTATE_CCW = new Vector3f(0.0f, 0.0f, 0.15f);
    private static final Vector3f LUNAR_LANDER_ROTATE_CW = new Vector3f(0.0f, 0.0f, -0.15f);

    private final int _id;
    private final TimeoutManager _timeoutManager;
    private final Vector3f _thrustForce;
    private final Vector3f _angularVelocity;
    private final Vector3f _linearVelocity;
    private final com.jme3.math.Vector3f _position;
    private final com.jme3.math.Matrix3f _rotationMatrix;
    private final com.jme3.math.Vector3f _upVector;

    private int _collectionTimeoutId;
    private int _droppingForDeliveryTimeoutId;
    private int _explodingTimeoutId;
    private int _weaponCoolDownTimeoutId;

    private PhysicsRigidBody _rigidBody;
    private IPlayerObserver _observer;

    private boolean _thrustActive;
    private boolean _rotateCcwActive;
    private boolean _rotateCwActive;
    private boolean _shootActive;

    private State _state;
    private WeaponState _weaponState;
    private int _hitPoints;
    private int _fuel;

    private final ArrayList<Crate> _collectedCrates;

    public enum State { IDLE, COLLECTING, DROPPING_FOR_DELIVERY, EXPLODING, DEAD }
    private enum WeaponState { IDLE, COOLING_DOWN }

    public Player(int id, TimeoutManager timeoutManager) {
        _id = id;
        _timeoutManager = timeoutManager;
        _rigidBody = null;
        _rotationMatrix = new Matrix3f();
        _upVector = new Vector3f();
        _position = new Vector3f();
        _thrustForce = new Vector3f();
        _angularVelocity = new Vector3f();
        _linearVelocity = new Vector3f();
        _collectedCrates = new ArrayList<>();

        _state = State.IDLE;
        _weaponState = WeaponState.IDLE;
        _thrustActive = _rotateCcwActive = _rotateCwActive = _shootActive = false;
        _hitPoints = MAX_HIT_POINTS;
        _fuel = MAX_FUEL;

        _collectionTimeoutId = 0;
        _droppingForDeliveryTimeoutId = 0;
        _explodingTimeoutId = 0;
        _weaponCoolDownTimeoutId = 0;
    }

    public int getId() {
        return _id;
    }

    public int getHitPoints() {
        return _hitPoints;
    }

    public int getFuel() {
        return _fuel;
    }

    public boolean isExplodingOrDead() {
        return _state == State.EXPLODING || _state == State.DEAD;
    }

    public boolean isIdle() {
        return _state == State.IDLE;
    }

    public void reset() {
        _thrustActive = _rotateCcwActive = _rotateCwActive = _shootActive = false;
        _hitPoints = MAX_HIT_POINTS;
        _state = State.IDLE;
        _weaponState = WeaponState.IDLE;
    }

    public void setObserver(IPlayerObserver observer) {
        _observer = observer;
    }

    public void setRigidBody(PhysicsRigidBody rigidBody) {
        _rigidBody = rigidBody;
    }
    public PhysicsRigidBody getRigidBody() {
        return _rigidBody;
    }

    public Vector3f getThrustForce() {
        return _thrustForce;
    }

    public void setThrustActive(boolean thrustActive) {
        _thrustActive = thrustActive;
    }
    public boolean isThrustActive() {
        return _thrustActive;
    }

    public void setRotateCcwActive(boolean rotateCcwActive) {
        _rotateCcwActive = rotateCcwActive;
    }
    public boolean isRotateCcwActive() {
        return _rotateCcwActive;
    }

    public void setRotateCwActive(boolean rotateCwActive) {
        _rotateCwActive = rotateCwActive;
    }
    public boolean isRotateCwActive() {
        return _rotateCwActive;
    }

    public void setShootActive(boolean shootActive) {
        _shootActive = shootActive;
    }
    public boolean isShootActive() {
        return _shootActive;
    }

    public boolean hasCollectedAtLeastOneCrate() {
        return !_collectedCrates.isEmpty();
    }

    public void startCollectingCrate(Crate crate) {
        if (_collectionTimeoutId != 0 || _state != State.IDLE) {
            return;
        }
        _state = State.COLLECTING;
        _collectionTimeoutId = _timeoutManager.addTimeout(CRATE_COLLECTION_TIME, callCount -> {
            _collectedCrates.add(crate);
            crate.setState(Crate.State.COLLECTED);

            _state = State.IDLE;
            _collectionTimeoutId = 0;

            if (_observer != null) {
                _observer.crateCollectionCompleted(crate);
            }
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }

    public void stopCollectingCrateIfMoving() {
        if (isMoving() && _collectionTimeoutId != 0) {
            removeTimeouts(_timeoutManager);
            if (_observer != null) {
                _observer.crateCollectionAborted();
            }
        }
    }

    public ArrayList<Crate> getCollectedCrates() {
        return _collectedCrates;
    }

    public void clearCollectedCrates() {
        _collectedCrates.clear();
    }

    public void dropCrateForDelivery() {
        if (_collectedCrates.isEmpty() || _droppingForDeliveryTimeoutId != 0 || _state != State.IDLE) {
            return;
        }

        Crate crate = _collectedCrates.get(0);
        _collectedCrates.remove(0);

        crate.dropForDelivery(_rigidBody.getPhysicsLocation(_position));

        // Drop 1 crate per second, no faster.
        _state = State.DROPPING_FOR_DELIVERY;
        _droppingForDeliveryTimeoutId = _timeoutManager.addTimeout(CRATE_DELIVERY_SPEED, callCount -> {
            _state = State.IDLE;
            _droppingForDeliveryTimeoutId = 0;
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }

    public void removeTimeouts(TimeoutManager timeoutManager) {
        if (_collectionTimeoutId != 0) {
            timeoutManager.removeTimeout(_collectionTimeoutId);
            _collectionTimeoutId = 0;
        }
        if (_droppingForDeliveryTimeoutId != 0) {
            timeoutManager.removeTimeout(_droppingForDeliveryTimeoutId);
            _droppingForDeliveryTimeoutId = 0;
        }
        if (_explodingTimeoutId != 0) {
            timeoutManager.removeTimeout(_explodingTimeoutId);
            _explodingTimeoutId = 0;
        }
        if (_weaponCoolDownTimeoutId != 0) {
            timeoutManager.removeTimeout(_weaponCoolDownTimeoutId);
            _weaponCoolDownTimeoutId = 0;
        }
        _state = State.IDLE;
        _weaponState = WeaponState.IDLE;
    }

    public void applyInputCommands() {
        if (_rigidBody == null) {
            return;
        }
        if (_thrustActive) {
            thrust();
        }
        if (_rotateCwActive) {
            _rigidBody.applyTorqueImpulse(LUNAR_LANDER_ROTATE_CW);
        }
        if (_rotateCcwActive) {
            _rigidBody.applyTorqueImpulse(LUNAR_LANDER_ROTATE_CCW);
        }
        if (_shootActive) {
            shoot();
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

    public void kick() {
        if (!isMoving() && _rigidBody != null) {
            _rigidBody.applyCentralForce(LUNAR_LANDER_KICK);
        }
    }

    public void takeDamage(int hitPoints) {
        _hitPoints -= hitPoints;
        if (_hitPoints >= 0) {
            _observer.playerShipTookDamage(_id, hitPoints, _hitPoints);
            return;
        }

        _hitPoints = 0;
        _state = State.EXPLODING;
        _observer.playerShipExploding(_id);
        _collectedCrates.clear();

        _explodingTimeoutId = _timeoutManager.addTimeout(EXPLODING_TIME, callCount -> {
            _state = State.DEAD;
            _observer.playerShipDead(_id);
            _explodingTimeoutId = 0;
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }

    public Vector3f calculateAndGetUpVector() {
        calculateUpVector();
        return _upVector;
    }

    private void calculateUpVector() {
        _rigidBody.getPhysicsRotationMatrix(_rotationMatrix);
        _upVector.x = _rotationMatrix.get(0, 1);
        _upVector.y = _rotationMatrix.get(1, 1);
        _upVector.z = _rotationMatrix.get(2, 1);
    }

    private void thrust() {
        calculateUpVector();
        _thrustForce.set(_upVector).multLocal(LUNAR_LANDER_THRUST);
        _rigidBody.applyCentralForce(_thrustForce);
    }

    private void shoot() {
        if (_weaponState != WeaponState.IDLE) {
            return;
        }
        _observer.playerFiredWeapon(_id);
        _weaponState = WeaponState.COOLING_DOWN;
        _weaponCoolDownTimeoutId = _timeoutManager.addTimeout(WEAPON_COOL_DOWN_TIME, callCount -> {
            _weaponState = WeaponState.IDLE;
            _observer.playerWeaponCooledDown(_id);
            _weaponCoolDownTimeoutId = 0;
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }
}
