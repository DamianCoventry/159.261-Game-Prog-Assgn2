package com.lunargravity.application;

import com.lunargravity.engine.scene.ISceneBuilderObserver;
import com.lunargravity.race.view.RaceBuilderObserver;

import java.io.IOException;

public interface IApplicationModes {
    void startMenu(ISceneBuilderObserver sceneBuilderObserver) throws IOException, InterruptedException;
    void createCampaignMvc(String fileName) throws IOException, InterruptedException;
    void createCampaignMvc(int numPlayers) throws IOException, InterruptedException;
    void loadCampaignEpisode(ISceneBuilderObserver sceneBuilderObserver) throws IOException, InterruptedException;
    void loadCampaignMission(ISceneBuilderObserver sceneBuilderObserver) throws IOException, InterruptedException;
    void startRaceGame(ISceneBuilderObserver sceneBuilderObserver, int numPlayers) throws IOException, InterruptedException;
    void loadRaceLevel(ISceneBuilderObserver sceneBuilderObserver, int numPlayers) throws IOException, InterruptedException;
    void startDogfightGame(ISceneBuilderObserver sceneBuilderObserver, int numPlayers) throws IOException, InterruptedException;
}
