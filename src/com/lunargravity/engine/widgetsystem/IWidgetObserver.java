package com.lunargravity.engine.widgetsystem;

import org.joml.Matrix4f;

public interface IWidgetObserver {
    void freeResources();
    void widgetOpening();
    void widgetOpened();
    enum CloseResult { CANCEL_CLOSE, PROCEED_WITH_CLOSE }
    CloseResult widgetClosing();
    void widgetClosed();
    void widgetShowing();
    void widgetShown();
    void widgetHiding();
    void widgetHidden();
    void widgetZOrderChanging();
    void widgetZOrderChanged();
    void widgetLoseKeyboardFocus();
    void widgetGainKeyboardFocus();
    void widgetLoseMouseCapture();
    void widgetGainMouseCapture();
    void widgetThink();
    void widgetDraw(int viewport, Matrix4f projectionMatrix);
}
