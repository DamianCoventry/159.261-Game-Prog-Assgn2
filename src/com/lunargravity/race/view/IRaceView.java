package com.lunargravity.race.view;

import com.lunargravity.mvc.IView;

import java.io.IOException;

public interface IRaceView extends IView {
    void showResultsWidget();
    void showPausedWidget();
    void showGetReady(int i) throws IOException;
    void showCompletedWidget();
    void showLevelStatusBar();
}
