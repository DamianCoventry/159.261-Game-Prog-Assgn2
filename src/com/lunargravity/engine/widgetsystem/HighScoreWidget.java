package com.lunargravity.engine.widgetsystem;

import java.io.IOException;

public class HighScoreWidget extends WidgetObserver {
    public HighScoreWidget(WidgetManager widgetManager) {
        super(widgetManager);
    }

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        super.initialiseChildren(wci);
        // TODO: need to examine the wci structure and pass the correct info to each of these ctor calls
    }

    @Override
    public void freeNativeResources() {
        super.freeNativeResources();
        // TODO
    }

    // TODO: get the list of images in here. maintain state for hovered, focused, pressed.
}
