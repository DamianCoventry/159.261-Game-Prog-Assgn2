package com.lunargravity.dogfight.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.dogfight.controller.IDogfightController;
import com.lunargravity.dogfight.controller.IDogfightControllerObserver;
import com.lunargravity.dogfight.view.IDogfightView;
import com.lunargravity.menu.statemachine.LoadingMenuState;

public class DogfightPausedState extends StateBase implements IDogfightControllerObserver {
    public DogfightPausedState(IStateMachineContext context) {
        super(context);
    }

    private IDogfightView getDogfightView() {
        return (IDogfightView)getContext().getLogicView();
    }

    private IDogfightController getDogfightController() {
        return (IDogfightController)getContext().getLogicController();
    }

    @Override
    public void begin() {
        getDogfightController().addObserver(this);
        getDogfightView().showPausedWidget();
    }

    @Override
    public void end() {
        getDogfightController().removeObserver(this);
    }

    @Override
    public void startNewDogfightRequested(int numPlayers) {

    }

    @Override
    public void startNextDogfightRequested() {

    }

    @Override
    public void resumeDogfightRequested() {
        changeState(new GetReadyState(getContext()));
    }

    @Override
    public void goToMainMenuRequested() {
        changeState(new LoadingMenuState(getContext()));
    }

    @Override
    public void goToDogfightScoreboardRequested() {

    }
}
