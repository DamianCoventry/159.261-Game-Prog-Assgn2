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

package com.lunargravity.dogfight.view;

import com.lunargravity.engine.widgetsystem.*;

import java.io.IOException;

public class DogfightResultsWidget extends WidgetObserver implements IButtonObserver {
    private static final String DOGFIGHT_HIGH_SCORE_0 = "dogfightHighScore0";
    private static final String DOGFIGHT_HIGH_SCORE_1 = "dogfightHighScore1";
    private static final String START_NEXT_DOGFIGHT_BUTTON = "startNextDogfight";
    private static final String MAIN_MENU_BUTTON = "mainMenuButton";

    private final IDogfightResultsObserver _observer;

    public DogfightResultsWidget(WidgetManager widgetManager, IDogfightResultsObserver observer) {
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
        child = wci.getChild(START_NEXT_DOGFIGHT_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(MAIN_MENU_BUTTON, "ButtonWidget");
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
            case START_NEXT_DOGFIGHT_BUTTON -> _observer.startNextDogfightButtonClicked();
            case MAIN_MENU_BUTTON -> _observer.mainMenuButtonClicked();
        }
    }
}
