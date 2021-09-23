package com.lunargravity.engine.graphics;

import static org.lwjgl.opengl.GL20.*;

public class GlShader {
    private int _id;
    public GlShader(int type, String sourceCode) {
        _id = glCreateShader(type);
        if (_id < 1) {
            throw new RuntimeException("Unable to create an OpenGL shader object");
        }

        glShaderSource(_id, sourceCode);

        glCompileShader(_id);
        if (glGetShaderi(_id, GL_COMPILE_STATUS) == 0) {
            String log = glGetShaderInfoLog(_id, 1024);
            glDeleteShader(_id);
            throw new RuntimeException("The shader source code doesn't compile.\n" + log);
        }
    }

    public void freeResources() {
        if (_id != 0) {
            glDeleteShader(_id);
            _id = 0;
        }
    }

    public int getId() {
        return _id;
    }
}
