package com.lunargravity.race.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.engine.timeouts.TimeoutManager;
import com.lunargravity.race.view.IRaceView;

public class RaceCompletedState extends StateBase {
    public RaceCompletedState(IStateMachineContext context) {
        super(context);
    }

    @Override
    public void begin() {
        getRaceView().showCompletedWidget();

        addTimeout(3500, (callCount) -> {
            changeState(new RaceResultsState(getContext()));
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }

    private IRaceView getRaceView() {
        return (IRaceView)getContext().getLogicView();
    }
}
