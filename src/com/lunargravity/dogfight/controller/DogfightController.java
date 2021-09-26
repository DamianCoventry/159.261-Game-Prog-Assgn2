package com.lunargravity.dogfight.controller;

import com.lunargravity.dogfight.model.IDogfightModel;
import com.lunargravity.engine.scene.ISceneLogicOwner;

import java.util.LinkedList;

public class DogfightController implements IDogfightController, ISceneLogicOwner {
    private final LinkedList<IDogfightControllerObserver> _observers;
    private final IDogfightModel _model;

    public DogfightController(IDogfightModel model) {
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
    public void addObserver(IDogfightControllerObserver observer) {
        _observers.add(observer);
    }

    @Override
    public void removeObserver(IDogfightControllerObserver observer) {
        _observers.remove(observer);
    }

    @Override
    public void startNextDogfight() {
        for (var observer : _observers) {
            observer.startNextDogfightRequested();
        }
    }

    @Override
    public void resumeDogfight() {
        for (var observer : _observers) {
            observer.resumeDogfightRequested();
        }
    }

    @Override
    public void mainMenuRequested() {
        for (var observer : _observers) {
            observer.mainMenuRequested();
        }
    }

    @Override
    public boolean isHighScore(int killCount) {
        return _model.isHighScore(killCount);
    }

    @Override
    public boolean insertDogfightHighScore(int killCount) {
        return _model.insertDogfightHighScore(killCount);
    }

    @Override
    public void saveDogfightScoreboard(String fileName) {
        _model.saveDogfightScoreboard(fileName);
    }

    @Override
    public void loadDogfightScoreboard(String fileName) {
        _model.loadDogfightScoreboard(fileName);
    }
}
