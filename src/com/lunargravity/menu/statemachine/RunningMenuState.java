package com.lunargravity.menu.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.campaign.statemachine.LoadingCampaignState;
import com.lunargravity.dogfight.statemachine.LoadingDogfightState;
import com.lunargravity.menu.controller.IMenuController;
import com.lunargravity.menu.controller.IMenuControllerObserver;
import com.lunargravity.race.statemachine.LoadingRaceState;
import com.lunargravity.world.controller.IMenuWorldController;

// TODO: allow the user to move the camera around the 3d scene

public class RunningMenuState extends StateBase implements IMenuControllerObserver {
    public RunningMenuState(IStateMachineContext context) {
        super(context);
    }

    private IMenuWorldController getMenuWorldController() {
        return (IMenuWorldController)getContext().getLogicController();
    }

    private IMenuController getMenuController() {
        return (IMenuController)getContext().getLogicController();
    }

    @Override
    public void begin() {
        getMenuController().addObserver(this);
    }

    @Override
    public void end() {
        getMenuController().removeObserver(this);
    }

    @Override
    public void startNewCampaignRequested(int numPlayers) {
        changeState(new LoadingCampaignState(getContext(), numPlayers));
    }

    @Override
    public void loadExistingCampaignRequested(String fileName) {
        changeState(new LoadingCampaignState(getContext(), fileName));
    }

    @Override
    public void startNewRaceRequested(int numPlayers) {
        changeState(new LoadingRaceState(getContext(), numPlayers, LoadingRaceState.Mode.NEW_GAME));
    }

    @Override
    public void startNewDogfightRequested(int numPlayers) {
        changeState(new LoadingDogfightState(getContext(), numPlayers));
    }
}
