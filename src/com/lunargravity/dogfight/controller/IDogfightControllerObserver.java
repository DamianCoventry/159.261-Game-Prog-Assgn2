package com.lunargravity.dogfight.controller;

public interface IDogfightControllerObserver {
    void startNextDogfightRequested();
    void resumeDogfightRequested();
    void mainMenuRequested();
}
