package com.lunargravity.race.controller;

public interface IRaceControllerObserver {
    void startNextRaceRequested();
    void resumeRaceRequested();
    void mainMenuRequested();
}
