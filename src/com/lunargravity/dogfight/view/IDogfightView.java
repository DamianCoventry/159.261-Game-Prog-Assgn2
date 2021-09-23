package com.lunargravity.dogfight.view;

import com.lunargravity.mvc.IView;

import java.io.IOException;

public interface IDogfightView extends IView {
    void showResultsWidget();
    void showScoreboardWidget();
    void showPausedWidget();
    void showGetReadyWidget(int countdown) throws IOException;
    void showCompletedWidget();
}
