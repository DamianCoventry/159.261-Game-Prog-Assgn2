package com.lunargravity.engine.graphics;

import static org.lwjgl.opengl.GL30C.*;

public class GlVertexArray {
    private int _id;
    private int _numVertexAttributes;

    public GlVertexArray() {
        _id = glGenVertexArrays();
        if (_id == 0) {
            throw new RuntimeException("Unable to create an OpenGL vertex array object");
        }
        _numVertexAttributes = 0;
    }

    public void freeResources() {
        if (_id != 0) {
            glBindVertexArray(0);
            glDeleteVertexArrays(_id);
            _id = 0;
        }
    }

    public void activate() {
        glBindVertexArray(_id);
    }

    public void setNumVertexAttributes(int numVertexAttributes) {
        _numVertexAttributes = numVertexAttributes;
    }
    public int getNumVertexAttributes() {
        return _numVertexAttributes;
    }

    public void drawTriangles(int firstVertex, int numVertices) {
        activate();
        for (int i = 0; i < _numVertexAttributes; ++ i) {
            glEnableVertexAttribArray(i);
        }
        glDrawArrays(GL_TRIANGLES, firstVertex, numVertices);
    }
}
