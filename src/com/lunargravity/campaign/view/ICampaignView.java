package com.lunargravity.campaign.view;

import com.lunargravity.mvc.IView;

import java.io.IOException;

public interface ICampaignView extends IView {
    void showEpisodeIntro();
    void showEpisodeOutro();
    void showMissionIntro();
    void showGameWon();
    void showGameOver();
    void showMissionPaused();
    void showMissionCompleted();
    void showGetReady(int i) throws IOException;
    enum WhichPlayer { PLAYER_1, PLAYER_2, BOTH_PLAYERS }
    void showPlayerDied(WhichPlayer whichPlayer) throws IOException;
}
