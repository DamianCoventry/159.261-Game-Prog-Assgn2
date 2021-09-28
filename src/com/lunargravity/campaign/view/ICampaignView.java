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
    void showMissionStatusBar();
    void showGetReady(int i) throws IOException;
    enum WhichPlayer { PLAYER_1, PLAYER_2, BOTH_PLAYERS }
    void showPlayerDied(WhichPlayer whichPlayer) throws IOException;
}
