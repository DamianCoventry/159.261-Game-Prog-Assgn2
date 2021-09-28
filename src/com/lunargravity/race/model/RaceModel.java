package com.lunargravity.race.model;

import com.lunargravity.engine.scene.ISceneStateOwner;

import java.time.LocalDate;
import java.time.LocalTime;

public class RaceModel implements IRaceModel, ISceneStateOwner {
    public static final int NUM_HIGH_SCORES = 5;

    private int _level;
    private final int _numPlayers;
    private RaceHighScore[] _raceHighScores;

    public RaceModel(int level, int numPlayers) {
        _level = level;
        _numPlayers = numPlayers;
        allocateRaceScoreboard();
        resetRaceScoreboard();
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
        return String.format("scenes/RaceWorld%02d.json", _level);
    }

    @Override
    public String getLogicLevelScene() {
        return String.format("scenes/RaceLogic%02d.json", _level);
    }

    @Override
    public boolean isHighScore(LocalTime elapsedTime) {
        return findInsertLocation(elapsedTime) >= 0;
    }

    @Override
    public void resetRaceScoreboard() {
        int minute = 2;
        LocalDate today = LocalDate.now();
        for (var highScore : _raceHighScores) {
            highScore._dateOfHighScore = today;
            highScore._highScore = LocalTime.of(0, minute++, 0, 0);
        }
    }

    @Override
    public boolean insertRaceHighScore(LocalTime elapsedTime) {
        int index = findInsertLocation(elapsedTime);
        if (index < 0) {
            return false; // Not a high score
        }
        moveHighScoresDown(index);
        _raceHighScores[index]._highScore = elapsedTime;
        _raceHighScores[index]._dateOfHighScore = LocalDate.now();
        return true;
    }

    @Override
    public void saveRaceScoreboard(String fileName) {
        // TODO
    }

    @Override
    public void loadRaceScoreboard(String fileName) {
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

    private void allocateRaceScoreboard() {
        _raceHighScores = new RaceHighScore[NUM_HIGH_SCORES];
        for (int i = 0; i < NUM_HIGH_SCORES; ++i) {
            _raceHighScores[i] = new RaceHighScore();
        }
    }

    private int findInsertLocation(LocalTime elapsedTime) {
        for (int i = 0; i < _raceHighScores.length; ++i) {
            if (elapsedTime.isBefore(_raceHighScores[i]._highScore)) {
                return i;
            }
        }
        return -1; // Not a high score
    }

    private void moveHighScoresDown(int index) {
        for (int i = _raceHighScores.length - 1; i > 0 && i > index; --i) {
            _raceHighScores[i]._highScore = _raceHighScores[i - 1]._highScore;
            _raceHighScores[i]._dateOfHighScore = _raceHighScores[i - 1]._dateOfHighScore;
        }
    }
}
