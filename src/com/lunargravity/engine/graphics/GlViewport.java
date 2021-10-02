//
// Lunar Gravity
//
// This game is based upon the Amiga video game Gravity Force that was
// released in 1989 by Stephan Wenzler
//
// https://www.mobygames.com/game/gravity-force
// https://www.youtube.com/watch?v=m9mFtCvnko8
//
// This implementation is Copyright (c) 2021, Damian Coventry
// All rights reserved
// Written for Massey University course 159.261 Game Programming (Assignment 2)
//

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

    public boolean containsPoint(float x, float y) {
        return (x >= _config._positionX) && (x <= _config._positionX + _config._width) &&
               (y >= _config._positionY) && (x <= _config._positionY + _config._height);
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
