package com.lunargravity.campaign.controller;

import com.lunargravity.campaign.model.ICampaignModel;
import com.lunargravity.campaign.view.ICampaignView;
import com.lunargravity.engine.core.IEngine;

import java.util.LinkedList;

public class CampaignController implements ICampaignController {
    private final LinkedList<ICampaignControllerObserver> _observers;
    private final IEngine _engine;
    private final ICampaignModel _model;

    public CampaignController(IEngine engine, ICampaignModel model) {
        _engine = engine;
        _model = model;
        _observers = new LinkedList<>();
    }

    @Override
    public void addObserver(ICampaignControllerObserver observer) {
        _observers.add(observer);
    }

    @Override
    public void removeObserver(ICampaignControllerObserver observer) {
        _observers.remove(observer);
    }

    @Override
    public void controllerThink() {
        // TODO
    }

    @Override
    public void episodeIntroAborted() throws Exception {
        for (var observer : _observers) {
            observer.episodeIntroAborted();
        }
    }

    @Override
    public void episodeOutroAborted() throws Exception {
        for (var observer : _observers) {
            observer.episodeOutroAborted();
        }
    }

    @Override
    public void completeEpisode() throws Exception {
        if (_model.incrementEpisode() == ICampaignModel.IncrementEpisodeResult.GAME_COMPLETED) {
            for (var observer : _observers) {
                observer.gameCompleted();
            }
        }
        else {
            for (var observer : _observers) {
                observer.startNextEpisode();
            }
        }
    }

    @Override
    public void skipToMission(int i) throws Exception {
        _model.setMission(i);
        for (var observer : _observers) {
            observer.startNextEpisode();
        }
    }

    @Override
    public void gameOverAborted() {
        for (var observer : _observers) {
            observer.gameOverAborted();
        }
    }

    @Override
    public void gameWonAborted() {
        for (var observer : _observers) {
            observer.gameCompletedAborted();
        }
    }

    @Override
    public void missionIntroAborted() {
        for (var observer : _observers) {
            observer.missionIntroAborted();
        }
    }

    @Override
    public void resumeMission() {
        for (var observer : _observers) {
            observer.resumeMission();
        }
    }

    @Override
    public void levelCompleted() {
        for (var observer : _observers) {
            observer.missionCompleted();
        }
    }

    @Override
    public void completeMission() throws Exception {
        if (_model.incrementMission() == ICampaignModel.IncrementMissionResult.EPISODE_COMPLETED) {
            for (var observer : _observers) {
                observer.episodeCompleted();
            }
        }
        else {
            for (var observer : _observers) {
                observer.startNextMission();
            }
        }
    }

    @Override
    public void playerDied(ICampaignView.WhichPlayer whichPlayer) {
        for (var observer : _observers) {
            observer.playerDied(whichPlayer);
        }
    }

    @Override
    public void spawnNextPlayerShip(ICampaignView.WhichPlayer whichPlayer) {
        if (whichPlayer == ICampaignView.WhichPlayer.BOTH_PLAYERS) {
            throw new IllegalArgumentException("whichPlayer is BOTH_PLAYERS");
        }
        int player = (whichPlayer == ICampaignView.WhichPlayer.PLAYER_1 ? 0 : 1);
        if (_model.decrementPlayerShip(player) == ICampaignModel.DecrementPlayerShipResult.SHIPS_EXHAUSTED) {
            for (var observer : _observers) {
                observer.gameOver();
            }
        }
        else {
            for (var observer : _observers) {
                observer.playerShipSpawned(whichPlayer);
            }
        }
    }

    @Override
    public void quitCampaign() {
        for (var observer : _observers) {
            observer.quitCampaign();
        }
    }
}
