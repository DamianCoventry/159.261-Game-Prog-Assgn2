package com.lunargravity.race.controller;

import com.lunargravity.mvc.IController;

import java.time.LocalTime;

public interface IRaceController extends IController {
    void addObserver(IRaceControllerObserver observer);
    void removeObserver(IRaceControllerObserver observer);

    void startNewRace(int numPlayers);
    void startNextRace();
    void resumeRace();

    void goToMainMenu();
    void goToRaceScoreboard();

    boolean isHighScore(LocalTime elapsedTime);
    void resetRaceScoreboard();
    boolean insertRaceHighScore(LocalTime elapsedTime);
    void saveRaceScoreboard(String fileName);
    void loadRaceScoreboard(String fileName);
}
