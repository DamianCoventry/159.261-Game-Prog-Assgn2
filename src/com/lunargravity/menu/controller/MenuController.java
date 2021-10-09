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

package com.lunargravity.menu.controller;

import com.lunargravity.application.PlayerInputBindings;
import com.lunargravity.engine.core.IEngine;
import com.lunargravity.menu.model.IMenuModel;

import java.util.LinkedList;

public class MenuController implements IMenuController {
    private final LinkedList<IMenuControllerObserver> _observers;
    private final IEngine _engine;
    private final PlayerInputBindings _playerInputBindings;
    private final IMenuModel _model;

    public MenuController(IEngine engine, PlayerInputBindings playerInputBindings, IMenuModel model) {
        _engine = engine;
        _playerInputBindings = playerInputBindings;
        _model = model;
        _observers = new LinkedList<>();
    }

    @Override
    public void addObserver(IMenuControllerObserver observer) {
        _observers.add(observer);
    }

    @Override
    public void removeObserver(IMenuControllerObserver observer) {
        _observers.remove(observer);
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
    public void startNewCampaign(int numPlayers) {
        for (var observer : _observers) {
            observer.startNewCampaignRequested(numPlayers);
        }
    }

    @Override
    public void loadExistingCampaign(String fileName) {
        for (var observer : _observers) {
            observer.loadExistingCampaignRequested(fileName);
        }
    }

    @Override
    public void startNewRace(int numPlayers) {
        for (var observer : _observers) {
            observer.startNewRaceRequested(numPlayers);
        }
    }

    @Override
    public void resetRaceScoreboard() {
        _model.resetRaceScoreboard();
    }

    @Override
    public void startNewDogfight(int numPlayers) {
        for (var observer : _observers) {
            observer.startNewDogfightRequested(numPlayers);
        }
    }

    @Override
    public void resetDogfightScoreboard() {
        _model.resetDogfightScoreboard();
    }

    @Override
    public void enableSound() {
        _engine.enableSound();
    }

    @Override
    public void disableSound() {
        _engine.disableSound();
    }

    @Override
    public void setSoundVolume(int volume) {
        _engine.setSoundVolume(volume);
    }

    @Override
    public void enableMusic() {
        _engine.enableMusic();
    }

    @Override
    public void disableMusic() {
        _engine.disableMusic();
    }

    @Override
    public void setMusicVolume(int volume) {
        _engine.setMusicVolume(volume);
    }

    @Override
    public void setPlayerRotateLeftKey(int player, int key) {
        _playerInputBindings.setPlayerRotateLeftKey(player, key);
    }

    @Override
    public void setPlayerRotateRightKey(int player, int key) {
        _playerInputBindings.setPlayerRotateRightKey(player, key);
    }

    @Override
    public void setPlayerThrustKey(int player, int key) {
        _playerInputBindings.setPlayerThrustKey(player, key);
    }

    @Override
    public void setPlayerShootKey(int player, int key) {
        _playerInputBindings.setPlayerShootKey(player, key);
    }

    @Override
    public void setPlayerKickKey(int player, int key) {
        _playerInputBindings.setPlayerKickKey(player, key);
    }

    @Override
    public void setDefaultPlayerKeys() {
        _playerInputBindings.setDefaults();
    }

    @Override
    public PlayerInputBindings getPlayerInputBindings() {
        return _playerInputBindings;
    }

    @Override
    public void exitApplication() {
        _engine.exit();
    }
}
