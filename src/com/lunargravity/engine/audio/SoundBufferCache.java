package com.lunargravity.engine.audio;

import java.util.ArrayList;

public class SoundBufferCache {
    private final ArrayList<SoundBuffer> _soundBuffers;

    public SoundBufferCache() {
        _soundBuffers = new ArrayList<>();
    }

    public ArrayList<SoundBuffer> getSoundBuffers() {
        return _soundBuffers;
    }

    public SoundBuffer getByExactName(String name) {
        for (var soundBuffer : _soundBuffers) {
            if (name.equals(soundBuffer.getFileName())) {
                return soundBuffer;
            }
        }
        return null;
    }

    public SoundBuffer getByPartialName(String subString) {
        for (var soundBuffer : _soundBuffers) {
            if (soundBuffer.getFileName().contains(subString)) {
                return soundBuffer;
            }
        }
        return null;
    }

    public SoundBuffer add(SoundBuffer material) {
        SoundBuffer cachedSoundBuffer = getByExactName(material.getFileName());
        if (cachedSoundBuffer != null) {
            return cachedSoundBuffer;
        }
        _soundBuffers.add(material);
        return material;
    }

    public void freeResources() {
        for (var s : _soundBuffers) {
            s.freeResources();
        }
        _soundBuffers.clear();
    }
}
