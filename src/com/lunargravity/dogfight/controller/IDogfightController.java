package com.lunargravity.dogfight.controller;

import com.lunargravity.mvc.IController;

import java.time.LocalTime;

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
