package com.lunargravity.race.view;

import com.lunargravity.engine.widgetsystem.*;

import java.io.IOException;

public class RaceResultsWidget extends WidgetObserver implements IButtonObserver {
    private static final String RACE_HIGH_SCORE_0 = "raceHighScore0";
    private static final String RACE_HIGH_SCORE_1 = "raceHighScore1";
    private static final String START_NEXT_RACE_BUTTON = "startNextRace";
    private static final String MAIN_MENU_BUTTON = "mainMenuButton";

    private final IRaceResultsObserver _observer;

    public RaceResultsWidget(WidgetManager widgetManager, IRaceResultsObserver observer) {
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
        child = wci.getChild(START_NEXT_RACE_BUTTON, "ButtonWidget");
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
            case START_NEXT_RACE_BUTTON -> _observer.startNextRaceButtonClicked();
            case MAIN_MENU_BUTTON -> _observer.mainMenuButtonClicked();
        }
    }
}
