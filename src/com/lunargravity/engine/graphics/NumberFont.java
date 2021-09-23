package com.lunargravity.engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11C.glDepthMask;

public class NumberFont {
    private static final int NUM_CHARACTERS = 11;

    private final GlRenderer _renderer;
    private final GlTexture _texture;
    private final GlDiffuseTextureProgram _program;
    private final float _frameWidth;
    private final float _halfFrameWidth;
    private final float _halfFrameHeight;
    private final Matrix4f _projectionMatrix;
    private final Matrix4f _modelMatrix;
    private PolyhedraVxTc[] _characterPolyhedra;

    public NumberFont(GlRenderer renderer, float frameWidth, float frameHeight, String imageFileName) throws IOException {
        _frameWidth = frameWidth;
        _halfFrameWidth = frameWidth / 2.0f;
        _halfFrameHeight = frameHeight / 2.0f;
        _renderer = renderer;
        _program = _renderer.getDiffuseTextureProgram();
        _projectionMatrix = new Matrix4f();
        _modelMatrix = new Matrix4f();
        _texture = new GlTexture(BitmapImage.fromFile(imageFileName));
        buildPolyhedra(frameWidth, frameHeight);
    }

    public void freeResources() {
        _texture.freeResources();
        for (var c : _characterPolyhedra) {
            c.freeResources();
        }
    }

    public void drawNumber(Matrix4f projectionMatrix, long number, float x, float y, float scale, Vector4f colour) {
        _renderer.activateTextureImageUnit(0);
        glBindTexture(GL_TEXTURE_2D, _texture.getId());

        _program.bind();
        _program.setDiffuseColour(colour);

        glDepthMask(false);

        String text = String.valueOf(number);
        for (int i = 0; i < text.length(); ++i) {
            int index = text.charAt(i) - '0'; // convert to an index into the _characterPolyhedra array
            drawSingleCharacter(projectionMatrix, index, x, y, scale);
            x += _frameWidth;
        }

        glDepthMask(true);
    }

    public void drawPercentage(Matrix4f projectionMatrix, long number, float x, float y, float scale, Vector4f colour) {
        drawNumber(projectionMatrix, number, x, y, scale, colour);
        glDepthMask(false);
        drawSingleCharacter(projectionMatrix, NUM_CHARACTERS - 1, x + String.valueOf(number).length() * _frameWidth, y, scale);
        glDepthMask(true);
    }

    private void buildPolyhedra(float frameWidth, float frameHeight) {
        final float deltaU = frameWidth / _texture.getWidth();
        final float deltaV = frameHeight / _texture.getHeight();

        _characterPolyhedra = new PolyhedraVxTc[NUM_CHARACTERS];
        for (int i = 0; i < NUM_CHARACTERS; ++i) {
            final float[] vertices = new float[] {
                    // Triangle 0
                    -_halfFrameWidth, _halfFrameHeight, 0.0f,
                    -_halfFrameWidth, -_halfFrameHeight, 0.0f,
                    _halfFrameWidth, -_halfFrameHeight, 0.0f,
                    // Triangle 1
                    -_halfFrameWidth, _halfFrameHeight, 0.0f,
                    _halfFrameWidth, -_halfFrameHeight, 0.0f,
                    _halfFrameWidth, _halfFrameHeight, 0.0f,
            };

            final float u0 = i * deltaU;
            final float u1 = u0 + deltaU;

            final float[] texCoordinates = new float[] {
                    // Triangle 0
                    u0, 0.0f,
                    u0, deltaV,
                    u1, deltaV,
                    // Triangle 1
                    u0, 0.0f,
                    u1, deltaV,
                    u1, 0.0f,
            };
            
            _characterPolyhedra[i] = new PolyhedraVxTc(vertices, texCoordinates);
        }
    }

    private void drawSingleCharacter(Matrix4f projectionMatrix, int i, float x, float y, float scale) {
        _modelMatrix
                .identity()
                .translate(x + _halfFrameWidth, y + _halfFrameHeight, 0.0f)
                .scale(scale);

        // there is no view matrix when using an orthographic projection matrix
        Matrix4f mvpMatrix = _projectionMatrix.set(projectionMatrix).mul(_modelMatrix);

        _program.activate(mvpMatrix);
        _characterPolyhedra[i].draw();
    }
}
