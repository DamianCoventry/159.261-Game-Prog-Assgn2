package com.lunargravity.engine.graphics;

import static org.lwjgl.opengl.GL11.*;

public class GlRenderer {
    private GlViewport[] _viewports;

    public GlRenderer(GlViewportConfig[] viewportConfigs) {
        setViewports(viewportConfigs);

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void clearBuffers() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void setViewports(GlViewportConfig[] viewportConfigs) {
        if (viewportConfigs == null || viewportConfigs.length == 0) {
            throw new IllegalArgumentException("viewportConfigs");
        }

        _viewports = new GlViewport[viewportConfigs.length];
        for (int i = 0; i < viewportConfigs.length; ++i) {
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
}
