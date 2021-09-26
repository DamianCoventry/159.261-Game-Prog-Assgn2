package com.lunargravity.menu.view;

import com.lunargravity.engine.widgetsystem.*;

import java.io.IOException;

// TODO: change this to be an AnnouncementWidget

public class EpisodeIntroWidget extends WidgetObserver {
    private static final String EPISODE_INTRO_TEXT = "episodeIntroText";

    protected EpisodeIntroWidget(WidgetManager widgetManager) {
        super(widgetManager);
    }

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        WidgetCreateInfo child = wci.getChild(EPISODE_INTRO_TEXT, "ImageWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ImageWidget(_widgetManager)));
        }
    }
}
