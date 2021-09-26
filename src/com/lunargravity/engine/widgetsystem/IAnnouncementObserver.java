package com.lunargravity.engine.widgetsystem;

import java.io.IOException;

public interface IAnnouncementObserver {
    void announcementClicked(String widgetId) throws IOException, InterruptedException;
}
