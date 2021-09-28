package com.lunargravity.dogfight.model;

import com.lunargravity.engine.scene.ISceneStateOwner;

import java.time.LocalDate;

public class DogfightModel implements IDogfightModel, ISceneStateOwner {
    public static final int NUM_HIGH_SCORES = 5;

    private int _level;
    private final int _numPlayers;
    private DogfightHighScore[] _dogfightHighScores;

    public DogfightModel(int level, int numPlayers) {
        _level = level;
        _numPlayers = numPlayers;
        allocateDogfightScoreboard();
        resetDogfightScoreboard();
    }

    @Override
    public int getLevel() {
        return _level;
    }

    @Override
    public int getNumPlayers() {
        return _numPlayers;
    }

    @Override
    public void incrementLevel() {
        if (++_level >= NUM_LEVELS) {
            _level = 0;
        }
    }

    @Override
    public String getWorldLevelScene() {
        return String.format("scenes/DogfightWorld%02d.json", _level);
    }

    @Override
    public String getLogicLevelScene() {
        return String.format("scenes/DogfightLogic%02d.json", _level);
    }

    @Override
    public boolean isHighScore(int killCount) {
        return findInsertLocation(killCount) >= 0;
    }

    @Override
    public void resetDogfightScoreboard() {
        int killCount = 25;
        LocalDate today = LocalDate.now();
        for (var highScore : _dogfightHighScores) {
            highScore._dateOfHighScore = today;
            highScore._highScore = killCount;
            killCount -= 5;
        }
    }

    @Override
    public boolean insertDogfightHighScore(int killCount) {
        int index = findInsertLocation(killCount);
        if (index < 0) {
            return false; // Not a high score
        }
        moveHighScoresDown(index);
        _dogfightHighScores[index]._highScore = killCount;
        _dogfightHighScores[index]._dateOfHighScore = LocalDate.now();
        return true;
    }

    @Override
    public void saveDogfightScoreboard(String fileName) {
        // TODO
    }

    @Override
    public void loadDogfightScoreboard(String fileName) {
        // TODO
    }

    @Override
    public String modelToJson() {
        // TODO
        return null;
    }

    @Override
    public void modelFromJson(String json) {
        // TODO
    }

    @Override
    public void stateSettingLoaded(String name, String value) {
        // TODO
    }

    private void allocateDogfightScoreboard() {
        _dogfightHighScores = new DogfightHighScore[NUM_HIGH_SCORES];
        for (int i = 0; i < NUM_HIGH_SCORES; ++i) {
            _dogfightHighScores[i] = new DogfightHighScore();
        }
    }

    private int findInsertLocation(int killCount) {
        for (int i = 0; i < _dogfightHighScores.length; ++i) {
            if (killCount > _dogfightHighScores[i]._highScore) {
                return i;
            }
        }
        return -1; // Not a high score
    }

    private void moveHighScoresDown(int index) {
        for (int i = _dogfightHighScores.length - 1; i > 0 && i > index; --i) {
            _dogfightHighScores[i]._highScore = _dogfightHighScores[i - 1]._highScore;
            _dogfightHighScores[i]._dateOfHighScore = _dogfightHighScores[i - 1]._dateOfHighScore;
        }
    }
}
