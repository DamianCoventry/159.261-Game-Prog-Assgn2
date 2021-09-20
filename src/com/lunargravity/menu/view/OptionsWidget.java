package com.lunargravity.menu.view;

import com.lunargravity.engine.widgetsystem.*;

public class OptionsWidget extends WidgetObserver implements
        ICheckObserver,
        INumericObserver,
        IKeyBindingObserver,
        IButtonObserver
{
    private static final String BACKGROUND_IMAGE = "backgroundImage";
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

    private Widget _backgroundImage;
    private Widget _enableSoundCheck;
    private Widget _soundVolumeNumeric;
    private Widget _enableMusicCheck;
    private Widget _musicVolumeNumeric;
    private Widget _p1RotateCwKeyboardKey;
    private Widget _p1RotateCcwKeyboardKey;
    private Widget _p1ThrustKeyboardKey;
    private Widget _p1KickKeyboardKey;
    private Widget _p1ShootKeyboardKey;
    private Widget _p2RotateCwKeyboardKey;
    private Widget _p2RotateCcwKeyboardKey;
    private Widget _p2ThrustKeyboardKey;
    private Widget _p2KickKeyboardKey;
    private Widget _p2ShootKeyboardKey;
    private Widget _resetToDefaultsButton;
    private Widget _mainMenuButton;

    public OptionsWidget(WidgetManager widgetManager, IOptionsWidgetObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void createChildWidgets(WidgetCreateInfo wci) {
        // TODO: need to examine the wci structure and pass the correct info to each of these ctor calls
        _backgroundImage = new Widget(wci, new ImageWidget(_widgetManager));
        _enableSoundCheck = new Widget(wci, new CheckWidget(_widgetManager, this));
        _soundVolumeNumeric = new Widget(wci, new NumericWidget(_widgetManager, this));
        _enableMusicCheck = new Widget(wci, new CheckWidget(_widgetManager, this));
        _musicVolumeNumeric = new Widget(wci, new NumericWidget(_widgetManager, this));
        _p1RotateCwKeyboardKey = new Widget(wci, new KeyBindingWidget(_widgetManager, this));
        _p1RotateCcwKeyboardKey = new Widget(wci, new KeyBindingWidget(_widgetManager, this));
        _p1ThrustKeyboardKey = new Widget(wci, new KeyBindingWidget(_widgetManager, this));
        _p1KickKeyboardKey = new Widget(wci, new KeyBindingWidget(_widgetManager, this));
        _p1ShootKeyboardKey = new Widget(wci, new KeyBindingWidget(_widgetManager, this));
        _p2RotateCwKeyboardKey = new Widget(wci, new KeyBindingWidget(_widgetManager, this));
        _p2RotateCcwKeyboardKey = new Widget(wci, new KeyBindingWidget(_widgetManager, this));
        _p2ThrustKeyboardKey = new Widget(wci, new KeyBindingWidget(_widgetManager, this));
        _p2KickKeyboardKey = new Widget(wci, new KeyBindingWidget(_widgetManager, this));
        _p2ShootKeyboardKey = new Widget(wci, new KeyBindingWidget(_widgetManager, this));
        _resetToDefaultsButton = new Widget(wci, new ButtonWidget(_widgetManager, this));
        _mainMenuButton = new Widget(wci, new ButtonWidget(_widgetManager, this));
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
        if (widgetId.equals(P1_ROTATE_CW_KEYBOARD_KEY)) {
            _observer.playerKeyBindingChanged(1, IOptionsWidgetObserver.Binding.ROTATE_CW, key);
        }
        else if (widgetId.equals(P1_ROTATE_CCW_KEYBOARD_KEY)) {
            _observer.playerKeyBindingChanged(1, IOptionsWidgetObserver.Binding.ROTATE_CCW, key);
        }
        else if (widgetId.equals(P1_THRUST_KEYBOARD_KEY)) {
            _observer.playerKeyBindingChanged(1, IOptionsWidgetObserver.Binding.THRUST, key);
        }
        else if (widgetId.equals(P1_KICK_KEYBOARD_KEY)) {
            _observer.playerKeyBindingChanged(1, IOptionsWidgetObserver.Binding.KICK, key);
        }
        else if (widgetId.equals(P1_SHOOT_KEYBOARD_KEY)) {
            _observer.playerKeyBindingChanged(1, IOptionsWidgetObserver.Binding.SHOOT, key);
        }
        else if (widgetId.equals(P2_ROTATE_CW_KEYBOARD_KEY)) {
            _observer.playerKeyBindingChanged(2, IOptionsWidgetObserver.Binding.ROTATE_CW, key);
        }
        else if (widgetId.equals(P2_ROTATE_CCW_KEYBOARD_KEY)) {
            _observer.playerKeyBindingChanged(2, IOptionsWidgetObserver.Binding.ROTATE_CCW, key);
        }
        else if (widgetId.equals(P2_THRUST_KEYBOARD_KEY)) {
            _observer.playerKeyBindingChanged(2, IOptionsWidgetObserver.Binding.THRUST, key);
        }
        else if (widgetId.equals(P2_KICK_KEYBOARD_KEY)) {
            _observer.playerKeyBindingChanged(2, IOptionsWidgetObserver.Binding.KICK, key);
        }
        else if (widgetId.equals(P2_SHOOT_KEYBOARD_KEY)) {
            _observer.playerKeyBindingChanged(2, IOptionsWidgetObserver.Binding.SHOOT, key);
        }
    }
}
