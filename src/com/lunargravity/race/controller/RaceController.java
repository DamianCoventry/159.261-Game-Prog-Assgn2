package com.lunargravity.race.controller;

import com.lunargravity.engine.scene.ISceneLogicOwner;
import com.lunargravity.race.model.IRaceModel;

import java.time.LocalTime;
import java.util.LinkedList;

public class RaceController implements IRaceController, ISceneLogicOwner {
    private final LinkedList<IRaceControllerObserver> _observers;
    private final IRaceModel _model;

    public RaceController(IRaceModel model) {
        _model = model;
        _observers = new LinkedList<>();
    }

    @Override
    public void onControllerThink() {
        // TODO
    }

    @Override
    public void logicSettingLoaded(String name, String value) {
        // TODO
    }

    @Override
    public void addObserver(IRaceControllerObserver observer) {
        _observers.add(observer);
    }

    @Override
    public void removeObserver(IRaceControllerObserver observer) {
        _observers.remove(observer);
    }

    @Override
    public void startNextRace() {
        for (var observer : _observers) {
            observer.startNextRaceRequested();
        }
    }

    @Override
    public void resumeRace() {
        for (var observer : _observers) {
            observer.resumeRaceRequested();
        }
    }

    @Override
    public void mainMenuRequested() {
        for (var observer : _observers) {
            observer.mainMenuRequested();
        }
    }

    @Override
    public boolean isHighScore(LocalTime elapsedTime) {
        return _model.isHighScore(elapsedTime);
    }

    @Override
    public boolean insertRaceHighScore(LocalTime elapsedTime) {
        return _model.insertRaceHighScore(elapsedTime);
    }

    @Override
    public void saveRaceScoreboard(String fileName) {
        _model.saveRaceScoreboard(fileName);
    }

    @Override
    public void loadRaceScoreboard(String fileName) {
        _model.loadRaceScoreboard(fileName);
    }
}
