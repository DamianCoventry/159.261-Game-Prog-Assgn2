//
// Lunar Gravity
//
// This game is based upon the Amiga video game Gravity Force that was
// released in 1989 by Stephan Wenzler
//
// https://www.mobygames.com/game/gravity-force
// https://www.youtube.com/watch?v=m9mFtCvnko8
//
// This implementation is Copyright (c) 2021, Damian Coventry
// All rights reserved
// Written for Massey University course 159.261 Game Programming (Assignment 2)
//

package com.lunargravity.menu.statemachine;

import com.lunargravity.application.IStateMachineContext;
import com.lunargravity.application.StateBase;
import com.lunargravity.campaign.statemachine.LoadingCampaignState;
import com.lunargravity.dogfight.statemachine.LoadingDogfightState;
import com.lunargravity.engine.timeouts.TimeoutManager;
import com.lunargravity.menu.controller.IMenuController;
import com.lunargravity.menu.controller.IMenuControllerObserver;
import com.lunargravity.menu.view.IMenuView;
import com.lunargravity.race.statemachine.LoadingRaceState;
import com.lunargravity.world.controller.IMenuWorldController;

import java.io.IOException;

public class RunningMenuState extends StateBase implements IMenuControllerObserver {
    private int _timeoutId;

    public RunningMenuState(IStateMachineContext context) {
        super(context);
        _timeoutId = 0;
    }

    private IMenuWorldController getMenuWorldController() {
        return (IMenuWorldController)getContext().getLogicController();
    }

    private IMenuController getMenuController() {
        return (IMenuController)getContext().getLogicController();
    }

    private IMenuView getMenuView() {
        return (IMenuView)getContext().getLogicView();
    }

    @Override
    public void begin() {
        getMenuController().addObserver(this);

        _timeoutId = addTimeout(250, (callCount) -> {
            try {
                getMenuView().showMainWidget();
            } catch (IOException e) {
                e.printStackTrace();
            }
            _timeoutId = 0;
            return TimeoutManager.CallbackResult.REMOVE_THIS_CALLBACK;
        });
    }

    @Override
    public void end() {
        getMenuController().removeObserver(this);
        if (_timeoutId > 0) {
            removeTimeout(_timeoutId);
            _timeoutId = 0;
        }
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
        changeState(new LoadingDogfightState(getContext(), numPlayers, LoadingDogfightState.Mode.NEW_GAME));
    }
}
