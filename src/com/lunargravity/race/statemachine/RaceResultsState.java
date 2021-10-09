package com.lunargravity.race.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.menu.statemachine.LoadingMenuState;
import com.lunargravity.race.controller.IRaceController;
import com.lunargravity.race.controller.IRaceControllerObserver;
import com.lunargravity.race.view.IRaceView;

import java.io.IOException;

public class RaceResultsState extends StateBase implements IRaceControllerObserver {
    public RaceResultsState(IStateMachineContext context) {
        super(context);
    }

    private IRaceView getRaceView() {
        return (IRaceView)getContext().getLogicView();
    }

    private IRaceController getRaceController() {
        return (IRaceController)getContext().getLogicController();
    }

    @Override
    public void begin() throws IOException {
        getRaceController().addObserver(this);
        getRaceView().showResultsWidget();
    }

    @Override
    public void end() {
        getRaceController().removeObserver(this);
    }

    @Override
    public void startNextRaceRequested(int numPlayers) {
        changeState(new LoadingRaceState(getContext(), numPlayers, LoadingRaceState.Mode.NEXT_LEVEL));
    }

    @Override
    public void resumeRaceRequested() {
        // Nothing to do
    }

    @Override
    public void mainMenuRequested() {
        changeState(new LoadingMenuState(getContext()));
    }
}
