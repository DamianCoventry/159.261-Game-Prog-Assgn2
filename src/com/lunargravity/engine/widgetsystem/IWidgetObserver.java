package com.lunargravity.engine.widgetsystem;

public interface IWidgetObserver {
    void onOpening();
    void onOpened();
    enum CloseResult { CANCEL_CLOSE, PROCEED_WITH_CLOSE }
    CloseResult onClosing();
    void onClosed();
    void onShowing();
    void onShown();
    void onHiding();
    void onHidden();
    void onZOrderChanging();
    void onZOrderChanged();
    void onLoseKeyboardFocus();
    void onGainKeyboardFocus();
    void onLoseMouseCapture();
    void onGainMouseCapture();
}
