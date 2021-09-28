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

public class MainWidget extends WidgetObserver implements IButtonObserver {
    private static final String CAMPAIGN_BUTTON = "campaignButton";
    private static final String RACE_BUTTON = "raceButton";
    private static final String DOGFIGHT_BUTTON = "dogfightButton";
    private static final String OPTIONS_BUTTON = "optionsButton";
    private static final String EXIT_BUTTON = "exitButton";

    private final IMainWidgetObserver _observer;

    public MainWidget(WidgetManager widgetManager, IMainWidgetObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        WidgetCreateInfo child = wci.getChild(CAMPAIGN_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(RACE_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(DOGFIGHT_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(OPTIONS_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(EXIT_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
    }

    @Override
    public void buttonClicked(String widgetId) {
        switch (widgetId) {
            case CAMPAIGN_BUTTON -> _observer.campaignGameButtonClicked();
            case RACE_BUTTON -> _observer.raceGameButtonClicked();
            case DOGFIGHT_BUTTON -> _observer.dogfightGameButtonClicked();
            case OPTIONS_BUTTON -> _observer.optionsButtonClicked();
            case EXIT_BUTTON -> _observer.exitApplicationButtonClicked();
        }
    }

    @Override
    public void freeResources() {
        super.freeResources();
        // anything to do?
    }
}
