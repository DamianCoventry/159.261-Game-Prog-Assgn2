package com.lunargravity.engine.graphics;

import org.joml.Matrix4f;

import java.util.ArrayList;

public class GlStaticMesh {
    private final String _name;

    public GlStaticMesh(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void draw(Matrix4f mvp) {
        // TODO
    }

    public void bindMaterials(ArrayList<GlMaterial> materials) {
        // TODO
    }
}
