package com.lunargravity.menu.view;

public interface IOptionsWidgetObserver {
    void enableSoundCheckboxSet();
    void enableSoundCheckboxCleared();
    void soundVolumeNumericChanged(int volume);
    void enableMusicCheckboxSet();
    void enableMusicCheckboxCleared();
    void musicVolumeNumericChanged(int volume);
    enum Binding { ROTATE_CW, ROTATE_CCW, THRUST, KICK, SHOOT };
    void playerKeyBindingChanged(int player, Binding binding, int key);
    void setDefaultPlayerKeysButtonClicked();
    void mainMenuButtonClicked();
}
