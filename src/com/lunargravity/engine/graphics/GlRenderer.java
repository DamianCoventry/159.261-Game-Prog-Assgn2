package com.lunargravity.engine.graphics;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;

public class GlRenderer {
    private final GlDiffuseTextureProgram _diffuseTextureProgram;
    private GlViewport[] _viewports;

    public GlRenderer(ViewportConfig[] viewportConfigs) throws IOException {
        setViewports(viewportConfigs);

        _diffuseTextureProgram = new GlDiffuseTextureProgram();

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public GlDiffuseTextureProgram getDiffuseTextureProgram() {
        return _diffuseTextureProgram;
    }

    public void freeResources() {
        _diffuseTextureProgram.freeResources();
    }

    public void clearBuffers() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void setViewports(ViewportConfig[] viewportConfigs) {
        if (viewportConfigs == null || viewportConfigs.length == 0) {
            throw new IllegalArgumentException("viewportConfigs");
        }

        _viewports = new GlViewport[viewportConfigs.length];
        for (int i = 0; i < viewportConfigs.length; ++i) {
            viewportConfigs[i]._viewportIndex = i;
            _viewports[i] = new GlViewport(viewportConfigs[i]);
        }
    }

    public int getNumViewports() {
        return _viewports.length;
    }

    public GlViewport getViewport(int i) {
        if (i >= 0 && i < _viewports.length) {
            return _viewports[i];
        }
        return null;
    }

    public void activateTextureImageUnit(int unit) {
        glActiveTexture(GL_TEXTURE0 + unit);
    }
}
