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

import org.joml.Matrix4f;
import org.joml.Vector2f;

public interface IManualFrameUpdater {
    void prepareNewFrame();
    void submitFrame();
    Vector2f getOrthographicViewportSize();
    Vector2f[] getPerspectiveViewportSizes();
    Matrix4f getPerspectiveProjectionMatrix();
    Matrix4f getOrthographicProjectionMatrix();
}
