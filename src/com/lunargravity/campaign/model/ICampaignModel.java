package com.lunargravity.campaign.model;

import com.lunargravity.mvc.IModel;

public interface ICampaignModel extends IModel {
    int NUM_EPISODES = 5;
    int NUM_MISSIONS_PER_EPISODE = 6;

    int getEpisode();
    int getMission();
    int getNumPlayers();

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
