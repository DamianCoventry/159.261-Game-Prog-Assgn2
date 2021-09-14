package com.lunargravity.application;

import com.lunargravity.engine.scene.ISceneBuilderObserver;

public interface IApplicationModes {
    void startMenu(ISceneBuilderObserver sceneBuilderObserver);
    void startCampaignGame(ISceneBuilderObserver sceneBuilderObserver);
    void startRaceGame(ISceneBuilderObserver sceneBuilderObserver);
    void startDogfightGame(ISceneBuilderObserver sceneBuilderObserver);
}
