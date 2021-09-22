package com.lunargravity.dogfight.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.dogfight.view.IDogfightView;
import com.lunargravity.engine.timeouts.TimeoutManager;

public class GetReadyState extends StateBase {
    private static final int NUM_COUNTDOWN_SECONDS = 3;

    public GetReadyState(IStateMachineContext context) {
        super(context);
    }

    private IDogfightView getDogfightView() {
        return (IDogfightView)getContext().getLogicView();
    }

    @Override
    public void begin() {
        getDogfightView().showGetReadyWidget(NUM_COUNTDOWN_SECONDS);

        addTimeout(1000, (callCount) -> {
            if (callCount < NUM_COUNTDOWN_SECONDS) {
                getDogfightView().showGetReadyWidget(NUM_COUNTDOWN_SECONDS - callCount);
                return TimeoutManager.CallbackResult.KEEP_CALLING;
            }
            changeState(new RunningDogfightState(getContext()));
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }
}
