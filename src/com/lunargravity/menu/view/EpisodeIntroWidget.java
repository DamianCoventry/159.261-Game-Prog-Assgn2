//
// Lunar Gravity
//
// This game is based upon the Amiga video game Gravity Force that was
// released in 1989 by Stephan Wenzler
//
// https://www.mobygames.com/game/gravity-force
// https://www.youtube.com/watch?v=m9mFtCvnko8
//
// This implementation is Copyright (c) 2021, Damian Coventry
// All rights reserved
// Written for Massey University course 159.261 Game Programming (Assignment 2)
//

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
