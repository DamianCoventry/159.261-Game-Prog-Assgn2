package com.lunargravity.dogfight.view;

import com.lunargravity.engine.widgetsystem.*;

import java.io.IOException;

public class DogfightPausedWidget extends WidgetObserver implements IButtonObserver {
    private static final String RESUME_DOGFIGHT_BUTTON = "resumeDogfight";
    private static final String MAIN_MENU_BUTTON = "mainMenuButton";

    private final IDogfightPausedObserver _observer;

    public DogfightPausedWidget(WidgetManager widgetManager, IDogfightPausedObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        WidgetCreateInfo child = wci.getChild(RESUME_DOGFIGHT_BUTTON, "ButtonWidget");
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
            case RESUME_DOGFIGHT_BUTTON -> _observer.resumeDogfightButtonClicked();
            case MAIN_MENU_BUTTON -> _observer.mainMenuButtonClicked();
        }
    }

    @Override
    public void freeNativeResources() {
        super.freeNativeResources();
        // anything to do?
    }
}
