package com.lunargravity.engine.widgetsystem;

public class ListWidget extends WidgetObserver {
    private IListObserver _observer;
    public ListWidget(WidgetManager widgetManager, IListObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void createChildWidgets(WidgetCreateInfo wci) {
        // TODO: need to examine the wci structure and pass the correct info to each of these ctor calls
    }

    // TODO: need a simple but flexible system of adding custom widgets as children
    public int addItem() {
        // TODO
        return 0;
    }
    public boolean removeItem(int index) {
        // TODO
        return false;
    }
    public void clearItems() {
        // TODO
    }
    public int getItemCount() {
        // TODO
        return 0;
    }
    public String getItem(int index) {
        // TODO
        return null;
    }
    public int getSelectedIndex() {
        // TODO
        return 0;
    }
    public void setSelectedIndex(int index) {
        // TODO
    }
}
