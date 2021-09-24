package com.lunargravity.engine.widgetsystem;

import java.io.IOException;

public class LabelWidget extends WidgetObserver {
    protected LabelWidget(WidgetManager widgetManager) {
        super(widgetManager);
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

    // TODO: get the text and the font in here.
}
