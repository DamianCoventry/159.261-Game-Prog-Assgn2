package com.lunargravity.engine.graphics;

import org.joml.*;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32C.GL_GEOMETRY_SHADER;

public class GlProgram {
    protected int _id;

    protected GlProgram(String vertexShaderSourceCode, String fragmentShaderSourceCode) {
        _id = glCreateProgram();
        if (_id < 1) {
            throw new RuntimeException("Unable to create an OpenGL program object");
        }

        GlShader vertexShader = new GlShader(GL_VERTEX_SHADER, vertexShaderSourceCode);
        GlShader fragmentShader = new GlShader(GL_FRAGMENT_SHADER, fragmentShaderSourceCode);

        link(vertexShader, fragmentShader);
    }

    protected GlProgram(String vertexShaderSourceCode, String geometryShaderSourceCode, String fragmentShaderSourceCode) {
        _id = glCreateProgram();
        if (_id < 1) {
            throw new RuntimeException("Unable to create an OpenGL program object");
        }

        GlShader vertexShader = new GlShader(GL_VERTEX_SHADER, vertexShaderSourceCode);
        GlShader fragmentShader = new GlShader(GL_FRAGMENT_SHADER, fragmentShaderSourceCode);
        GlShader geometryShader = new GlShader(GL_GEOMETRY_SHADER, geometryShaderSourceCode);

        link(vertexShader, fragmentShader, geometryShader);
    }

    public void freeResources() {
        if (_id != 0) {
            glUseProgram(0);
            glDeleteProgram(_id);
            _id = 0;
        }
    }

    public void bind() {
        glUseProgram(_id);
    }

    protected int getId() {
        return _id;
    }

    protected int getUniformLocation(String uniformName) {
        int location = glGetUniformLocation(_id, uniformName);
        if (location < 0) {
            throw new RuntimeException("Uniform name does not exist");
        }
        return location;
    }

    protected void setUniform(int location, int value) {
        glUniform1i(location, value);
    }

    protected void setUniform(int location, float value) {
        glUniform1f(location, value);
    }

    protected void setUniform(int location, Vector3f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(3);
            value.get(fb);
            glUniform3fv(location, fb);
        }
    }

    protected void setUniform(int location, Vector4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(4);
            value.get(fb);
            glUniform4fv(location, fb);
        }
    }

    protected void setUniform(int location, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            glUniformMatrix4fv(location, false, fb);
        }
    }

    private void link(GlShader vertexShader, GlShader fragmentShader) {
        glAttachShader(_id, vertexShader.getId());
        glAttachShader(_id, fragmentShader.getId());

        glLinkProgram(_id);
        int linkStatus = glGetProgrami(_id, GL_LINK_STATUS);
        String log = glGetProgramInfoLog(_id, 1024);

        glDetachShader(_id, vertexShader.getId());
        vertexShader.freeResources();

        glDetachShader(_id, fragmentShader.getId());
        fragmentShader.freeResources();

        if (linkStatus == 0) {
            throw new RuntimeException("The program doesn't link.\n" + log);
        }

        glValidateProgram(_id);
        if (glGetProgrami(_id, GL_VALIDATE_STATUS) == 0) {
            throw new RuntimeException("The program doesn't validate.\n" + glGetProgramInfoLog(_id, 1024));
        }
    }

    private void link(GlShader vertexShader, GlShader fragmentShader, GlShader geometryShader) {
        glAttachShader(_id, vertexShader.getId());
        glAttachShader(_id, fragmentShader.getId());
        glAttachShader(_id, geometryShader.getId());

        glLinkProgram(_id);
        int linkStatus = glGetProgrami(_id, GL_LINK_STATUS);
        String log = glGetProgramInfoLog(_id, 1024);

        glDetachShader(_id, vertexShader.getId());
        vertexShader.freeResources();

        glDetachShader(_id, fragmentShader.getId());
        fragmentShader.freeResources();

        glDetachShader(_id, geometryShader.getId());
        geometryShader.freeResources();

        if (linkStatus == 0) {
            glDeleteProgram(_id);
            throw new RuntimeException("The program doesn't link.\n" + log);
        }

        glValidateProgram(_id);
        if (glGetProgrami(_id, GL_VALIDATE_STATUS) == 0) {
            glDeleteProgram(_id);
            throw new RuntimeException("The program doesn't validate.\n" + glGetProgramInfoLog(_id, 1024));
        }
    }
}
