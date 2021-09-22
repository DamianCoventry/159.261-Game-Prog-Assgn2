package com.lunargravity.dogfight.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.engine.timeouts.TimeoutManager;

public class PlayerDiedState extends StateBase {
    public PlayerDiedState(IStateMachineContext context) {
        super(context);
    }

    @Override
    public void begin() {
        addTimeout(3000, (callCount) -> {
            changeState(new RunningDogfightState(getContext()));
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }
}
