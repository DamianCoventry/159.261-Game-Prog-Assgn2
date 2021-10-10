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

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.glBindBufferBase;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.system.MemoryUtil.memFree;

public class GlVertexBuffer {
    private int _id;

    public GlVertexBuffer() {
        _id = glGenBuffers();
        if (_id == 0) {
            throw new RuntimeException("Unable to create an OpenGL vertex buffer object");
        }
    }

    public void freeNativeResources() {
        if (_id != 0) {
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glDeleteBuffers(_id);
            _id = 0;
        }
    }

    public void setVertices(int attribute, int numFloatsPerVertex, float[] vertices) {
        FloatBuffer floatBuffer = null;
        try {
            floatBuffer = MemoryUtil.memAllocFloat(vertices.length);
            floatBuffer.put(vertices).flip();

            glBindBuffer(GL_ARRAY_BUFFER, _id);
            glBufferData(GL_ARRAY_BUFFER, floatBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(attribute);
            glVertexAttribPointer(attribute, numFloatsPerVertex, GL_FLOAT, false, 0, 0);
        }
        finally {
            if (floatBuffer != null) {
                memFree(floatBuffer);
            }
        }
    }
}
