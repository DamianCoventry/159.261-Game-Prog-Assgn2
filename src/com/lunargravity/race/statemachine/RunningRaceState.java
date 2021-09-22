package com.lunargravity.race.statemachine;

import com.lunargravity.application.*;
import com.lunargravity.race.view.IRaceView;

public class RunningRaceState extends StateBase {
    public RunningRaceState(IStateMachineContext context) {
        super(context);
    }

    private IRaceView getRaceView() {
        return (IRaceView)getContext().getLogicView();
    }

    // TODO: we will need events like these
    public void bothPlayerShipsCrossedFinishLine() {
        changeState(new RaceCompletedState(getContext()));
    }
    public void playerShipExploded() {
        changeState(new PlayerDiedState(getContext()));
    }
    public void escapeKeyPressed() {
        changeState(new RacePausedState(getContext()));
    }
}
