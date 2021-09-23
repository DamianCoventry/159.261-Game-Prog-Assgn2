package com.lunargravity.engine.core;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public interface IManualFrameUpdater {
    Vector2f[] getViewportSizes();
    void prepareNewFrame();
    void submitFrame();
    Matrix4f getPerspectiveProjectionMatrix();
    Matrix4f getOrthographicProjectionMatrix();
}
