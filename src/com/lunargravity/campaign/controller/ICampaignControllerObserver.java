package com.lunargravity.campaign.controller;

import java.io.IOException;

public interface ICampaignControllerObserver {
    void gameOver();
    void gameOverAborted();
    void gameCompleted();
    void gameCompletedAborted();

    void startNextEpisode() throws IOException, InterruptedException;
    void episodeIntroAborted();
    void missionIntroAborted();
    void episodeOutroAborted() throws IOException, InterruptedException;
    void episodeCompleted();

    void startNextMission();
    void resumeMission();
    void playerShipSpawned();

    void quitCampaign();
}
