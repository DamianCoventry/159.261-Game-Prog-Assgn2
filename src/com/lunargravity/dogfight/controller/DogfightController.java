//
// Lunar Gravity
//
// This game is based upon the Amiga video game Gravity Force that was
// released in 1989 by Stephan Wenzler
//
// https://www.mobygames.com/game/gravity-force
// https://www.youtube.com/watch?v=m9mFtCvnko8
//
// This implementation is Copyright (c) 2021, Damian Coventry
// All rights reserved
// Written for Massey University course 159.261 Game Programming (Assignment 2)
//

package com.lunargravity.dogfight.controller;

import com.lunargravity.dogfight.model.IDogfightModel;

import java.util.LinkedList;

public class DogfightController implements IDogfightController {
    private final LinkedList<IDogfightControllerObserver> _observers;
    private final IDogfightModel _model;

    public DogfightController(IDogfightModel model) {
        _model = model;
        _observers = new LinkedList<>();
    }

    @Override
    public void controllerThink() {
        // TODO
    }

    @Override
    public void levelCompleted() {
        // Nothing to do
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
        _model.incrementLevel();
        for (var observer : _observers) {
            observer.startNextDogfightRequested(_model.getNumPlayers());
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

    @Override
    public void killPlayer(int i) {
        // TODO
    }
}
