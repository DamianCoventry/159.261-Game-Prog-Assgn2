package com.lunargravity.race.view;

import com.lunargravity.engine.widgetsystem.*;

public class RaceCompletedObserver extends WidgetObserver {
    public RaceCompletedObserver(WidgetManager widgetManager) {
        super(widgetManager);
    }

    @Override
    protected void createChildWidgets(WidgetCreateInfo wci) {
        // TODO: need to examine the wci structure and pass the correct info to each of these ctor calls
    }

    @Override
    public void freeResources() {
        // TODO
    }
}
