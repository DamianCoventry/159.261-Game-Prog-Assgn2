package com.lunargravity.engine.graphics;

public class PolyhedraVxTcNm {
    private final int _numVertices;
    private final GlVertexArray _vertexArray;
    private final GlVertexBuffer _vertices;
    private final GlVertexBuffer _texCoordinates;
    private final GlVertexBuffer _normals;

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
    }

    public void freeResources() {
        _vertices.freeResources();
        _texCoordinates.freeResources();
        _normals.freeResources();
        _vertexArray.freeResources();
    }

    public void draw() {
        _vertexArray.drawTriangles(0, _numVertices);
    }
}
