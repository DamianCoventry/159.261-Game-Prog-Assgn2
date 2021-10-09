package com.lunargravity.campaign.controller;

import com.lunargravity.campaign.view.ICampaignView;
import com.lunargravity.mvc.IController;

import java.io.IOException;

public interface ICampaignController extends IController {
    void addObserver(ICampaignControllerObserver observer);
    void removeObserver(ICampaignControllerObserver observer);

    void gameOverAborted();
    void gameWonAborted();

    void episodeIntroAborted() throws Exception;
    void episodeOutroAborted() throws Exception;
    void completeEpisode() throws Exception;
    void skipToMission(int i) throws Exception;

    void missionIntroAborted();
    void resumeMission();
    void completeMission() throws Exception;
    void playerDied(ICampaignView.WhichPlayer whichPlayer);
    void spawnNextPlayerShip(ICampaignView.WhichPlayer whichPlayer);

    void quitCampaign();
}
