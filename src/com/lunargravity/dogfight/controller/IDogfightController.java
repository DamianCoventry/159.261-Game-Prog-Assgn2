package com.lunargravity.dogfight.controller;

import com.lunargravity.mvc.IController;

public interface IDogfightController extends IController {
    void addObserver(IDogfightControllerObserver observer);
    void removeObserver(IDogfightControllerObserver observer);

    void startNewDogfight(int numPlayers);
    void startNextDogfight();
    void resumeDogfight();

    void goToMainMenu();
    void goToDogfightScoreboard();

    boolean isHighScore(int killCount);
    void resetDogfightScoreboard();
    boolean insertDogfightHighScore(int killCount);
    void saveDogfightScoreboard(String fileName);
    void loadDogfightScoreboard(String fileName);
}
