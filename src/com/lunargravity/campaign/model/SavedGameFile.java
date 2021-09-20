package com.lunargravity.campaign.model;

import java.io.IOException;

public class SavedGameFile {
    private int _episode;
    private int _mission;
    private int _numPlayers;
    private String _startDateTime;
    private String _lastSaveDateTime;
    private int _elapsedHours;
    private int _elapsedMinutes;
    private int _elapsedSeconds;
    private int _thumbnailWidth;
    private int _thumbnailHeight;
    private byte[] _thumbnailBytes;

    static public void create(String fileName, int episode, int mission, int numPlayers) throws IOException {
        // TODO
    }

    public SavedGameFile(String fileName) throws IOException {
        // TODO
    }

    public int getEpisode() {
        return _episode;
    }

    public int getMission() {
        return _mission;
    }

    public int getNumPlayers() {
        return _numPlayers;
    }

    public String getStartDateTime() {
        return _startDateTime;
    }

    public String getLastSaveDateTime() {
        return _lastSaveDateTime;
    }

    public int getElapsedHours() {
        return _elapsedHours;
    }

    public int getElapsedMinutes() {
        return _elapsedMinutes;
    }

    public int getElapsedSeconds() {
        return _elapsedSeconds;
    }

    public int getThumbnailWidth() {
        return _thumbnailWidth;
    }

    public int getThumbnailHeight() {
        return _thumbnailHeight;
    }

    public byte[] getThumbnailBytes() {
        return _thumbnailBytes;
    }
}
