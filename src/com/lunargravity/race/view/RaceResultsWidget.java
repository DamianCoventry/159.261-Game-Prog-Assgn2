package com.lunargravity.race.view;

import com.lunargravity.engine.widgetsystem.*;

public class RaceResultsWidget extends WidgetObserver {
    private IRaceResultsObserver _observer;

    public RaceResultsWidget(WidgetManager widgetManager, IRaceResultsObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void createChildWidgets(WidgetCreateInfo wci) {
        // TODO: need to examine the wci structure and pass the correct info to each of these ctor calls
    }

    /* TODO
    getChild("campaignGame").onClick(() -> { _observer.onButtonCampaignGame(); });

    getChild("raceGame").onClick(() -> { _observer.onButtonRaceGame(); });

    getChild("dogfightGame").onClick(() -> { _observer.onButtonDogfightGame(); });

    getChild("options").onClick(() -> { _observer.onButtonOptions(); });

    getChild("exit").onClick(() -> { _observer.onButtonExit(); });
    */
}
