package com.lunargravity.engine.widgetsystem;

public interface IKeyBindingObserver {
    void keyBindingChanged(String widgetId, int oldKey, int newKey);
}
