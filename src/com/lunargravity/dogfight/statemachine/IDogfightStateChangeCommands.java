package com.lunargravity.dogfight.statemachine;

public interface IDogfightStateChangeCommands {
    void startNextDogfight();
    void resetDogfightScoreboard();
    void resumeDogfight();
    void quitDogfight();
}
