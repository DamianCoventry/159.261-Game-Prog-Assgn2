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

package com.lunargravity.application;

import com.lunargravity.engine.core.IInputObserver;
import com.lunargravity.engine.core.IManualFrameUpdater;
import com.lunargravity.engine.graphics.Renderer;
import com.lunargravity.engine.graphics.ViewportConfig;
import org.joml.Matrix4f;

public interface IState extends IInputObserver {
    void begin() throws Exception;
    void end();
    void think();
    void draw3d(int viewport, Matrix4f projectionMatrix);
    void draw2d(Matrix4f projectionMatrix);

    IManualFrameUpdater getManualFrameUpdater();
    Renderer getRenderer();

    ViewportConfig onViewportSizeChanged(int viewport, ViewportConfig viewportConfig, int windowWidth, int windowHeight);
}
