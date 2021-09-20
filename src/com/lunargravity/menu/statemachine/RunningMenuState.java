package com.lunargravity.menu.statemachine;

import com.lunargravity.application.*;
import com.lunargravity.campaign.statemachine.EpisodeIntroState;
import com.lunargravity.dogfight.statemachine.DogfightScoreboardState;
import com.lunargravity.menu.controller.IMenuController;
import com.lunargravity.menu.controller.IMenuControllerObserver;
import com.lunargravity.race.statemachine.RaceScoreboardState;
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
        changeState(new EpisodeIntroState(getContext(), numPlayers));
    }

    @Override
    public void loadExistingCampaignRequested(String fileName) {
        changeState(new EpisodeIntroState(getContext(), fileName));
    }

    @Override
    public void viewRaceScoreboardRequested() {
        changeState(new RaceScoreboardState(getContext()));
    }

    @Override
    public void viewDogfightScoreboardRequested() {
        changeState(new DogfightScoreboardState(getContext()));
    }
}
