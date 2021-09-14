package com.lunargravity.engine.graphics;

public class GlTexture {
    private final String _fileName;

    public GlTexture(String fileName) {
        _fileName = fileName;
    }

    public String getName() {
        return _fileName;
    }
}
