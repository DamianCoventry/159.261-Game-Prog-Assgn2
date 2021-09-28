package com.lunargravity.race.controller;

public interface IRaceControllerObserver {
    void startNextRaceRequested(int numPlayers);
    void resumeRaceRequested();
    void mainMenuRequested();
}
