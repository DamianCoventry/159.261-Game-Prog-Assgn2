package com.lunargravity.menu.view;

import com.lunargravity.engine.widgetsystem.*;

public class MainWidget extends WidgetObserver implements IButtonObserver {
    private static final String BACKGROUND_IMAGE = "backgroundImage";
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
    protected void createChildWidgets(WidgetCreateInfo wci) {
        WidgetCreateInfo child = wci.getChild(BACKGROUND_IMAGE, "ImageWidget");
        if (child != null) {
            getWidget().addChild(new Widget(child, new ImageWidget(_widgetManager)));
        }
        child = wci.getChild(CAMPAIGN_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(RACE_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(DOGFIGHT_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(OPTIONS_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(EXIT_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(child, new ButtonWidget(_widgetManager, this)));
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
}
