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
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GLDirectionalLightProgram extends GlProgram {
    private final int _mvMatrixLocation;
    private final int _projectionMatrixLocation;
    private final int _diffuseTextureLocation;
    private final int _diffuseColourLocation;
    private final int _ambientLightLocation;
    private final int _lightDirectionLocation;
    private final int _lightColourLocation;
    private final int _lightIntensityLocation;

    private Vector4f _diffuseColour;
    private Vector3f _ambientLight;
    private Vector3f _lightDirection;
    private Vector3f _lightColour;
    private float _lightIntensity;

    public GLDirectionalLightProgram() throws IOException {
        super(Files.readString(Paths.get("shaders/DirectionalLight.vert"), StandardCharsets.UTF_8),
              Files.readString(Paths.get("shaders/DirectionalLight.frag"), StandardCharsets.UTF_8));

        _mvMatrixLocation = getUniformLocation("mvMatrix");
        _projectionMatrixLocation = getUniformLocation("projectionMatrix");
        _diffuseTextureLocation = getUniformLocation("diffuseTexture");
        _diffuseColourLocation = getUniformLocation("diffuseColour");
        _ambientLightLocation = getUniformLocation("ambientLight");
        _lightDirectionLocation = getUniformLocation("lightDirection");
        _lightColourLocation = getUniformLocation("lightColour");
        _lightIntensityLocation = getUniformLocation("lightIntensity");

        _diffuseColour = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        _ambientLight = new Vector3f(0.15f, 0.15f, 0.15f);
        _lightDirection = new Vector3f(0.0f, 0.0f, 1.0f);
        _lightColour = new Vector3f(1.0f, 1.0f, 1.0f);
        _lightIntensity = 1.0f;
    }

    public void setDiffuseColour(Vector4f diffuseColour) {
        _diffuseColour.set(diffuseColour);
    }

    public void setAmbientLight(Vector3f ambientLight) {
        _ambientLight = ambientLight;
    }

    public void setLightDirection(Vector3f lightDirection) {
        _lightDirection = lightDirection;
    }

    public void setLightColour(Vector3f lightColour) {
        _lightColour = lightColour;
    }

    public void setLightIntensity(float lightIntensity) {
        _lightIntensity = lightIntensity;
    }

    public void activate(Matrix4f mvMatrix, Matrix4f projectionMatrix) {
        super.bind();
        setUniform(_mvMatrixLocation, mvMatrix);
        setUniform(_projectionMatrixLocation, projectionMatrix);
        setUniform(_diffuseTextureLocation, 0);
        setUniform(_diffuseColourLocation, _diffuseColour);
        setUniform(_ambientLightLocation, _ambientLight);
        setUniform(_lightDirectionLocation, _lightDirection);
        setUniform(_lightColourLocation, _lightColour);
        setUniform(_lightIntensityLocation, _lightIntensity);
    }
}
