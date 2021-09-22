package com.lunargravity.race.controller;

public interface IRaceControllerObserver {
    void startNewRaceRequested(int numPlayers);
    void startNextRaceRequested();
    void resumeRaceRequested();
    void goToMainMenuRequested();
    void goToRaceScoreboardRequested();
}
