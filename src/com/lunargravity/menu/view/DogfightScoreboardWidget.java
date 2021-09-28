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

public class DogfightScoreboardWidget extends WidgetObserver implements IButtonObserver {
    private static final String DOGFIGHT_HIGH_SCORE_0 = "dogfightHighScore0";
    private static final String DOGFIGHT_HIGH_SCORE_1 = "dogfightHighScore1";
    private static final String DOGFIGHT_HIGH_SCORE_2 = "dogfightHighScore2";
    private static final String DOGFIGHT_HIGH_SCORE_3 = "dogfightHighScore3";
    private static final String DOGFIGHT_HIGH_SCORE_4 = "dogfightHighScore4";
    private static final String RESET_SCOREBOARD_BUTTON = "resetScoreboardButton";
    private static final String MAIN_MENU_BUTTON = "mainMenuButton";
    private static final String SINGLE_PLAYER_DOGFIGHT_BUTTON = "singlePlayerDogfightButton";
    private static final String TWO_PLAYERS_DOGFIGHT_BUTTON = "twoPlayersDogfightButton";

    private final IDogfightScoreboardObserver _observer;

    public DogfightScoreboardWidget(WidgetManager widgetManager, IDogfightScoreboardObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        WidgetCreateInfo child = wci.getChild(DOGFIGHT_HIGH_SCORE_0, "HighScoreWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(DOGFIGHT_HIGH_SCORE_1, "HighScoreWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(DOGFIGHT_HIGH_SCORE_2, "HighScoreWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(DOGFIGHT_HIGH_SCORE_3, "HighScoreWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(DOGFIGHT_HIGH_SCORE_4, "HighScoreWidget");
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
        child = wci.getChild(SINGLE_PLAYER_DOGFIGHT_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(TWO_PLAYERS_DOGFIGHT_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
    }

    @Override
    public void freeResources() {
        super.freeResources();
        // TODO
    }

    @Override
    public void buttonClicked(String widgetId) {
        switch (widgetId) {
            case RESET_SCOREBOARD_BUTTON -> _observer.resetDogfightScoreboardButtonClicked();
            case MAIN_MENU_BUTTON -> _observer.mainMenuButtonClicked();
            case SINGLE_PLAYER_DOGFIGHT_BUTTON -> _observer.startSinglePlayerDogfightButtonClicked();
            case TWO_PLAYERS_DOGFIGHT_BUTTON -> _observer.startTwoPlayersDogfightButtonClicked();
        }
    }
}
