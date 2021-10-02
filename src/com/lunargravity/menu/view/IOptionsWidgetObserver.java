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

package com.lunargravity.menu.view;

public interface IOptionsWidgetObserver {
    void enableSoundCheckboxSet();
    void enableSoundCheckboxCleared();
    void soundVolumeNumericChanged(int volume);
    void enableMusicCheckboxSet();
    void enableMusicCheckboxCleared();
    void musicVolumeNumericChanged(int volume);
    enum Binding { ROTATE_CW, ROTATE_CCW, THRUST, KICK, SHOOT }

    void playerKeyBindingChanged(int player, Binding binding, int key);
    void setDefaultPlayerKeysButtonClicked();
    void mainMenuButtonClicked();
}
