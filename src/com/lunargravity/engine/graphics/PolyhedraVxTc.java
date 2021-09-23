package com.lunargravity.engine.graphics;

public class PolyhedraVxTc {
    private final int _numVertices;
    private final GlVertexArray _vertexArray;
    private final GlVertexBuffer _vertices;
    private final GlVertexBuffer _texCoordinates;

    public PolyhedraVxTc(float[] vertices, float[] texCoordinates) {
        _numVertices = vertices.length / 3;

        _vertexArray = new GlVertexArray();
        _vertexArray.activate();
        _vertexArray.setNumVertexAttributes(2);

        _vertices = new GlVertexBuffer();
        _vertices.setVertices(0, 3, vertices);

        _texCoordinates = new GlVertexBuffer();
        _texCoordinates.setVertices(1, 2, texCoordinates);
    }

    public void freeResources() {
        _vertices.freeResources();
        _texCoordinates.freeResources();
        _vertexArray.freeResources();
    }

    public void draw() {
        _vertexArray.drawTriangles(0, _numVertices);
    }
}
