package com.lunargravity.campaign.model;

public class CampaignModel implements ICampaignModel {
    public static final int INITIAL_SHIP_COUNT = 3;

    private int _episode;
    private int _mission;
    private final int[] _playerShipCounts;
    private final int _numPlayers;

    public CampaignModel(int episode, int mission, int numPlayers) {
        _episode = episode;
        _mission = mission;
        _numPlayers = numPlayers;
        _playerShipCounts = new int[2];
        _playerShipCounts[0] = INITIAL_SHIP_COUNT - 1; // Allocate a ship immediately
        _playerShipCounts[1] = INITIAL_SHIP_COUNT - 1;
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
    public void setMission(int i) {
        if (i >= 0 && i < NUM_MISSIONS_PER_EPISODE) {
            _mission = i;
        }
    }

    @Override
    public int getEpisode() {
        return _episode;
    }

    @Override
    public int getMission() {
        return _mission;
    }

    @Override
    public int getNumPlayers() {
        return _numPlayers;
    }

    @Override
    public int getShipsRemaining(int playerId) {
        return _playerShipCounts[playerId];
    }

    @Override
    public DecrementPlayerShipResult decrementPlayerShip(int player) {
        if (player < 0 || player > 1) {
            throw new IllegalArgumentException("player index is invalid");
        }
        if (_playerShipCounts[player] == 0) {
            return DecrementPlayerShipResult.SHIPS_EXHAUSTED;
        }
        --_playerShipCounts[player];
        return DecrementPlayerShipResult.SHIP_AVAILABLE;
    }

    @Override
    public IncrementEpisodeResult incrementEpisode() {
        if (_episode == NUM_EPISODES) {
            return IncrementEpisodeResult.GAME_COMPLETED;
        }
        ++_episode;
        _mission = 0;
        return _episode == NUM_EPISODES ?
                IncrementEpisodeResult.GAME_COMPLETED :
                IncrementEpisodeResult.START_NEXT_EPISODE;
    }

    @Override
    public IncrementMissionResult incrementMission() {
        if (_mission == NUM_MISSIONS_PER_EPISODE) {
            return IncrementMissionResult.EPISODE_COMPLETED;
        }
        ++_mission;
        return _mission == NUM_MISSIONS_PER_EPISODE ?
                IncrementMissionResult.EPISODE_COMPLETED :
                IncrementMissionResult.START_NEXT_MISSION;
    }

    @Override
    public String getWorldMissionScene() {
        return String.format("scenes/CampaignWorldE%dM%d.json", _episode, _mission);
    }

    @Override
    public String getLogicMissionScene() {
        return String.format("scenes/CampaignLogicE%dM%d.json", _episode, _mission);
    }

    @Override
    public String getEpisodeIntroScene() {
        return String.format("scenes/CampaignE%dIntro.json", _episode);
    }
}
