package com.lunargravity.menu.controller;

import com.lunargravity.mvc.IController;

public interface IMenuController extends IController {
    void addObserver(IMenuControllerObserver observer);
    void removeObserver(IMenuControllerObserver observer);

    void startNewCampaign(int numPlayers);
    void loadExistingCampaign(String fileName);
    void viewRaceScoreboard();
    void viewDogfightScoreboard();

    void enableSound();
    void disableSound();
    void setSoundVolume(int volume);

    void enableMusic();
    void disableMusic();
    void setMusicVolume(int volume);

    void setPlayerRotateLeftKey(int player, int key);
    void setPlayerRotateRightKey(int player, int key);
    void setPlayerThrustKey(int player, int key);
    void setPlayerShootKey(int player, int key);
    void setPlayerKickKey(int player, int key);
    void setDefaultPlayerKeys();

    void exitApplication();
}
