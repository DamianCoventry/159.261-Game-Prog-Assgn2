package com.lunargravity.dogfight.view;

import com.lunargravity.mvc.IView;

import java.io.IOException;

public interface IDogfightView extends IView {
    void showResultsWidget() throws IOException;
    void showPausedWidget() throws IOException;
    void showGetReady(int i) throws IOException;
    void showCompletedWidget() throws IOException;
    void showLevelStatusBar();
}
