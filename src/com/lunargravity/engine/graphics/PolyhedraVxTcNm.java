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

import org.joml.Vector3f;

public class PolyhedraVxTcNm {
    private final int _numVertices;
    private final GlVertexArray _vertexArray;
    private final GlVertexBuffer _vertices;
    private final GlVertexBuffer _texCoordinates;
    private final GlVertexBuffer _normals;
    private final Vector3f _midPoint;

    public PolyhedraVxTcNm(float[] vertices, float[] texCoordinates, float[] normals) {
        _numVertices = vertices.length / 3;

        _vertexArray = new GlVertexArray();
        _vertexArray.activate();
        _vertexArray.setNumVertexAttributes(3);

        _vertices = new GlVertexBuffer();
        _vertices.setVertices(0, 3, vertices);

        _texCoordinates = new GlVertexBuffer();
        _texCoordinates.setVertices(1, 2, texCoordinates);

        _normals = new GlVertexBuffer();
        _normals.setVertices(2, 3, normals);

        _midPoint = new Vector3f();
        for (int i = 0; i < vertices.length; i += 3) {
            _midPoint.add(vertices[i], vertices[i + 1], vertices[i + 2]);
        }
        _midPoint.div(_numVertices);
    }

    public Vector3f getMidPoint() {
        return _midPoint;
    }

    public void freeNativeResources() {
        _vertices.freeNativeResources();
        _texCoordinates.freeNativeResources();
        _normals.freeNativeResources();
        _vertexArray.freeNativeResources();
    }

    public void draw() {
        _vertexArray.drawTriangles(0, _numVertices);
    }
}
