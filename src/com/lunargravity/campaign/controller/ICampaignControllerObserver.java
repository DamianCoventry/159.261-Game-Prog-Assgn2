package com.lunargravity.campaign.controller;

import com.lunargravity.campaign.view.ICampaignView;

public interface ICampaignControllerObserver {
    void gameOver();
    void gameOverAborted();
    void gameCompleted();
    void gameCompletedAborted();

    void startNextEpisode() throws Exception;
    void episodeIntroAborted() throws Exception;
    void missionIntroAborted();
    void episodeOutroAborted() throws Exception;
    void episodeCompleted();

    void startNextMission() throws Exception;
    void resumeMission();
    void missionCompleted();
    void playerDied(ICampaignView.WhichPlayer whichPlayer);
    void playerShipSpawned(ICampaignView.WhichPlayer whichPlayer);

    void quitCampaign();
}
