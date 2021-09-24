package com.lunargravity.engine.widgetsystem;

import java.io.IOException;

public class KeyBindingWidget extends WidgetObserver {
    private final IKeyBindingObserver _observer;

    public KeyBindingWidget(WidgetManager widgetManager, IKeyBindingObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        super.initialiseChildren(wci);
        // TODO: need to examine the wci structure and pass the correct info to each of these ctor calls
    }

    @Override
    public void freeResources() {
        super.freeResources();
        // TODO
    }

    // TODO: get the list of images in here. maintain state for hovered, focused, pressed.
}
