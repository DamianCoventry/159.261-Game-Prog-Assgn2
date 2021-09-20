package com.lunargravity.engine.widgetsystem;

public class NumericWidget extends WidgetObserver {
    private final INumericObserver _observer;

    public NumericWidget(WidgetManager widgetManager, INumericObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void createChildWidgets(WidgetCreateInfo wci) {
        // TODO: need to examine the wci structure and pass the correct info to each of these ctor calls
    }

    // TODO: get the list of images in here. maintain state for hovered, focused, pressed.
}
