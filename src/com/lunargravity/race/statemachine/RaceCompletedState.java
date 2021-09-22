package com.lunargravity.race.statemachine;

import com.lunargravity.application.*;
import com.lunargravity.engine.timeouts.TimeoutManager;
import com.lunargravity.race.view.IRaceView;

public class RaceCompletedState extends StateBase {
    public RaceCompletedState(IStateMachineContext context) {
        super(context);
    }

    private IRaceView getRaceView() {
        return (IRaceView)getContext().getLogicView();
    }

    @Override
    public void begin() {
        getRaceView().showCompletedWidget();

        addTimeout(3000, (callCount) -> {
            changeState(new RaceResultsState(getContext()));
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }
}
