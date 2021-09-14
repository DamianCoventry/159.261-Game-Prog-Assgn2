package com.lunargravity.engine.graphics;

import org.joml.Matrix4f;

public class GlObject {
    public String _name;
    public GlTransform _transform;
    public GlStaticMesh _staticMesh;

    public GlObject(String name, GlTransform transform) {
        _name = name;
        _transform = transform;
        _staticMesh = null;
    }

    public GlObject(String name, GlTransform transform, GlStaticMesh staticMesh) {
        _name = name;
        _transform = transform;
        _staticMesh = staticMesh;
    }

    public void draw(Matrix4f viewProjectionMatrix) {
        // TODO
    }
    public void draw(Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        // TODO
    }
}
