package com.lunargravity.engine.graphics;

import java.util.ArrayList;

public class TextureCache {
    private final ArrayList<GlTexture> _textures;

    public TextureCache() {
        _textures = new ArrayList<>();
    }

    public GlTexture get(String fileName) {
        for (var texture : _textures) {
            if (fileName.equals(texture.getFileName())) {
                return texture;
            }
        }
        return null;
    }

    public GlTexture add(GlTexture texture) {
        GlTexture cachedTexture = get(texture.getFileName());
        if (cachedTexture != null) {
            return cachedTexture;
        }
        _textures.add(texture);
        return texture;
    }

    public void freeResources() {
        for (var t : _textures) {
            t.freeResources();
        }
        _textures.clear();
    }
}
