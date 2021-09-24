package com.lunargravity.dogfight.view;

import com.lunargravity.engine.widgetsystem.*;

import java.io.IOException;

public class DogfightCompletedWidget extends WidgetObserver {
    public DogfightCompletedWidget(WidgetManager widgetManager) {
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
}
