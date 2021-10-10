package com.lunargravity.engine.audio;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.openal.ALC10.*;

public class AudioDevice {
    private long _deviceId;
    private long _context;

    public AudioDevice() {
        _deviceId = alcOpenDevice((ByteBuffer)null);
        if (_deviceId == 0) {
            throw new RuntimeException("Unable to open the default Audio Device");
        }

        ALCCapabilities deviceCaps = ALC.createCapabilities(_deviceId);
        _context = alcCreateContext(_deviceId, (IntBuffer)null);
        if (_context == 0) {
            alcCloseDevice(_deviceId);
            throw new IllegalStateException("Unable to crate an Audio context");
        }

        alcMakeContextCurrent(_context);
        AL.createCapabilities(deviceCaps);
    }

    public void freeNativeResources() {
        if (_context != 0) {
            alcDestroyContext(_context);
            _context = 0;
        }
        if (_deviceId != 0) {
            alcCloseDevice(_deviceId);
            _deviceId = 0;
        }
    }
}
