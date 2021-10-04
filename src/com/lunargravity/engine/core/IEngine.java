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

package com.lunargravity.engine.core;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.lunargravity.engine.desktopwindow.GlfwWindow;
import com.lunargravity.engine.graphics.Renderer;
import com.lunargravity.engine.timeouts.TimeoutManager;

public interface IEngine extends IManualFrameUpdater {
    void freeResources();
    void run() throws Exception;
    void exit();

    void setDefaultViewport();
    long getFps();
    long getFrameLengthMs();
    long getNowMs();

    TimeoutManager getTimeoutManager();
    PhysicsSpace getPhysicsSpace();
    Renderer getRenderer();
    GlfwWindow.CursorPosition getMouseCursorPosition();
    float getDesktopWindowWidth();
    float getDesktopWindowHeight();

    void setPhysicsCollisionListener(PhysicsCollisionListener listener);

    boolean isSoundAvailable();
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
