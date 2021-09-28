package com.lunargravity.race.view;

import com.lunargravity.engine.widgetsystem.*;

import java.io.IOException;

public class RacePausedWidget extends WidgetObserver implements IButtonObserver {
    private static final String RESUME_RACE_BUTTON = "resumeRace";
    private static final String MAIN_MENU_BUTTON = "mainMenuButton";

    private final IRacePausedObserver _observer;

    public RacePausedWidget(WidgetManager widgetManager, IRacePausedObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        WidgetCreateInfo child = wci.getChild(RESUME_RACE_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(MAIN_MENU_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
    }

    @Override
    public void buttonClicked(String widgetId) {
        switch (widgetId) {
            case RESUME_RACE_BUTTON -> _observer.resumeRaceButtonClicked();
            case MAIN_MENU_BUTTON -> _observer.mainMenuButtonClicked();
        }
    }

    @Override
    public void freeResources() {
        super.freeResources();
        // anything to do?
    }
}
