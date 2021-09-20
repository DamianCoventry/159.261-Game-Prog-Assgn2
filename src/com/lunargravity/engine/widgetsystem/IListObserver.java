package com.lunargravity.engine.widgetsystem;

public interface IListObserver {
    void selectedListItemChanged(String widgetId, int oldItem, int newItem);
    void listItemAdded(String widgetId, int itemIndex);
    void listItemRemoved(String widgetId, int itemIndex);
    void listItemsCleared(String widgetId);
}
