package com.lunargravity.engine.graphics;

import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11C.glViewport;

public class GlViewport {
    private ViewportConfig _config;
    private Matrix4f _perspectiveMatrix;
    private Matrix4f _orthographicMatrix;

    public GlViewport(ViewportConfig config) {
        setConfig(config);
    }

    public Matrix4f getPerspectiveMatrix() {
        return _perspectiveMatrix;
    }

    public Matrix4f getOrthographicMatrix() {
        return _orthographicMatrix;
    }

    public void activate() {
        glViewport(_config._positionX, _config._positionY, _config._width, _config._height);
    }

    public void setConfig(ViewportConfig config) {
        _config = config;
        _perspectiveMatrix = createPerspectiveMatrix();
        _orthographicMatrix = createOrthographicMatrix();
    }
    public ViewportConfig getConfig() {
        return _config;
    }

    private Matrix4f createPerspectiveMatrix() {
        float aspectRatio = (float)_config._width / (float)_config._height;
        return new Matrix4f().perspective(
                (float)Math.toRadians(_config._verticalFovDegrees / 2.0),
                aspectRatio, _config._perspectiveNcp, _config._perspectiveFcp);
    }

    private Matrix4f createOrthographicMatrix() {
        return new Matrix4f().ortho2D(
                0.0f, (float)_config._width,
                0.0f, (float)_config._height);
    }
}
