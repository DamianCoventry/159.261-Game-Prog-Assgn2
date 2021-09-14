package com.lunargravity.engine.scene;

public interface ISceneBuilderObserver {
    void onSceneBuildBeginning();
    void onSceneBuildEnded();
    void onSceneBuildProgressed(int currentItem, int totalItems);
}
