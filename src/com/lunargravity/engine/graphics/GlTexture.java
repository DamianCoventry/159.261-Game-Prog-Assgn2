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

import static org.lwjgl.opengl.GL11C.*;

public class GlTexture {
    private final String _fileName;
    private int _id;
    private final float _width;
    private final float _height;

    public GlTexture(BitmapImage bitmapImage) {
        _id = glGenTextures();
        if (_id < 1) {
            throw new RuntimeException("Unable to create an OpenGL texture object");
        }

        glBindTexture(GL_TEXTURE_2D, _id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, bitmapImage.getWidth(), bitmapImage.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, bitmapImage.getByteBuffer());
        if (glGetError() != GL_NO_ERROR) {
            glDeleteTextures(_id);
            throw new RuntimeException("Unable to copy the bitmapImage pixel data into an OpenGL texture object [" + bitmapImage.getFileName() + "]");
        }

        _fileName = bitmapImage.getFileName();
        _width = bitmapImage.getWidth();
        _height = bitmapImage.getHeight();
    }

    public void freeNativeResources() {
        if (_id != 0) {
            glDeleteTextures(_id);
            _id = 0;
        }
    }

    public int getId() {
        return _id;
    }
    public String getFileName() {
        return _fileName;
    }
    public float getWidth() {
        return _width;
    }
    public float getHeight() {
        return _height;
    }
}
