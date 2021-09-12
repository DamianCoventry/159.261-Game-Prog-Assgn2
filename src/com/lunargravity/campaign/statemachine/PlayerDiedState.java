package com.lunargravity.campaign.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;

public class PlayerDiedState extends StateBase {
    public PlayerDiedState(IStateMachineContext context) {
        super(context);
    }
}
