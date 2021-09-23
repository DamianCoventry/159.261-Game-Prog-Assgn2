package com.lunargravity.engine.core;

import org.joml.Matrix4f;

import java.io.IOException;

public interface IFrameConsumer {
    void onFrameBegin(long frameNo, long nowMs, double frameDelta);
    void onFrameEnd() throws IOException, InterruptedException;
    void onFrameThink();
    void onFrameDraw3d(int viewport, Matrix4f projectionMatrix);
    void onFrameDraw2d(int viewport, Matrix4f projectionMatrix);
}
