package com.lunargravity.race.model;

import com.lunargravity.mvc.IModel;

import java.time.LocalTime;

public interface IRaceModel extends IModel {
    int getNumPlayers();
    boolean isHighScore(LocalTime elapsedTime);
    void resetRaceScoreboard();
    boolean insertRaceHighScore(LocalTime elapsedTime);
    void saveRaceScoreboard(String fileName);
    void loadRaceScoreboard(String fileName);
}
