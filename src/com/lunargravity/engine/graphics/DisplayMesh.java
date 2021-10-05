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

import java.util.ArrayList;

public class DisplayMesh {
    private final String _name;
    private final ArrayList<GlStaticMeshPiece> _pieces;
    private final Vector3f _midPoint;

    public DisplayMesh(String name) {
        _name = name;
        _pieces = new ArrayList<>();
        _midPoint = new Vector3f();
    }

    public void freeResources() {
        for (var p : _pieces) {
            p.freeResources();
        }
    }

    public String getName() {
        return _name;
    }

    public Vector3f getMidPoint() {
        return _midPoint;
    }

    public void addPiece(GlStaticMeshPiece piece) {
        _pieces.add(piece);
        _midPoint.set(0);
        for (var p : _pieces) {
            _midPoint.add(p.getMidPoint());
        }
        _midPoint.div(_pieces.size());
    }

    public void bindMaterials(MaterialCache materialCache, TextureCache textureCache) {
        for (var piece : _pieces) {
            piece.bindMaterial(materialCache, textureCache);
        }
    }

    public void draw(Renderer renderer, GlDiffuseTextureProgram program, Matrix4f mvpMatrix) {
        for (var piece : _pieces) {
            piece.draw(renderer, program, mvpMatrix);
        }
    }

    public void draw(Renderer renderer, GlDiffuseTextureProgram program, Matrix4f mvpMatrix, Vector4f diffuseColour) {
        for (var piece : _pieces) {
            piece.draw(renderer, program, mvpMatrix, diffuseColour);
        }
    }

    public void draw(Renderer renderer, GLDirectionalLightProgram program, Matrix4f mvMatrix, Matrix4f projectionMatrix) {
        for (var piece : _pieces) {
            piece.draw(renderer, program, mvMatrix, projectionMatrix);
        }
    }

    public void draw(Renderer renderer, GLSpecularDirectionalLightProgram program, Matrix4f mvMatrix, Matrix4f projectionMatrix) {
        for (var piece : _pieces) {
            piece.draw(renderer, program, mvMatrix, projectionMatrix);
        }
    }
}
