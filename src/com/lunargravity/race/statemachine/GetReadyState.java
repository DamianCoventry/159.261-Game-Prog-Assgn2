package com.lunargravity.race.statemachine;

import com.lunargravity.application.*;
import com.lunargravity.engine.timeouts.TimeoutManager;
import com.lunargravity.race.view.IRaceView;

import java.io.IOException;

public class GetReadyState extends StateBase {
    private static final int NUM_COUNTDOWN_SECONDS = 3;

    public GetReadyState(IStateMachineContext context) {
        super(context);
    }

    private IRaceView getRaceView() {
        return (IRaceView)getContext().getLogicView();
    }

    @Override
    public void begin() throws IOException {
        getRaceView().showGetReadyWidget(NUM_COUNTDOWN_SECONDS);

        addTimeout(1000, (callCount) -> {
            if (callCount < NUM_COUNTDOWN_SECONDS) {
                try {
                    getRaceView().showGetReadyWidget(NUM_COUNTDOWN_SECONDS - callCount);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return TimeoutManager.CallbackResult.KEEP_CALLING;
            }
            changeState(new RunningRaceState(getContext()));
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }
}
