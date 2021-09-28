package com.lunargravity.dogfight.controller;

public interface IDogfightControllerObserver {
    void startNextDogfightRequested(int numPlayers);
    void resumeDogfightRequested();
    void mainMenuRequested();
}
