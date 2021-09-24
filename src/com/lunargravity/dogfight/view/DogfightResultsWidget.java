package com.lunargravity.dogfight.view;

import com.lunargravity.engine.widgetsystem.*;

import java.io.IOException;

public class DogfightResultsWidget extends WidgetObserver {
    private IDogfightResultsObserver _observer;

    public DogfightResultsWidget(WidgetManager widgetManager, IDogfightResultsObserver observer) {
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
