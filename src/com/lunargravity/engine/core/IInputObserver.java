package com.lunargravity.engine.core;

import java.io.IOException;

public interface IInputObserver {
    void keyboardKeyEvent(int key, int scancode, int action, int mods);
    void mouseButtonEvent(int button, int action, int mods) throws IOException, InterruptedException;
    void mouseCursorMovedEvent(double xPos, double yPos);
    void mouseWheelScrolledEvent(double xOffset, double yOffset);
}
