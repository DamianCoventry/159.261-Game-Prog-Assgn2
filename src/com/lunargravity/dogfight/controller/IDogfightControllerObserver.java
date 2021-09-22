package com.lunargravity.dogfight.controller;

public interface IDogfightControllerObserver {
    void startNewDogfightRequested(int numPlayers);
    void startNextDogfightRequested();
    void resumeDogfightRequested();
    void goToMainMenuRequested();
    void goToDogfightScoreboardRequested();
}
