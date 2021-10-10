package com.lunargravity.campaign.model;

import com.lunargravity.mvc.IModel;

public interface ICampaignModel extends IModel {
    int NUM_EPISODES = 4;
    int NUM_MISSIONS_PER_EPISODE = 3;

    void setEpisode(int i);
    void setMission(int i);
    int getEpisode();
    int getMission();
    int getNumPlayers();
    int getShipsRemaining(int playerId);

    enum DecrementPlayerShipResult { SHIP_AVAILABLE, SHIPS_EXHAUSTED }
    DecrementPlayerShipResult decrementPlayerShip(int i);

    enum IncrementEpisodeResult { START_NEXT_EPISODE, GAME_COMPLETED }
    IncrementEpisodeResult incrementEpisode();

    enum IncrementMissionResult { START_NEXT_MISSION, EPISODE_COMPLETED }
    IncrementMissionResult incrementMission();

    String getWorldMissionScene();
    String getLogicMissionScene();
    String getEpisodeIntroScene();
}
