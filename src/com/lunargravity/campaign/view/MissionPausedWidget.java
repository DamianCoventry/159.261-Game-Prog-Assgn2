package com.lunargravity.campaign.view;

import com.lunargravity.engine.widgetsystem.*;

import java.io.IOException;

public class MissionPausedWidget extends WidgetObserver implements IButtonObserver {
    private static final String RESUME_MISSION_BUTTON = "resumeMission";
    private static final String MAIN_MENU_BUTTON = "mainMenuButton";

    private final IMissionPausedObserver _observer;

    public MissionPausedWidget(WidgetManager widgetManager, IMissionPausedObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        WidgetCreateInfo child = wci.getChild(RESUME_MISSION_BUTTON, "ButtonWidget");
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
            case RESUME_MISSION_BUTTON -> _observer.resumeMissionButtonClicked();
            case MAIN_MENU_BUTTON -> _observer.mainMenuButtonClicked();
        }
    }

    @Override
    public void freeResources() {
        super.freeResources();
        // anything to do?
    }
}
