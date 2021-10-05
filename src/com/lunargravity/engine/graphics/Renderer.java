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

import org.joml.Vector3f;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;

public class Renderer {
    private final GlDiffuseTextureProgram _diffuseTextureProgram;
    private final GLDirectionalLightProgram _directionalLightProgram;
    private final GLSpecularDirectionalLightProgram _specularDirectionalLightProgram;
    private final GlViewport _orthographicViewport;
    private GlViewport[] _perspectiveViewports;

    public Renderer(int windowWidth, int windowHeight, ViewportConfig[] perspectiveConfigs) throws IOException {
        _orthographicViewport = new GlViewport(ViewportConfig.createFullWindow(windowWidth, windowHeight));

        setPerspectiveViewports(perspectiveConfigs);

        _diffuseTextureProgram = new GlDiffuseTextureProgram();
        _directionalLightProgram = new GLDirectionalLightProgram();
        _directionalLightProgram.setLightDirection(new Vector3f(-7.0f, 2.5f, 4.0f).normalize());
        _specularDirectionalLightProgram = new GLSpecularDirectionalLightProgram();
        _specularDirectionalLightProgram.setLightDirection(new Vector3f(-7.0f, 2.5f, 4.0f).normalize());

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public GlDiffuseTextureProgram getDiffuseTextureProgram() {
        return _diffuseTextureProgram;
    }

    public GLDirectionalLightProgram getDirectionalLightProgram() {
        return _directionalLightProgram;
    }

    public GLSpecularDirectionalLightProgram getSpecularDirectionalLightProgram() {
        return _specularDirectionalLightProgram;
    }

    public void freeResources() {
        _diffuseTextureProgram.freeResources();
    }

    public void clearBuffers() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void setPerspectiveViewports(ViewportConfig[] perspectiveConfigs) {
        if (perspectiveConfigs == null || perspectiveConfigs.length == 0) {
            throw new IllegalArgumentException("perspectiveConfigs");
        }

        _perspectiveViewports = new GlViewport[perspectiveConfigs.length];
        for (int i = 0; i < perspectiveConfigs.length; ++i) {
            perspectiveConfigs[i]._viewportIndex = i;
            _perspectiveViewports[i] = new GlViewport(perspectiveConfigs[i]);
        }
    }

    public int getNumPerspectiveViewports() {
        return _perspectiveViewports.length;
    }

    public GlViewport getPerspectiveViewport(int i) {
        if (i >= 0 && i < _perspectiveViewports.length) {
            return _perspectiveViewports[i];
        }
        return null;
    }

    public GlViewport getOrthographicViewport() {
        return _orthographicViewport;
    }

    public void activateTextureImageUnit(int unit) {
        glActiveTexture(GL_TEXTURE0 + unit);
    }
}
