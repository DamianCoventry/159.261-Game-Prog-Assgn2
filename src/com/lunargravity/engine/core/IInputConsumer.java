package com.lunargravity.engine.core;

public interface IInputConsumer {
    void keyboardKeyEvent(int key, int scancode, int action, int mods);
    void mouseButtonEvent(int button, int action, int mods);
    void mouseCursorMovedEvent(double xPos, double yPos);
    void mouseWheelScrolledEvent(double xOffset, double yOffset);
}
