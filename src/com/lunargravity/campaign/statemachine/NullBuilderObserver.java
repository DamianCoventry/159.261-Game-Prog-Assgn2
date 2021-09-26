package com.lunargravity.campaign.statemachine;

import com.lunargravity.engine.scene.ISceneBuilderObserver;

public class NullBuilderObserver implements ISceneBuilderObserver {
    @Override
    public void freeResources() {
        // Nothing to do
    }

    @Override
    public void sceneBuildBeginning() {
        // Nothing to do
    }

    @Override
    public void sceneBuildEnded() {
        // Nothing to do
    }

    @Override
    public void sceneBuildProgressed(int currentItem, int totalItems) {
        // Nothing to do
    }
}
