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

package com.lunargravity.menu.controller;

import com.lunargravity.application.PlayerInputBindings;
import com.lunargravity.mvc.IController;

public interface IMenuController extends IController {
    void addObserver(IMenuControllerObserver observer);
    void removeObserver(IMenuControllerObserver observer);

    void startNewCampaign(int numPlayers);
    void loadExistingCampaign(String fileName);
    void startNewRace(int numPlayers);
    void resetRaceScoreboard();
    void startNewDogfight(int numPlayers);
    void resetDogfightScoreboard();

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
    PlayerInputBindings getPlayerInputBindings();

    void exitApplication();
}
