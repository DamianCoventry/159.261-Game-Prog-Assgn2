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

import com.lunargravity.engine.graphics.GlRenderer;
import com.lunargravity.engine.graphics.NumberFont;

import java.io.IOException;

public class LargeNumberFont extends NumberFont {
    public static final float FRAME_WIDTH = 26.0f;
    public static final float FRAME_HEIGHT = 37.0f;

    public LargeNumberFont(GlRenderer renderer) throws IOException {
        super(renderer, FRAME_WIDTH, FRAME_HEIGHT, "images/LargeNumbers.png");
    }
}
