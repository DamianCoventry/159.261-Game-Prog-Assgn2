package com.lunargravity.engine.core;

import com.lunargravity.engine.graphics.ViewportConfig;

public interface IViewportSizeObserver {
    ViewportConfig onViewportSizeChanged(int viewport, ViewportConfig viewportConfig, int windowWidth, int windowHeight);
}
