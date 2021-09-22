package com.lunargravity.dogfight.model;

import com.lunargravity.mvc.IModel;

public interface IDogfightModel extends IModel {
    int getNumPlayers();
    boolean isHighScore(int killCount);
    void resetDogfightScoreboard();
    boolean insertDogfightHighScore(int killCount);
    void saveDogfightScoreboard(String fileName);
    void loadDogfightScoreboard(String fileName);
}
