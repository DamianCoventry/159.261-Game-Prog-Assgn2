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
