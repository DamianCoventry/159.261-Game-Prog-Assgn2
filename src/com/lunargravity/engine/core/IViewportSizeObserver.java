package com.lunargravity.engine.core;

import com.lunargravity.engine.graphics.GlViewportConfig;

public interface IViewportSizeObserver {
    GlViewportConfig onViewportSizeChanged(int viewport, GlViewportConfig currentConfig, int windowWidth, int windowHeight);
}
