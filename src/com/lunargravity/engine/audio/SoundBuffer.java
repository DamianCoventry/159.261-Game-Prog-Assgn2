package com.lunargravity.engine.audio;

import org.lwjgl.system.MemoryUtil;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;

public class SoundBuffer {
    public static SoundBuffer fromFile(String fileName) throws UnsupportedAudioFileException, IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new RuntimeException("File system item [" + fileName + "] does not exist");
        }
        if (!file.isFile()) {
            throw new RuntimeException("The file system item [" + fileName + "] exists but is not a file");
        }
        return new SoundBuffer(fileName, AudioSystem.getAudioInputStream(file));
    }

    private final String _fileName;
    private int _bufferId;
    private ByteBuffer _audioData;

    private SoundBuffer(String fileName, AudioInputStream audioInputStream) throws IOException {
        AudioFormat audioFormat = audioInputStream.getFormat();
        if(audioFormat.getChannels() != 1) {
            throw new RuntimeException("Only single channel audio files supported");
        }
        if(audioFormat.getSampleRate() != 22050) {
            throw new RuntimeException("Only 22kHZ audio files supported");
        }
        if(audioFormat.getSampleSizeInBits() != 16) {
            throw new RuntimeException("Only 16 bit audio files supported");
        }

        _bufferId = alGenBuffers();
        if (_bufferId == 0) {
            throw new RuntimeException("Unable to create a sound buffer");
        }

        long numBytes = audioInputStream.getFrameLength() * audioFormat.getFrameSize();
        byte[] bytes = new byte[(int)numBytes];

        long numRead = audioInputStream.read(bytes);
        if (numRead != numBytes){
            alDeleteBuffers(_bufferId);
            throw new RuntimeException("Unable to read all of the audio data from the file ["+fileName+"]");
        }

        _audioData = MemoryUtil.memAlloc((int)numBytes);
        _audioData.put(bytes).flip();

        alBufferData(_bufferId, AL_FORMAT_MONO16, _audioData, (int)audioFormat.getSampleRate());
        _fileName = fileName;
    }

    public void freeNativeResources() {
        if (_bufferId != 0) {
            alDeleteBuffers(_bufferId);
            _bufferId = 0;
        }
        if (_audioData != null) {
            MemoryUtil.memFree(_audioData);;
            _audioData = null;
        }
    }

    public int getBufferId() {
        return _bufferId;
    }

    public String getFileName() {
        return _fileName;
    }
}
