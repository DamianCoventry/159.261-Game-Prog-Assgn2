package com.lunargravity.menu.view;

public interface ICampaignWidgetObserver {
    void singlePlayerCampaignButtonClicked();
    void twoPlayersCampaignButtonClicked();
    void loadSavedCampaignButtonClicked(String fileName);
    void mainMenuButtonClicked();
}
