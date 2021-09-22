package com.lunargravity.race.statemachine;

import com.lunargravity.application.*;
import com.lunargravity.engine.timeouts.TimeoutManager;

public class PlayerDiedState extends StateBase {
    public PlayerDiedState(IStateMachineContext context) {
        super(context);
    }

    @Override
    public void begin() {
        addTimeout(3000, (callCount) -> {
            changeState(new RunningRaceState(getContext()));
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }
}
