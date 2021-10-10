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

    public void freeNativeResources() {
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
