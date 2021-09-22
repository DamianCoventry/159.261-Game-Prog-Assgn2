package com.lunargravity.race.view;

import com.lunargravity.mvc.IView;

public interface IRaceView extends IView {
    void showResultsWidget();
    void showScoreboardWidget();
    void showPausedWidget();
    void showGetReadyWidget(int countdown);
    void showCompletedWidget();
}
