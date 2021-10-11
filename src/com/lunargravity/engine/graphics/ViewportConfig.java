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

package com.lunargravity.engine.graphics;

public class ViewportConfig {
    public static ViewportConfig createFullWindow(int width, int height) {
        return createDefault(0, 0, width, height);
    }

    public static ViewportConfig createDefault(int x, int y, int width, int height) {
        ViewportConfig config = new ViewportConfig();
        config._viewportIndex = 0;
        config._positionX = x;
        config._positionY = y;
        config._width = width;
        config._height = height;
        config._verticalFovDegrees = 60.0;
        config._perspectiveNcp = 1.0f;
        config._perspectiveFcp = 10000.0f;
        return config;
    }

    public int _viewportIndex;
    public int _positionX;
    public int _positionY;
    public int _width;
    public int _height;
    public double _verticalFovDegrees;
    public float _perspectiveNcp;
    public float _perspectiveFcp;
}
