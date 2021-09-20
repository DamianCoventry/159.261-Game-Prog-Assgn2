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

    private Widget _backgroundImage;
    private Widget _campaignButton;
    private Widget _raceButton;
    private Widget _dogfightButton;
    private Widget _optionsButton;
    private Widget _exitButton;

    public MainWidget(WidgetManager widgetManager, IMainWidgetObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void createChildWidgets(WidgetCreateInfo wci) {
        // TODO: need to examine the wci structure and pass the correct info to each of these ctor calls
        _backgroundImage = new Widget(wci, new ImageWidget(_widgetManager));
        _campaignButton = new Widget(wci, new ButtonWidget(_widgetManager, this));
        _raceButton = new Widget(wci, new ButtonWidget(_widgetManager, this));
        _dogfightButton = new Widget(wci, new ButtonWidget(_widgetManager, this));
        _optionsButton = new Widget(wci, new ButtonWidget(_widgetManager, this));
        _exitButton = new Widget(wci, new ButtonWidget(_widgetManager, this));
    }

    @Override
    public void buttonClicked(String widgetId) {
        if (widgetId.equals(CAMPAIGN_BUTTON)) {
            _observer.campaignGameButtonClicked();
        }
        else if (widgetId.equals(RACE_BUTTON)) {
            _observer.raceGameButtonClicked();
        }
        else if (widgetId.equals(DOGFIGHT_BUTTON)) {
            _observer.dogfightGameButtonClicked();
        }
        else if (widgetId.equals(OPTIONS_BUTTON)) {
            _observer.optionsButtonClicked();
        }
        else if  (widgetId.equals(EXIT_BUTTON)) {
            _observer.exitApplicationButtonClicked();
        }
    }
}
