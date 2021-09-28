//
// Lunar Gravity
//
// This game is based upon the Amiga video game Gravity Force that was
// released in 1989 by Stephan Wenzler
//
// https://www.mobygames.com/game/gravity-force
// https://www.youtube.com/watch?v=m9mFtCvnko8
//
// This implementation is Copyright (c) 2021, Damian Coventry
// All rights reserved
// Written for Massey University course 159.261 Game Programming (Assignment 2)
//

package com.lunargravity.application;

import com.lunargravity.engine.scene.ISceneBuilderObserver;

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
    void loadDogfightLevel(ISceneBuilderObserver sceneBuilderObserver, int numPlayers) throws IOException, InterruptedException;
}
