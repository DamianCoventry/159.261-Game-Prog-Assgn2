package com.lunargravity.dogfight.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.dogfight.controller.IDogfightController;
import com.lunargravity.dogfight.controller.IDogfightControllerObserver;
import com.lunargravity.dogfight.view.IDogfightView;
import com.lunargravity.menu.statemachine.LoadingMenuState;

import java.io.IOException;

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
    public void begin() throws IOException {
        getDogfightController().addObserver(this);
        getDogfightView().showResultsWidget();
    }

    @Override
    public void end() {
        getDogfightController().removeObserver(this);
    }

    @Override
    public void startNextDogfightRequested(int numPlayers) {
        changeState(new LoadingDogfightState(getContext(), numPlayers, LoadingDogfightState.Mode.NEXT_LEVEL));
    }

    @Override
    public void resumeDogfightRequested() {
        // Nothing to do
    }

    @Override
    public void mainMenuRequested() {
        changeState(new LoadingMenuState(getContext()));
    }
}
