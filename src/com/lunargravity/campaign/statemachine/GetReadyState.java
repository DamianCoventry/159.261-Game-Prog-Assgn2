package com.lunargravity.campaign.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.campaign.controller.ICampaignController;
import com.lunargravity.campaign.view.ICampaignView;
import com.lunargravity.engine.timeouts.TimeoutManager;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;

public class GetReadyState extends StateBase {
    public static final int MIN_SECONDS = 1;
    public static final int MAX_SECONDS = 3;
    private int _timeoutId;

    public GetReadyState(IStateMachineContext context) {
        super(context);
        _timeoutId = 0;
    }

    @Override
    public void begin() throws IOException {
        getCampaignView().showGetReady(MAX_SECONDS);

        _timeoutId = addTimeout(1000, (callCount) -> {
            if (callCount < MAX_SECONDS) {
                try {
                    getCampaignView().showGetReady(MAX_SECONDS - callCount);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return TimeoutManager.CallbackResult.KEEP_CALLING;
            }

            changeState(new RunningMissionState(getContext()));
            _timeoutId = 0;
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }

    @Override
    public void end() {
        if (_timeoutId != 0) {
            removeTimeout(_timeoutId);
            _timeoutId = 0;
        }
    }

    private ICampaignView getCampaignView() {
        return (ICampaignView)getContext().getLogicView();
    }
}
