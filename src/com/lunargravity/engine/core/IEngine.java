package com.lunargravity.engine.core;

import com.jme3.bullet.PhysicsSpace;
import com.lunargravity.engine.graphics.GlRenderer;
import com.lunargravity.engine.timeouts.TimeoutManager;

public interface IEngine extends IManualFrameUpdater {
    void freeResources();
    void run();
    void exit();

    void setDefaultViewport();
    long getFps();
    long getFrameLengthMs();

    TimeoutManager getTimeoutManager();
    PhysicsSpace getPhysicsSpace();
    GlRenderer getRenderer();

    boolean isSoundEnabled();
    void enableSound();
    void disableSound();
    void setSoundVolume(int volume);
    int getSoundVolume();

    boolean isMusicEnabled();
    void enableMusic();
    void disableMusic();
    void setMusicVolume(int volume);
    int getMusicVolume();
}
