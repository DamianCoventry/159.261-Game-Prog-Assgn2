package com.lunargravity.engine.scene;

public interface ISceneBuilderObserver {
    void freeResources();
    void sceneBuildBeginning();
    void sceneBuildEnded();
    void sceneBuildProgressed(int currentItem, int totalItems);
}
