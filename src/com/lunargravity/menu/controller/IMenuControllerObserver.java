package com.lunargravity.menu.controller;

public interface IMenuControllerObserver {
    void startNewCampaignRequested(int numPlayers);
    void loadExistingCampaignRequested(String fileName);
    void viewRaceScoreboardRequested();
    void viewDogfightScoreboardRequested();
}
