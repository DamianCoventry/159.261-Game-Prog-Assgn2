package com.lunargravity.dogfight.model;

import com.lunargravity.mvc.IModel;

public interface IDogfightModel extends IModel {
    int NUM_LEVELS = 10;

    int getLevel();
    int getNumPlayers();

    void incrementLevel();
    String getWorldLevelScene();
    String getLogicLevelScene();

    boolean isHighScore(int killCount);
    void resetDogfightScoreboard();
    boolean insertDogfightHighScore(int killCount);
    void saveDogfightScoreboard(String fileName);
    void loadDogfightScoreboard(String fileName);
}
