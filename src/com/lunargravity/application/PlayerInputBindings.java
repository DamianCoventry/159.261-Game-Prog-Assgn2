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

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerInputBindings {
    private static final String INPUT_BINDINGS_FILE_NAME = "inputBindings.json";

    private static class InputBinding {
        public int _keyThrust;
        public int _keyRotateCcw;
        public int _keyRotateCw;
        public int _keyShoot;
        public int _keyKick;
    }
    private InputBinding[] _inputBindings;

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

    public boolean load() {
        try {
            File file = new File(INPUT_BINDINGS_FILE_NAME);
            if (!file.exists() || !file.isFile()) {
                return false;
            }

            String text = Files.readString(Paths.get(INPUT_BINDINGS_FILE_NAME), StandardCharsets.UTF_8);
            if (text.isEmpty()) {
                return false;
            }
            
            Gson gson = new Gson();
            _inputBindings = gson.fromJson(text, InputBinding[].class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void save() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(_inputBindings);
            Files.writeString(Paths.get(INPUT_BINDINGS_FILE_NAME), json, StandardCharsets.UTF_8);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
