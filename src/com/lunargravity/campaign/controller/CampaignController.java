package com.lunargravity.campaign.controller;

import com.lunargravity.campaign.model.ICampaignModel;
import com.lunargravity.campaign.view.ICampaignView;
import com.lunargravity.engine.core.IEngine;
import com.lunargravity.engine.scene.ISceneLogicOwner;

import java.io.IOException;
import java.util.LinkedList;

public class CampaignController implements ICampaignController, ISceneLogicOwner {
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
    public void onControllerThink() {
        // TODO
    }

    @Override
    public void logicSettingLoaded(String name, String value) {
        // TODO
    }

    @Override
    public void episodeIntroAborted() {
        for (var observer : _observers) {
            observer.episodeIntroAborted();
        }
    }

    @Override
    public void episodeOutroAborted() throws IOException, InterruptedException {
        for (var observer : _observers) {
            observer.episodeOutroAborted();
        }
    }

    @Override
    public void completeEpisode() throws IOException, InterruptedException {
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
    public void completeMission() {
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
                observer.playerShipSpawned();
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
