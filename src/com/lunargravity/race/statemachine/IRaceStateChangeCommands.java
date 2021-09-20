package com.lunargravity.race.statemachine;

public interface IRaceStateChangeCommands {
    void startNextRace();
    void resetRaceScoreboard();
    void resumeRace();
    void quitRace();
}
