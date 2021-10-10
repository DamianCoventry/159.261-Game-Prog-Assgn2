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

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class GlStaticMeshPiece {
    private final String _materialName;
    private final PolyhedraVxTcNm _polyhedra;
    private Material _material;

    public GlStaticMeshPiece(String materialName, PolyhedraVxTcNm polyhedra) {
        _materialName = materialName;
        _material = null;
        _polyhedra = polyhedra;
    }

    public Vector3f getMidPoint() {
        return _polyhedra.getMidPoint();
    }

    public void freeNativeResources() {
        _polyhedra.freeNativeResources();
    }

    public void bindMaterial(MaterialCache materialCache, TextureCache textureCache) {
        _material = materialCache.get(_materialName);
        if (_material == null) {
            throw new RuntimeException("No material has been loaded with the name [" + _materialName + "]");
        }
        if (!_material.isBound()) {
            _material.bindTextures(textureCache);
        }
    }

    public Material getMaterial() {
        return _material;
    }

    public void draw(Renderer renderer, GlDiffuseTextureProgram program, Matrix4f mvpMatrix) {
        renderer.activateTextureImageUnit(0);
        glBindTexture(GL_TEXTURE_2D, _material.getDiffuseTexture().getId());
        program.setDiffuseColour(_material.getDiffuseColour());
        program.activate(mvpMatrix);
        _polyhedra.draw();
    }

    public void draw(Renderer renderer, GlDiffuseTextureProgram program, Matrix4f mvpMatrix, Vector4f diffuseColour) {
        renderer.activateTextureImageUnit(0);
        glBindTexture(GL_TEXTURE_2D, _material.getDiffuseTexture().getId());
        program.setDiffuseColour(diffuseColour);
        program.activate(mvpMatrix);
        _polyhedra.draw();
    }

    public void draw(Renderer renderer, GlDiffuseTextureProgram program, Matrix4f mvpMatrix, GlTexture diffuseTexture, Vector4f diffuseColour) {
        renderer.activateTextureImageUnit(0);
        glBindTexture(GL_TEXTURE_2D, diffuseTexture.getId());
        program.setDiffuseColour(diffuseColour);
        program.activate(mvpMatrix);
        _polyhedra.draw();
    }

    public void draw(Renderer renderer, GLDirectionalLightProgram program, Matrix4f mvMatrix, Matrix4f projectionMatrix) {
        renderer.activateTextureImageUnit(0);
        glBindTexture(GL_TEXTURE_2D, _material.getDiffuseTexture().getId());
        program.setDiffuseColour(_material.getDiffuseColour());
        program.activate(mvMatrix, projectionMatrix);
        _polyhedra.draw();
    }

    public void draw(Renderer renderer, GLSpecularDirectionalLightProgram program, Matrix4f mvMatrix, Matrix4f projectionMatrix) {
        renderer.activateTextureImageUnit(0);
        glBindTexture(GL_TEXTURE_2D, _material.getDiffuseTexture().getId());
        program.setDiffuseColour(_material.getDiffuseColour());
        program.activate(mvMatrix, projectionMatrix);
        _polyhedra.draw();
    }

    public void draw(Renderer renderer, GLSpecularDirectionalLightProgram program, GlTexture diffuseTexture, Matrix4f mvMatrix, Matrix4f projectionMatrix) {
        renderer.activateTextureImageUnit(0);
        glBindTexture(GL_TEXTURE_2D, diffuseTexture.getId());
        program.setDiffuseColour(_material.getDiffuseColour());
        program.activate(mvMatrix, projectionMatrix);
        _polyhedra.draw();
    }
}
