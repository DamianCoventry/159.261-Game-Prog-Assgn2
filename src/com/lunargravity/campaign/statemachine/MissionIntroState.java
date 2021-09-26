package com.lunargravity.campaign.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.campaign.view.ICampaignView;
import com.lunargravity.engine.timeouts.TimeoutManager;

public class MissionIntroState extends StateBase {
    private int _timeoutId;

    public MissionIntroState(IStateMachineContext context) {
        super(context);
    }

    private ICampaignView getCampaignView() {
        return (ICampaignView)getContext().getLogicView();
    }

    @Override
    public void begin() {
        getCampaignView().showMissionIntro();

        _timeoutId = addTimeout(3000, (callCount) -> {
            changeState(new GetReadyState(getContext()));

            _timeoutId = 0;
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }

    @Override
    public void end() {
        if (_timeoutId > 0) {
            removeTimeout(_timeoutId);
            _timeoutId = 0;
        }
    }
}
