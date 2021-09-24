package com.lunargravity.race.view;

import com.lunargravity.engine.widgetsystem.*;

import java.io.IOException;

public class RaceResultsWidget extends WidgetObserver {
    private IRaceResultsObserver _observer;

    public RaceResultsWidget(WidgetManager widgetManager, IRaceResultsObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        super.initialiseChildren(wci);
        // TODO: need to examine the wci structure and pass the correct info to each of these ctor calls
    }

    @Override
    public void freeResources() {
        super.freeResources();
        // TODO
    }

    /* TODO
    getChild("campaignGame").onClick(() -> { _observer.onButtonCampaignGame(); });

    getChild("raceGame").onClick(() -> { _observer.onButtonRaceGame(); });

    getChild("dogfightGame").onClick(() -> { _observer.onButtonDogfightGame(); });

    getChild("options").onClick(() -> { _observer.onButtonOptions(); });

    getChild("exit").onClick(() -> { _observer.onButtonExit(); });
    */
}
