package com.lunargravity.engine.core;

import com.lunargravity.engine.scene.ISceneLoadObserver;

public interface IEngine {
    void freeResources();
    void run();
    void setDefaultViewport();
    long getFps();
    long getFrameLengthMs();
    void loadScene(String fileName, ISceneLoadObserver loadProgressObserver);
}
