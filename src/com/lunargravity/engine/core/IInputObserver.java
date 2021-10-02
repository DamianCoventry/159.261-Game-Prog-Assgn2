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

import java.io.IOException;

public interface IInputObserver {
    void keyboardKeyEvent(int key, int scancode, int action, int mods);
    void mouseButtonEvent(int button, int action, int mods) throws Exception;
    void mouseCursorMovedEvent(double xPos, double yPos);
    void mouseWheelScrolledEvent(double xOffset, double yOffset);
}
