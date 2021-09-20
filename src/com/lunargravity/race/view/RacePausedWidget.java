package com.lunargravity.race.view;

import com.lunargravity.engine.widgetsystem.*;

public class RacePausedWidget extends WidgetObserver {
    private IRacePausedObserver _observer;

    public RacePausedWidget(WidgetManager widgetManager, IRacePausedObserver observer) {
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

    getChild("RaceGame").onClick(() -> { _observer.onButtonRaceGame(); });

    getChild("options").onClick(() -> { _observer.onButtonOptions(); });

    getChild("exit").onClick(() -> { _observer.onButtonExit(); });
    */
}
