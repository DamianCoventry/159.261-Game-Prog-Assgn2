//
// Lunar Gravity
//
// This game is based upon the Amiga video game Gravity Force that was
// released in 1989 by Stephan Wenzler
//
// https://www.mobygames.com/game/gravity-force
// https://www.youtube.com/watch?v=m9mFtCvnko8
//
// This implementation is Copyright (c) 2021, Damian Coventry
// All rights reserved
// Written for Massey University course 159.261 Game Programming (Assignment 2)
//

package com.lunargravity.engine.widgetsystem;

import java.io.IOException;

public class ListWidget extends WidgetObserver {
    private final IListObserver _observer;
    public ListWidget(WidgetManager widgetManager, IListObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        super.initialiseChildren(wci);
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

    @Override
    public void freeNativeResources() {
        super.freeNativeResources();
        // TODO
    }
}
