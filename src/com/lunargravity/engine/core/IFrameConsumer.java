package com.lunargravity.engine.core;

import org.joml.Matrix4f;

public interface IFrameConsumer {
    void onFrameBegin(long frameNo, long nowMs, double frameDelta);
    void onFrameEnd();
    void onFrameThink();
    void onFrameDraw3d(int viewport, Matrix4f projectionMatrix);
    void onFrameDraw2d(int viewport, Matrix4f projectionMatrix);
}
