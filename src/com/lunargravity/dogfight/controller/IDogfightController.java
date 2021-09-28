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

package com.lunargravity.dogfight.controller;

import com.lunargravity.mvc.IController;

public interface IDogfightController extends IController {
    void addObserver(IDogfightControllerObserver observer);
    void removeObserver(IDogfightControllerObserver observer);

    void startNextDogfight();
    void resumeDogfight();

    void mainMenuRequested();

    boolean isHighScore(int killCount);
    boolean insertDogfightHighScore(int killCount);
    void saveDogfightScoreboard(String fileName);
    void loadDogfightScoreboard(String fileName);

    void killPlayer(int i);
}
