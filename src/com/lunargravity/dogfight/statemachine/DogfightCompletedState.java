package com.lunargravity.dogfight.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.dogfight.view.IDogfightView;
import com.lunargravity.engine.timeouts.TimeoutManager;

public class DogfightCompletedState extends StateBase {
    public DogfightCompletedState(IStateMachineContext context) {
        super(context);
    }

    @Override
    public void begin() {
        getDogfightView().showCompletedWidget();

        addTimeout(3500, (callCount) -> {
            changeState(new DogfightResultsState(getContext()));
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }

    private IDogfightView getDogfightView() {
        return (IDogfightView)getContext().getLogicView();
    }
}
