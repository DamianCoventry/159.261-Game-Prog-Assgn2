package com.lunargravity.race.view;

import com.lunargravity.mvc.IView;

import java.io.IOException;

public interface IRaceView extends IView {
    void showResultsWidget();
    void showPausedWidget();
    void showGetReadyWidget(int countdown) throws IOException;
    void showCompletedWidget();
}
