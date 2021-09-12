package com.lunargravity.engine.core;

public interface IInputConsumer {
    void onKeyboardKeyEvent(int key, int scancode, int action, int mods);
    void onMouseButtonEvent(int button, int action, int mods);
    void onMouseCursorMovedEvent(double xPos, double yPos);
    void onMouseWheelScrolledEvent(double xOffset, double yOffset);
}
