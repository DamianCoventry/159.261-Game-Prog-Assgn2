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

package com.lunargravity.engine.widgetsystem;

import com.lunargravity.engine.graphics.BitmapImage;
import com.lunargravity.engine.graphics.GlTexture;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.io.IOException;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class KeyBindingWidget extends WidgetObserver {
    public static final String PRESS_ANY_KEY_IMAGE = "pressAnyKeyImage";

    private final IKeyBindingObserver _observer;
    protected GlTexture _pressAnyKeyTexture;
    protected GlTexture _currentKeyTexture;
    private boolean _listening;
    private int _key;

    // Escape is used to pause the game, so don't allow it to be bound to some other action
    private static final HashMap<Integer, String> SUPPORTED_KEYS = new HashMap<>() {{
        put(GLFW_KEY_SPACE, "KeySpace.png");
        put(GLFW_KEY_APOSTROPHE, "KeyApostrophe.png");
        put(GLFW_KEY_COMMA, "KeyComma.png");
        put(GLFW_KEY_MINUS, "KeyMinus.png");
        put(GLFW_KEY_PERIOD, "KeyPeriod.png");
        put(GLFW_KEY_SLASH, "KeySlash.png");
        put(GLFW_KEY_0, "Key0.png");
        put(GLFW_KEY_1, "Key1.png");
        put(GLFW_KEY_2, "Key2.png");
        put(GLFW_KEY_3, "Key3.png");
        put(GLFW_KEY_4, "Key4.png");
        put(GLFW_KEY_5, "Key5.png");
        put(GLFW_KEY_6, "Key6.png");
        put(GLFW_KEY_7, "Key7.png");
        put(GLFW_KEY_8, "Key8.png");
        put(GLFW_KEY_9, "Key9.png");
        put(GLFW_KEY_SEMICOLON, "KeySemicolon.png");
        put(GLFW_KEY_EQUAL, "KeyEquals.png");
        put(GLFW_KEY_A, "KeyA.png");
        put(GLFW_KEY_B, "KeyB.png");
        put(GLFW_KEY_C, "KeyC.png");
        put(GLFW_KEY_D, "KeyD.png");
        put(GLFW_KEY_E, "KeyE.png");
        put(GLFW_KEY_F, "KeyF.png");
        put(GLFW_KEY_G, "KeyG.png");
        put(GLFW_KEY_H, "KeyH.png");
        put(GLFW_KEY_I, "KeyI.png");
        put(GLFW_KEY_J, "KeyJ.png");
        put(GLFW_KEY_K, "KeyK.png");
        put(GLFW_KEY_L, "KeyL.png");
        put(GLFW_KEY_M, "KeyM.png");
        put(GLFW_KEY_N, "KeyN.png");
        put(GLFW_KEY_O, "KeyO.png");
        put(GLFW_KEY_P, "KeyP.png");
        put(GLFW_KEY_Q, "KeyQ.png");
        put(GLFW_KEY_R, "KeyR.png");
        put(GLFW_KEY_S, "KeyS.png");
        put(GLFW_KEY_T, "KeyT.png");
        put(GLFW_KEY_U, "KeyU.png");
        put(GLFW_KEY_V, "KeyV.png");
        put(GLFW_KEY_W, "KeyW.png");
        put(GLFW_KEY_X, "KeyX.png");
        put(GLFW_KEY_Y, "KeyY.png");
        put(GLFW_KEY_Z, "KeyZ.png");
        put(GLFW_KEY_LEFT_BRACKET, "KeyLeftBracket.png");
        put(GLFW_KEY_BACKSLASH, "KeyBackSlash.png");
        put(GLFW_KEY_RIGHT_BRACKET, "KeyRightBracket.png");
        put(GLFW_KEY_GRAVE_ACCENT, "KeyGraveAccent.png");
        put(GLFW_KEY_WORLD_1, "KeyWorld1.png");
        put(GLFW_KEY_WORLD_2, "KeyWorld2.png");
        put(GLFW_KEY_ENTER, "KeyEnter.png");
        put(GLFW_KEY_TAB, "KeyTab.png");
        put(GLFW_KEY_BACKSPACE, "KeyBackspace.png");
        put(GLFW_KEY_INSERT, "KeyInsert.png");
        put(GLFW_KEY_DELETE, "KeyDelete.png");
        put(GLFW_KEY_RIGHT, "KeyRight.png");
        put(GLFW_KEY_LEFT, "KeyLeft.png");
        put(GLFW_KEY_DOWN, "KeyDown.png");
        put(GLFW_KEY_UP, "KeyUp.png");
        put(GLFW_KEY_PAGE_UP, "KeyPageUp.png");
        put(GLFW_KEY_PAGE_DOWN, "KeyPageDown.png");
        put(GLFW_KEY_HOME, "KeyHome.png");
        put(GLFW_KEY_END, "KeyEnd.png");
        put(GLFW_KEY_CAPS_LOCK, "KeyCapsLock.png");
        put(GLFW_KEY_SCROLL_LOCK, "KeyScrollLock.png");
        put(GLFW_KEY_NUM_LOCK, "KeyNumLock.png");
        put(GLFW_KEY_PRINT_SCREEN, "KeyPrintScreen.png");
        put(GLFW_KEY_PAUSE, "KeyPause.png");
        put(GLFW_KEY_F1, "KeyF1.png");
        put(GLFW_KEY_F2, "KeyF2.png");
        put(GLFW_KEY_F3, "KeyF3.png");
        put(GLFW_KEY_F4, "KeyF4.png");
        put(GLFW_KEY_F5, "KeyF5.png");
        put(GLFW_KEY_F6, "KeyF6.png");
        put(GLFW_KEY_F7, "KeyF7.png");
        put(GLFW_KEY_F8, "KeyF8.png");
        put(GLFW_KEY_F9, "KeyF9.png");
        put(GLFW_KEY_F10, "KeyF10.png");
        put(GLFW_KEY_F11, "KeyF11.png");
        put(GLFW_KEY_F12, "KeyF12.png");
        put(GLFW_KEY_F13, "KeyF13.png");
        put(GLFW_KEY_F14, "KeyF14.png");
        put(GLFW_KEY_F15, "KeyF15.png");
        put(GLFW_KEY_F16, "KeyF16.png");
        put(GLFW_KEY_F17, "KeyF17.png");
        put(GLFW_KEY_F18, "KeyF18.png");
        put(GLFW_KEY_F19, "KeyF19.png");
        put(GLFW_KEY_F20, "KeyF20.png");
        put(GLFW_KEY_F21, "KeyF21.png");
        put(GLFW_KEY_F22, "KeyF22.png");
        put(GLFW_KEY_F23, "KeyF23.png");
        put(GLFW_KEY_F24, "KeyF24.png");
        put(GLFW_KEY_F25, "KeyF25.png");
        put(GLFW_KEY_KP_0, "KeyKp0.png");
        put(GLFW_KEY_KP_1, "KeyKp1.png");
        put(GLFW_KEY_KP_2, "KeyKp2.png");
        put(GLFW_KEY_KP_3, "KeyKp3.png");
        put(GLFW_KEY_KP_4, "KeyKp4.png");
        put(GLFW_KEY_KP_5, "KeyKp5.png");
        put(GLFW_KEY_KP_6, "KeyKp6.png");
        put(GLFW_KEY_KP_7, "KeyKp7.png");
        put(GLFW_KEY_KP_8, "KeyKp8.png");
        put(GLFW_KEY_KP_9, "KeyKp9.png");
        put(GLFW_KEY_KP_DECIMAL, "KeyKpDecimal.png");
        put(GLFW_KEY_KP_DIVIDE, "KeyKpDivide.png");
        put(GLFW_KEY_KP_MULTIPLY, "KeyKpMultiply.png");
        put(GLFW_KEY_KP_SUBTRACT, "KeyKpSubtract.png");
        put(GLFW_KEY_KP_ADD, "KeyKpAdd.png");
        put(GLFW_KEY_KP_ENTER, "KeyKpEnter.png");
        put(GLFW_KEY_KP_EQUAL, "KeyKpEquals.png");
        put(GLFW_KEY_LEFT_SHIFT, "KeyLeftShift.png");
        put(GLFW_KEY_LEFT_CONTROL, "KeyLeftControl.png");
        put(GLFW_KEY_LEFT_ALT, "KeyLeftAlt.png");
        put(GLFW_KEY_LEFT_SUPER, "KeyLeftSuper.png");
        put(GLFW_KEY_RIGHT_SHIFT, "KeyRightShift.png");
        put(GLFW_KEY_RIGHT_CONTROL, "KeyRightControl.png");
        put(GLFW_KEY_RIGHT_ALT, "KeyRightAlt.png");
        put(GLFW_KEY_RIGHT_SUPER, "KeyRightSuper.png");
        put(GLFW_KEY_MENU, "KeyMenu.png");
    }};

    public KeyBindingWidget(WidgetManager widgetManager, IKeyBindingObserver observer) {
        super(widgetManager);
        _observer = observer;
        _listening = false;
        _key = 0;
    }

    public void setKey(int key) throws IOException {
        _key = key;
        _listening = false;
        loadTextureForCurrentKey();
    }
    public int getKey() {
        return _key;
    }

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        super.initialiseChildren(wci);
        String stringValue = wci._properties.get(PRESS_ANY_KEY_IMAGE);
        if (stringValue != null) {
            _pressAnyKeyTexture = new GlTexture(BitmapImage.fromFile(stringValue));
        }
    }

    @Override
    public void freeNativeResources() {
        super.freeNativeResources();
        if (_pressAnyKeyTexture != null) {
            _pressAnyKeyTexture.freeNativeResources();
        }
    }

    @Override
    public void mouseButtonEvent(int button, int action, int mods) throws Exception {
        boolean hadMouseCapture = _widget == _widgetManager.getMouseCapture();
        super.mouseButtonEvent(button, action, mods);
        if (action == GLFW_RELEASE && hadMouseCapture && _widget == _widgetManager.getHoveringOver()) {
            _listening = !_listening;
            if (_listening) {
                _widgetManager.setKeyboardFocus(_widget);
            }
            else {
                _widgetManager.setKeyboardFocus(null);
            }
        }
    }

    @Override
    public void keyboardKeyEvent(int key, int scancode, int action, int mods) throws IOException {
        super.keyboardKeyEvent(key, scancode, action, mods);
        if (_listening) {
            _listening = false;
            if (SUPPORTED_KEYS.containsKey(key)) {
                int oldKey = _key;
                _key = key;
                loadTextureForCurrentKey();
                _observer.keyBindingChanged(_widget.getId(), oldKey, _key);
            }
        }
    }

    @Override
    public void widgetDraw(Matrix4f projectionMatrix) {
        if (_backgroundTexture == null || _polyhedra == null) {
            return;
        }

        Vector2f viewportPosition = _widget.toViewportCoordinates(_widget.getPosition());

        _modelMatrix = _modelMatrix.identity().translate(viewportPosition.x, viewportPosition.y, 0.0f);
        // there is no view matrix when using an orthographic projection matrix
        Matrix4f mvpMatrix = projectionMatrix.mul(_modelMatrix);

        _widgetManager.getRenderer().activateTextureImageUnit(0);
        if (_listening && _pressAnyKeyTexture != null) {
            glBindTexture(GL_TEXTURE_2D, _pressAnyKeyTexture.getId());
        }
        else if (_hoverTexture != null && _widget == _widgetManager.getHoveringOver()) {
            glBindTexture(GL_TEXTURE_2D, _hoverTexture.getId());
        }
        else {
            glBindTexture(GL_TEXTURE_2D, _backgroundTexture.getId());
        }

        if (_backgroundAlpha.isCompleted()) {
            _program.setDiffuseColour(_backgroundColour);
        }
        else {
            _fadingColour.w = _backgroundAlpha.getCurrentValue();
            _program.setDiffuseColour(_fadingColour);
        }

        _program.activate(mvpMatrix);
        _polyhedra.draw();

        if (!_listening && SUPPORTED_KEYS.containsKey(_key) && _currentKeyTexture != null) {
            glBindTexture(GL_TEXTURE_2D, _currentKeyTexture.getId());
            _program.setDiffuseColour(_fadingColour);
            _program.activate(mvpMatrix);
            _polyhedra.draw();
        }
    }

    private void loadTextureForCurrentKey() throws IOException {
        if (_currentKeyTexture != null) {
            _currentKeyTexture.freeNativeResources();
            _currentKeyTexture = null;
        }
        if (SUPPORTED_KEYS.containsKey(_key)) {
            _currentKeyTexture = new GlTexture(BitmapImage.fromFile("images/" + SUPPORTED_KEYS.get(_key)));
        }
    }
}
