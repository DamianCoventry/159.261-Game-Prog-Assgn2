package com.lunargravity.menu.statemachine;

import com.lunargravity.application.*;
import com.lunargravity.menu.view.MenuBuilderObserver;

public class LoadingMenuState extends StateBase {
    public LoadingMenuState(IStateMachineContext context) {
        super(context);
    }

    @Override
    public void begin() {
        MenuBuilderObserver menuBuilderObserver = new MenuBuilderObserver(getManualFrameUpdater());

        getContext().startMenu(menuBuilderObserver);

        changeState(new RunningMenuState(getContext()));
    }
}
