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

import com.lunargravity.engine.widgetsystem.*;

import java.io.IOException;

public class OptionsWidget extends WidgetObserver implements
        ICheckObserver,
        INumericObserver,
        IKeyBindingObserver,
        IButtonObserver
{
    private static final String ENABLE_SOUND_CHECK = "enableSoundCheck";
    private static final String SOUND_VOLUME_NUMERIC = "soundVolumeNumeric";
    private static final String ENABLE_MUSIC_CHECK = "enableMusicCheck";
    private static final String MUSIC_VOLUME_NUMERIC = "musicVolumeNumeric";
    private static final String P1_ROTATE_CW_KEYBOARD_KEY = "p1RotateCwKeyboardKey";
    private static final String P1_ROTATE_CCW_KEYBOARD_KEY = "p1RotateCcwKeyboardKey";
    private static final String P1_THRUST_KEYBOARD_KEY = "p1ThrustKeyboardKey";
    private static final String P1_KICK_KEYBOARD_KEY = "p1KickKeyboardKey";
    private static final String P1_SHOOT_KEYBOARD_KEY = "p1ShootKeyboardKey";
    private static final String P2_ROTATE_CW_KEYBOARD_KEY = "p2RotateCwKeyboardKey";
    private static final String P2_ROTATE_CCW_KEYBOARD_KEY = "p2RotateCcwKeyboardKey";
    private static final String P2_THRUST_KEYBOARD_KEY = "p2ThrustKeyboardKey";
    private static final String P2_KICK_KEYBOARD_KEY = "p2KickKeyboardKey";
    private static final String P2_SHOOT_KEYBOARD_KEY = "p2ShootKeyboardKey";
    private static final String RESET_TO_DEFAULTS_BUTTON = "resetToDefaultsButton";
    private static final String MAIN_MENU_BUTTON = "mainMenuButton";

    private final IOptionsWidgetObserver _observer;

    public OptionsWidget(WidgetManager widgetManager, IOptionsWidgetObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        WidgetCreateInfo child = wci.getChild(ENABLE_SOUND_CHECK, "CheckWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new CheckWidget(_widgetManager, this)));
        }
        child = wci.getChild(SOUND_VOLUME_NUMERIC, "NumericWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new NumericWidget(_widgetManager, this)));
        }
        child = wci.getChild(ENABLE_MUSIC_CHECK, "CheckWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new CheckWidget(_widgetManager, this)));
        }
        child = wci.getChild(MUSIC_VOLUME_NUMERIC, "NumericWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new NumericWidget(_widgetManager, this)));
        }
        child = wci.getChild(P1_ROTATE_CW_KEYBOARD_KEY, "KeyBindingWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new KeyBindingWidget(_widgetManager, this)));
        }
        child = wci.getChild(P1_ROTATE_CCW_KEYBOARD_KEY, "KeyBindingWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new KeyBindingWidget(_widgetManager, this)));
        }
        child = wci.getChild(P1_THRUST_KEYBOARD_KEY, "KeyBindingWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new KeyBindingWidget(_widgetManager, this)));
        }
        child = wci.getChild(P1_KICK_KEYBOARD_KEY, "KeyBindingWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new KeyBindingWidget(_widgetManager, this)));
        }
        child = wci.getChild(P1_SHOOT_KEYBOARD_KEY, "KeyBindingWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new KeyBindingWidget(_widgetManager, this)));
        }
        child = wci.getChild(P2_ROTATE_CW_KEYBOARD_KEY, "KeyBindingWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new KeyBindingWidget(_widgetManager, this)));
        }
        child = wci.getChild(P2_ROTATE_CCW_KEYBOARD_KEY, "KeyBindingWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new KeyBindingWidget(_widgetManager, this)));
        }
        child = wci.getChild(P2_THRUST_KEYBOARD_KEY, "KeyBindingWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new KeyBindingWidget(_widgetManager, this)));
        }
        child = wci.getChild(P2_KICK_KEYBOARD_KEY, "KeyBindingWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new KeyBindingWidget(_widgetManager, this)));
        }
        child = wci.getChild(P2_SHOOT_KEYBOARD_KEY, "KeyBindingWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new KeyBindingWidget(_widgetManager, this)));
        }
        child = wci.getChild(RESET_TO_DEFAULTS_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(MAIN_MENU_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
    }

    @Override
    public void buttonClicked(String widgetId) {
        if (widgetId.equals(RESET_TO_DEFAULTS_BUTTON)) {
            _observer.setDefaultPlayerKeysButtonClicked();
        }
        else if (widgetId.equals(MAIN_MENU_BUTTON)) {
            _observer.mainMenuButtonClicked();
        }
    }

    @Override
    public void checkChanged(String widgetId, boolean checked) {
        if (widgetId.equals(ENABLE_SOUND_CHECK)) {
            if (checked) {
                _observer.enableSoundCheckboxSet();
            }
            else {
                _observer.enableSoundCheckboxCleared();
            }
        }
        else if (widgetId.equals(ENABLE_MUSIC_CHECK)) {
            if (checked) {
                _observer.enableMusicCheckboxSet();
            }
            else {
                _observer.enableMusicCheckboxCleared();
            }
        }
    }

    @Override
    public void numberChanged(String widgetId, int number) {
        if (widgetId.equals(SOUND_VOLUME_NUMERIC)) {
            _observer.soundVolumeNumericChanged(number);
        }
        else if (widgetId.equals(MUSIC_VOLUME_NUMERIC)) {
            _observer.musicVolumeNumericChanged(number);
        }
    }

    @Override
    public void keyBindingChanged(String widgetId, int key) {
        switch (widgetId) {
            case P1_ROTATE_CW_KEYBOARD_KEY -> _observer.playerKeyBindingChanged(1, IOptionsWidgetObserver.Binding.ROTATE_CW, key);
            case P1_ROTATE_CCW_KEYBOARD_KEY -> _observer.playerKeyBindingChanged(1, IOptionsWidgetObserver.Binding.ROTATE_CCW, key);
            case P1_THRUST_KEYBOARD_KEY -> _observer.playerKeyBindingChanged(1, IOptionsWidgetObserver.Binding.THRUST, key);
            case P1_KICK_KEYBOARD_KEY -> _observer.playerKeyBindingChanged(1, IOptionsWidgetObserver.Binding.KICK, key);
            case P1_SHOOT_KEYBOARD_KEY -> _observer.playerKeyBindingChanged(1, IOptionsWidgetObserver.Binding.SHOOT, key);
            case P2_ROTATE_CW_KEYBOARD_KEY -> _observer.playerKeyBindingChanged(2, IOptionsWidgetObserver.Binding.ROTATE_CW, key);
            case P2_ROTATE_CCW_KEYBOARD_KEY -> _observer.playerKeyBindingChanged(2, IOptionsWidgetObserver.Binding.ROTATE_CCW, key);
            case P2_THRUST_KEYBOARD_KEY -> _observer.playerKeyBindingChanged(2, IOptionsWidgetObserver.Binding.THRUST, key);
            case P2_KICK_KEYBOARD_KEY -> _observer.playerKeyBindingChanged(2, IOptionsWidgetObserver.Binding.KICK, key);
            case P2_SHOOT_KEYBOARD_KEY -> _observer.playerKeyBindingChanged(2, IOptionsWidgetObserver.Binding.SHOOT, key);
        }
    }

    @Override
    public void freeResources() {
        super.freeResources();
        // anything to do?
    }
}
