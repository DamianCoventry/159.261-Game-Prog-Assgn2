package com.lunargravity.campaign.controller;

import com.lunargravity.campaign.view.ICampaignView;
import com.lunargravity.mvc.IController;

import java.io.IOException;

public interface ICampaignController extends IController {
    void addObserver(ICampaignControllerObserver observer);
    void removeObserver(ICampaignControllerObserver observer);

    void gameOverAborted();
    void gameWonAborted();

    void episodeIntroAborted();
    void episodeOutroAborted() throws IOException, InterruptedException;
    void completeEpisode() throws IOException, InterruptedException;
    
    void missionIntroAborted();
    void resumeMission();
    void completeMission();
    void spawnNextPlayerShip(ICampaignView.WhichPlayer whichPlayer);

    void quitCampaign();
}
