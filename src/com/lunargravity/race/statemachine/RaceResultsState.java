package com.lunargravity.race.statemachine;

import com.lunargravity.application.*;
import com.lunargravity.menu.statemachine.LoadingMenuState;
import com.lunargravity.race.controller.IRaceController;
import com.lunargravity.race.controller.IRaceControllerObserver;
import com.lunargravity.race.view.IRaceView;

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
    public void begin() {
        getRaceController().addObserver(this);
        getRaceView().showResultsWidget();
    }

    @Override
    public void end() {
        getRaceController().removeObserver(this);
    }

    @Override
    public void startNextRaceRequested() {
        // TODO: will need the controller/model to bump the level number
        //   perhaps add a startNextRaceGame() method?
        changeState(new LoadingRaceState(getContext(), 1)); // TODO: get the numPlayers from the model
    }

    @Override
    public void resumeRaceRequested() {

    }

    @Override
    public void mainMenuRequested() {
        changeState(new LoadingMenuState(getContext()));
    }
}
