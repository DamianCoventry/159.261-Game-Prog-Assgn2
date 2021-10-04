package com.lunargravity.world.model;

public interface ICrateObserver {
    void crateStartedDelivering(Crate crate, com.jme3.math.Vector3f playerPosition);
    void crateIsContinuouslyMoving(Crate crate);
}
