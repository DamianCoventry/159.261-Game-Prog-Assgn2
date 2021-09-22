package com.lunargravity.dogfight.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.dogfight.controller.IDogfightController;
import com.lunargravity.dogfight.controller.IDogfightControllerObserver;
import com.lunargravity.dogfight.view.IDogfightView;
import com.lunargravity.menu.statemachine.LoadingMenuState;

public class DogfightResultsState extends StateBase implements IDogfightControllerObserver {
    public DogfightResultsState(IStateMachineContext context) {
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
        getDogfightView().showResultsWidget();
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
        // TODO: will need the controller/model to bump the level number
        //   perhaps add a startNextDogfightGame() method?
        changeState(new LoadingDogfightState(getContext(), 1)); // TODO: get the numPlayers from the model
    }

    @Override
    public void resumeDogfightRequested() {

    }

    @Override
    public void goToMainMenuRequested() {
        changeState(new LoadingMenuState(getContext()));
    }

    @Override
    public void goToDogfightScoreboardRequested() {
        changeState(new DogfightScoreboardState(getContext()));
    }
}
