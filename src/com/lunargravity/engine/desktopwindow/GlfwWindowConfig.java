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

package com.lunargravity.engine.desktopwindow;

import java.util.HashMap;

public class GlfwWindowConfig {
    public String _title;
    public int _positionX;
    public int _positionY;
    public int _width;
    public int _height;
    public boolean _resizeable;
    public boolean _centered;
    public String[] _iconFileNames;

    public static class MouseCursorConfig {
        public String _fileName;
        public int _clickXOffset;
        public int _clickYOffset;
        public MouseCursorConfig(String fileName, int clickXOffset, int clickYOffset) {
            _fileName = fileName;
            _clickXOffset = clickXOffset;
            _clickYOffset = clickYOffset;
        }
    }
    public HashMap<Integer, MouseCursorConfig> _mouseCursors;
    public int _initialMouseCursor;
}
