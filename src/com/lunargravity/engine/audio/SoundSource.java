package com.lunargravity.engine.audio;

import org.joml.Vector3f;

import static org.lwjgl.openal.AL10.*;

public class SoundSource {
    private int _sourceId;
    public SoundSource(boolean loop, boolean relative) {
        _sourceId = alGenSources();
        if (loop) {
            alSourcei(_sourceId, AL_LOOPING, AL_TRUE);
        }
        if (relative) {
            alSourcei(_sourceId, AL_SOURCE_RELATIVE, AL_TRUE);
        }
    }

    public void freeResources() {
        if (_sourceId != 0) {
            stop();
            alDeleteSources(_sourceId);
            _sourceId = 0;
        }
    }

    public void setBuffer(SoundBuffer soundBuffer) {
        stop();
        alSourcei(_sourceId, AL_BUFFER, soundBuffer.getBufferId());
    }

    public void setPosition(Vector3f position) {
        alSource3f(_sourceId, AL_POSITION, position.x, position.y, position.z);
    }

    public void setSpeed(Vector3f speed) {
        alSource3f(_sourceId, AL_VELOCITY, speed.x, speed.y, speed.z);
    }

    public void setGain(float gain) {
        alSourcef(_sourceId, AL_GAIN, gain);
    }

    public void setProperty(int param, float value) {
        alSourcef(_sourceId, param, value);
    }

    public void play() {
        alSourcePlay(_sourceId);
    }

    public boolean isPlaying() {
        return alGetSourcei(_sourceId, AL_SOURCE_STATE) == AL_PLAYING;
    }

    public void pause() {
        alSourcePause(_sourceId);
    }

    public void stop() {
        alSourceStop(_sourceId);
    }
}
