package com.lunargravity.race.view;

import com.lunargravity.engine.widgetsystem.*;

public class RaceScoreboardWidget extends WidgetObserver {
    private IRaceScoreboardObserver _observer;

    public RaceScoreboardWidget(WidgetManager widgetManager, IRaceScoreboardObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void createChildWidgets(WidgetCreateInfo wci) {
        // TODO: need to examine the wci structure and pass the correct info to each of these ctor calls
    }

    @Override
    public void freeResources() {
        // TODO
    }

    /* TODO
    getChild("campaignGame").onClick(() -> { _observer.onButtonCampaignGame(); });

    getChild("raceGame").onClick(() -> { _observer.onButtonRaceGame(); });

    getChild("RaceGame").onClick(() -> { _observer.onButtonRaceGame(); });

    getChild("options").onClick(() -> { _observer.onButtonOptions(); });

    getChild("exit").onClick(() -> { _observer.onButtonExit(); });
    */
}
