package com.lunargravity.application;

import com.lunargravity.engine.scene.ISceneBuilderObserver;

import java.io.IOException;

public interface IApplicationModes {
    void startMenu(ISceneBuilderObserver sceneBuilderObserver);
    void startCampaignGame(ISceneBuilderObserver sceneBuilderObserver, String fileName) throws IOException;
    void startCampaignGame(ISceneBuilderObserver sceneBuilderObserver, int numPlayers) throws IOException;
    void startRaceGame(ISceneBuilderObserver sceneBuilderObserver, int numPlayers);
    void startDogfightGame(ISceneBuilderObserver sceneBuilderObserver, int numPlayers);
}
