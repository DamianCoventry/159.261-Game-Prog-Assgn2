package com.lunargravity.engine.core;

import org.joml.Matrix4f;

public interface IManualFrameUpdater {
    void prepareNewFrame();
    void submitFrame();
    Matrix4f getPerspectiveProjectionMatrix();
    Matrix4f getOrthographicProjectionMatrix();
}
