package com.lunargravity.engine.graphics;

import java.util.ArrayList;

public class GlMaterial {
    private final String _name;

    public GlMaterial(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void bindTextures(ArrayList<GlTexture> textures) {
        // TODO
    }
}
