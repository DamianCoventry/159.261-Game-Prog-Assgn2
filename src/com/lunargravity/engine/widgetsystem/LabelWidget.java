package com.lunargravity.engine.widgetsystem;

public class LabelWidget extends WidgetObserver {
    protected LabelWidget(WidgetManager widgetManager) {
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

    // TODO: get the text and the font in here.
}
