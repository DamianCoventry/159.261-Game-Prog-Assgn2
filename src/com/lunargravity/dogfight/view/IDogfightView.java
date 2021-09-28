package com.lunargravity.dogfight.view;

import com.lunargravity.mvc.IView;

import java.io.IOException;

public interface IDogfightView extends IView {
    void showResultsWidget();
    void showPausedWidget();
    void showGetReady(int i) throws IOException;
    void showCompletedWidget();
}
