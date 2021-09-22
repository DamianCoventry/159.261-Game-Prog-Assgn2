package com.lunargravity.dogfight.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.dogfight.controller.IDogfightController;
import com.lunargravity.dogfight.controller.IDogfightControllerObserver;
import com.lunargravity.dogfight.view.IDogfightView;
import com.lunargravity.menu.statemachine.LoadingMenuState;

public class DogfightScoreboardState extends StateBase implements IDogfightControllerObserver {
    public DogfightScoreboardState(IStateMachineContext context) {
        super(context);
    }

    private IDogfightView getDogfightView() {
        return (IDogfightView)getContext().getLogicView();
    }

    // TODO: we will need to reset the race times when the Reset button is pressed. is that
    //  one explicitly from the widget? or does a widget event land in here?

    private IDogfightController getDogfightController() {
        return (IDogfightController)getContext().getLogicController();
    }

    @Override
    public void begin() {
        getDogfightController().addObserver(this);
        getDogfightView().showScoreboardWidget();
    }

    @Override
    public void end() {
        getDogfightController().removeObserver(this);
    }

    @Override
    public void startNewDogfightRequested(int numPlayers) {
        changeState(new LoadingDogfightState(getContext(), numPlayers));
    }

    @Override
    public void startNextDogfightRequested() {

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

    }
}
