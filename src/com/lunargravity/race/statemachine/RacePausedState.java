package com.lunargravity.race.statemachine;

import com.lunargravity.application.*;
import com.lunargravity.menu.statemachine.LoadingMenuState;
import com.lunargravity.race.controller.IRaceController;
import com.lunargravity.race.controller.IRaceControllerObserver;
import com.lunargravity.race.view.IRaceView;

public class RacePausedState extends StateBase implements IRaceControllerObserver {
    public RacePausedState(IStateMachineContext context) {
        super(context);
    }

    private IRaceView getRaceView() {
        return (IRaceView)getContext().getLogicView();
    }

    private IRaceController getRaceController() {
        return (IRaceController)getContext().getLogicController();
    }

    @Override
    public void begin() {
        getRaceController().addObserver(this);
        getRaceView().showPausedWidget();
    }

    @Override
    public void end() {
        getRaceController().removeObserver(this);
    }

    @Override
    public void startNewRaceRequested(int numPlayers) {

    }

    @Override
    public void startNextRaceRequested() {

    }

    @Override
    public void resumeRaceRequested() {
        changeState(new GetReadyState(getContext()));
    }

    @Override
    public void goToMainMenuRequested() {
        changeState(new LoadingMenuState(getContext()));
    }

    @Override
    public void goToRaceScoreboardRequested() {

    }
}
