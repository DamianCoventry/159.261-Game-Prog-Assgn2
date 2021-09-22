package com.lunargravity.dogfight.view;

import com.lunargravity.mvc.IView;

public interface IDogfightView extends IView {
    void showResultsWidget();
    void showScoreboardWidget();
    void showPausedWidget();
    void showGetReadyWidget(int countdown);
    void showCompletedWidget();
}
