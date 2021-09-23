package com.lunargravity.engine.widgetsystem;

public class AnnouncementWidget extends WidgetObserver {
    private IAnnouncementObserver _observer;
    public AnnouncementWidget(WidgetManager widgetManager, IAnnouncementObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    // TODO: requires 1 or more LabelWidgets to be centered vertically and horizontally
    //   requires 1 ore more LabelWidgets to be anchored to the bottom right of the viewport
    //   requires 1 background ImageWidget
    //   requires 1 or more fonts

    @Override
    protected void createChildWidgets(WidgetCreateInfo wci) {
        // TODO: need to examine the wci structure and pass the correct info to each of these ctor calls
    }

    @Override
    public void freeResources() {
        // TODO
    }
}
