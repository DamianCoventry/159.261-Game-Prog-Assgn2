package com.lunargravity.engine.core;

import com.jme3.bullet.PhysicsSpace;
import com.lunargravity.engine.graphics.GlRenderer;
import com.lunargravity.engine.scene.ISceneBuilderObserver;
import com.lunargravity.engine.timeouts.TimeoutManager;

public interface IEngine extends IManualFrameUpdater {
    void freeResources();
    void run();

    void setDefaultViewport();
    long getFps();
    long getFrameLengthMs();

    TimeoutManager getTimeoutManager();
    PhysicsSpace getPhysicsSpace();
    GlRenderer getRenderer();
}
