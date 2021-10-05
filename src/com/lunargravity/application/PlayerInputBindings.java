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

package com.lunargravity.application;

import java.io.File;
import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerInputBindings {
    private static class InputBinding {
        public int _keyThrust;
        public int _keyRotateCcw;
        public int _keyRotateCw;
        public int _keyShoot;
        public int _keyKick;
    }
    private final InputBinding[] _inputBindings;

    public PlayerInputBindings() {
        _inputBindings = new InputBinding[2];
        _inputBindings[0] = new InputBinding();
        _inputBindings[1] = new InputBinding();
        setDefaults();
    }

    public void setDefaults() {
        _inputBindings[0]._keyThrust = GLFW_KEY_UP;
        _inputBindings[0]._keyRotateCcw = GLFW_KEY_LEFT;
        _inputBindings[0]._keyRotateCw = GLFW_KEY_RIGHT;
        _inputBindings[0]._keyShoot = GLFW_KEY_RIGHT_CONTROL;
        _inputBindings[0]._keyKick = GLFW_KEY_RIGHT_SHIFT;
        _inputBindings[1]._keyThrust = GLFW_KEY_W;
        _inputBindings[1]._keyRotateCcw = GLFW_KEY_A;
        _inputBindings[1]._keyRotateCw = GLFW_KEY_D;
        _inputBindings[1]._keyShoot = GLFW_KEY_SPACE;
        _inputBindings[1]._keyKick = GLFW_KEY_K;
    }

    public void load(File file) throws IOException {
        // TODO
    }

    public void save(String fileName) throws IOException {
        // TODO
    }

    public void setPlayerRotateLeftKey(int i, int key) {
        if (i == 0 || i == 1) {
            _inputBindings[i]._keyRotateCcw = key;
        }
    }
    public int getPlayerRotateLeftKey(int i) {
        if (i == 0 || i == 1) {
            return _inputBindings[i]._keyRotateCcw;
        }
        return -1;
    }

    public void setPlayerRotateRightKey(int i, int key) {
        if (i == 0 || i == 1) {
            _inputBindings[i]._keyRotateCw = key;
        }
    }
    public int getPlayerRotateRightKey(int i) {
        if (i == 0 || i == 1) {
            return _inputBindings[i]._keyRotateCw;
        }
        return -1;
    }

    public void setPlayerThrustKey(int i, int key) {
        if (i == 0 || i == 1) {
            _inputBindings[i]._keyThrust = key;
        }
    }
    public int getPlayerThrustKey(int i) {
        if (i == 0 || i == 1) {
            return _inputBindings[i]._keyThrust;
        }
        return -1;
    }

    public void setPlayerShootKey(int i, int key) {
        if (i == 0 || i == 1) {
            _inputBindings[i]._keyShoot = key;
        }
    }
    public int getPlayerShootKey(int i) {
        if (i == 0 || i == 1) {
            return _inputBindings[i]._keyShoot;
        }
        return -1;
    }

    public void setPlayerKickKey(int i, int key) {
        if (i == 0 || i == 1) {
            _inputBindings[i]._keyKick = key;
        }
    }
    public int getPlayerKickKey(int i) {
        if (i == 0 || i == 1) {
            return _inputBindings[i]._keyKick;
        }
        return -1;
    }
}
