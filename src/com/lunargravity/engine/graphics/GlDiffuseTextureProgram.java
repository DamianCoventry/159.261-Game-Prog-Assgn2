package com.lunargravity.engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GlDiffuseTextureProgram extends GlProgram {
    private final int _mvpMatrixLocation;
    private final int _diffuseTextureLocation;
    private final int _diffuseColourLocation;
    private Vector4f _diffuseColour;

    protected GlDiffuseTextureProgram() throws IOException {
        super(Files.readString(Paths.get("shaders/DiffuseTexture.vert"), StandardCharsets.US_ASCII),
              Files.readString(Paths.get("shaders/DiffuseTexture.frag"), StandardCharsets.US_ASCII));

        _mvpMatrixLocation = getUniformLocation("mvpMatrix");
        _diffuseTextureLocation = getUniformLocation("diffuseTexture");
        _diffuseColourLocation = getUniformLocation("diffuseColour");

        setDefaultDiffuseColour();
    }

    public void setDefaultDiffuseColour() {
        _diffuseColour = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void setDiffuseColour(Vector4f diffuseColour) {
        _diffuseColour = diffuseColour;
    }

    public void activate(Matrix4f mvpMatrix) {
        super.bind();
        setUniform(_mvpMatrixLocation, mvpMatrix);
        setUniform(_diffuseTextureLocation, 0);
        setUniform(_diffuseColourLocation, _diffuseColour);
    }
}
