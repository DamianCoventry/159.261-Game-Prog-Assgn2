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

package com.lunargravity.engine.widgetsystem;

import org.joml.Matrix4f;

import java.io.IOException;

public interface IWidgetObserver {
    void freeResources();
    void widgetOpening();
    void widgetOpened();

    enum CloseResult { CANCEL_CLOSE, PROCEED_WITH_CLOSE }
    CloseResult widgetClosing();
    void widgetClosed();
    void widgetShowing();
    void widgetShown() throws IOException;
    void widgetHiding();
    void widgetHidden();
    void widgetZOrderChanging();
    void widgetZOrderChanged();
    void widgetLoseKeyboardFocus();
    void widgetGainKeyboardFocus();
    void widgetLoseMouseCapture();
    void widgetGainMouseCapture();
    void widgetThink();
    void widgetDraw(Matrix4f projectionMatrix);
    void widgetParentResized(float width, float height);
}
