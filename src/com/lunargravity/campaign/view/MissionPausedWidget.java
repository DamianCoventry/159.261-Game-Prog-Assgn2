package com.lunargravity.campaign.view;

import com.lunargravity.engine.widgetsystem.*;

public class MissionPausedWidget extends WidgetObserver {
    private IMissionPausedObserver _observer;

    public MissionPausedWidget(WidgetManager widgetManager, IMissionPausedObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) {
        // TODO: need to examine the wci structure and pass the correct info to each of these ctor calls
    }

    @Override
    public void freeResources() {
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
