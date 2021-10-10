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

package com.lunargravity.menu.view;

import com.lunargravity.engine.widgetsystem.*;

import java.io.IOException;

public class RaceScoreboardWidget extends WidgetObserver implements IButtonObserver {
    private static final String RACE_HIGH_SCORE_0 = "raceHighScore0";
    private static final String RACE_HIGH_SCORE_1 = "raceHighScore1";
    private static final String RACE_HIGH_SCORE_2 = "raceHighScore2";
    private static final String RACE_HIGH_SCORE_3 = "raceHighScore3";
    private static final String RACE_HIGH_SCORE_4 = "raceHighScore4";
    private static final String RESET_SCOREBOARD_BUTTON = "resetScoreboardButton";
    private static final String MAIN_MENU_BUTTON = "mainMenuButton";
    private static final String SINGLE_PLAYER_RACE_BUTTON = "singlePlayerRaceButton";
    private static final String TWO_PLAYERS_RACE_BUTTON = "twoPlayersRaceButton";

    private final IRaceScoreboardObserver _observer;

    public RaceScoreboardWidget(WidgetManager widgetManager, IRaceScoreboardObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        WidgetCreateInfo child = wci.getChild(RACE_HIGH_SCORE_0, "HighScoreWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(RACE_HIGH_SCORE_1, "HighScoreWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(RACE_HIGH_SCORE_2, "HighScoreWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(RACE_HIGH_SCORE_3, "HighScoreWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(RACE_HIGH_SCORE_4, "HighScoreWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(RESET_SCOREBOARD_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(MAIN_MENU_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(SINGLE_PLAYER_RACE_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(TWO_PLAYERS_RACE_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
    }

    @Override
    public void freeNativeResources() {
        super.freeNativeResources();
        // TODO
    }

    @Override
    public void buttonClicked(String widgetId) throws IOException {
        switch (widgetId) {
            case RESET_SCOREBOARD_BUTTON -> _observer.resetRaceScoreboardButtonClicked();
            case MAIN_MENU_BUTTON -> _observer.mainMenuButtonClicked();
            case SINGLE_PLAYER_RACE_BUTTON -> _observer.startSinglePlayerRaceButtonClicked();
            case TWO_PLAYERS_RACE_BUTTON -> _observer.startTwoPlayersRaceButtonClicked();
        }
    }
}
