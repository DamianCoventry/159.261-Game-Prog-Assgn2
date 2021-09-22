package com.lunargravity.dogfight.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.dogfight.view.IDogfightView;

public class RunningDogfightState extends StateBase {
    public RunningDogfightState(IStateMachineContext context) {
        super(context);
    }

    private IDogfightView getDogfightView() {
        return (IDogfightView)getContext().getLogicView();
    }

    // TODO: we will need events like these
    public void bothPlayerShipsCrossedFinishLine() {
        changeState(new DogfightCompletedState(getContext()));
    }
    public void playerShipExploded() {
        changeState(new PlayerDiedState(getContext()));
    }
    public void escapeKeyPressed() {
        changeState(new DogfightPausedState(getContext()));
    }
}
