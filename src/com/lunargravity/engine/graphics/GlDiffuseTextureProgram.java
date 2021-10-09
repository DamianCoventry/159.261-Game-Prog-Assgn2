//
// Lunar Gravity
//
// This game is based upon the Amiga video game Gravity Force that was
// released in 1989 by Stephan Wenzler
//
// https://www.mobygames.com/game/gravity-force
// https://www.youtube.com/watch?v=m9mFtCvnko8
//
// This implementation is Copyright (c) 2021, Damian Coventry
// All rights reserved
// Written for Massey University course 159.261 Game Programming (Assignment 2)
//

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
        super(Files.readString(Paths.get("shaders/DiffuseTexture.vert"), StandardCharsets.UTF_8),
              Files.readString(Paths.get("shaders/DiffuseTexture.frag"), StandardCharsets.UTF_8));

        _mvpMatrixLocation = getUniformLocation("mvpMatrix");
        _diffuseTextureLocation = getUniformLocation("diffuseTexture");
        _diffuseColourLocation = getUniformLocation("diffuseColour");

        setDefaultDiffuseColour();
    }

    public void setDefaultDiffuseColour() {
        _diffuseColour = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void setDiffuseColour(Vector4f diffuseColour) {
        _diffuseColour.set(diffuseColour);
    }

    public void activate(Matrix4f mvpMatrix) {
        super.bind();
        setUniform(_mvpMatrixLocation, mvpMatrix);
        setUniform(_diffuseTextureLocation, 0);
        setUniform(_diffuseColourLocation, _diffuseColour);
    }
}
