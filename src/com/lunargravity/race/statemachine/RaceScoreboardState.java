package com.lunargravity.race.statemachine;

import com.lunargravity.application.*;
import com.lunargravity.menu.statemachine.LoadingMenuState;
import com.lunargravity.race.controller.IRaceController;
import com.lunargravity.race.controller.IRaceControllerObserver;
import com.lunargravity.race.view.IRaceView;

public class RaceScoreboardState extends StateBase implements IRaceControllerObserver {
    public RaceScoreboardState(IStateMachineContext context) {
        super(context);
    }

    private IRaceView getRaceView() {
        return (IRaceView)getContext().getLogicView();
    }

    // TODO: we will need to reset the race times when the Reset button is pressed. is that
    //  one explicitly from the widget? or does a widget event land in here?

    private IRaceController getRaceController() {
        return (IRaceController)getContext().getLogicController();
    }

    @Override
    public void begin() {
        getRaceController().addObserver(this);
        getRaceView().showScoreboardWidget();
    }

    @Override
    public void end() {
        getRaceController().removeObserver(this);
    }

    @Override
    public void startNewRaceRequested(int numPlayers) {
        changeState(new LoadingRaceState(getContext(), numPlayers));
    }

    @Override
    public void startNextRaceRequested() {

    }

    @Override
    public void resumeRaceRequested() {

    }

    @Override
    public void goToMainMenuRequested() {
        changeState(new LoadingMenuState(getContext()));
    }

    @Override
    public void goToRaceScoreboardRequested() {

    }
}
