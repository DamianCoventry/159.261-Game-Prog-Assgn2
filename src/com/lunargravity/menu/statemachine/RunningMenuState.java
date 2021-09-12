package com.lunargravity.menu.statemachine;

import com.lunargravity.application.*;
import com.lunargravity.world.controller.IMenuWorldControllerEvents;

public class RunningMenuState extends StateBase implements IMenuWorldControllerEvents {
    public RunningMenuState(IStateMachineContext context) {
        super(context);
    }

    @Override
    public void temp() {

    }
}
