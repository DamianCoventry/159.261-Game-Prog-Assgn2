package com.lunargravity.race.controller;

import com.lunargravity.mvc.IController;

import java.time.LocalTime;

public interface IRaceController extends IController {
    void addObserver(IRaceControllerObserver observer);
    void removeObserver(IRaceControllerObserver observer);

    void startNextRace();
    void resumeRace();

    void mainMenuRequested();

    boolean isHighScore(LocalTime elapsedTime);
    boolean insertRaceHighScore(LocalTime elapsedTime);
    void saveRaceScoreboard(String fileName);
    void loadRaceScoreboard(String fileName);

    void killPlayer(int i);
}
