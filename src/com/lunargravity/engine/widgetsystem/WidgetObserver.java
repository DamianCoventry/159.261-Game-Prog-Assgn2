package com.lunargravity.engine.widgetsystem;

public abstract class WidgetObserver implements IWidgetObserver {
    protected WidgetManager _widgetManager;
    protected Widget _widget;

    protected WidgetObserver(WidgetManager widgetManager) {
        _widgetManager = widgetManager;
        _widget = null;
    }

    public void initialise(Widget widget, WidgetCreateInfo wci) {
        _widget = widget;
        createChildWidgets(wci);
    }

    protected abstract void createChildWidgets(WidgetCreateInfo wci);

    public String getWidgetId() {
        return _widget.getId();
    }

    @Override
    public void onOpening() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void onOpened() {
        // TODO: Any standard behaviour?
    }

    @Override
    public CloseResult onClosing() {
        // TODO: Any standard behaviour?
        return CloseResult.PROCEED_WITH_CLOSE;
    }

    @Override
    public void onClosed() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void onShowing() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void onShown() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void onHiding() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void onHidden() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void onZOrderChanging() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void onZOrderChanged() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void onLoseKeyboardFocus() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void onGainKeyboardFocus() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void onLoseMouseCapture() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void onGainMouseCapture() {
        // TODO: Any standard behaviour?
    }
}
