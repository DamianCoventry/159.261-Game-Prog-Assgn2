package com.lunargravity.engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;

public abstract class ParticleSystem {
    protected static final float VELOCITY_SCALE = 0.016f;
    protected final Renderer _renderer;
    protected final DisplayMesh _displayMesh;
    protected final Matrix4f _mvpMatrix;
    protected final GlTexture _originalDiffuseTexture;

    protected static class Particle {
        public Vector3f _position;
        public Vector3f _velocity;
        public Vector4f _diffuseColour;
        public Matrix4f _modelMatrix;
        public float _rotation;
        public float _rotationDelta;
        public float _alphaDelta;
        public long _lifeTimeMs;
        public boolean _dead;
        public GlTexture _diffuseTexture;
    }
    protected final Particle[] _particles;
    protected boolean _spawned;
    protected boolean _dead;

    public ParticleSystem(Renderer renderer, int numParticles, String spriteName,
                          float spriteWidth, float spriteHeight,
                          Vector4f diffuseColour, String diffuseTextureFileName,
                          MaterialCache materialCache, TextureCache textureCache) throws IOException {
        _renderer = renderer;
        _mvpMatrix = new Matrix4f();
        _spawned = false;
        _dead = true;

        _particles = new Particle[numParticles];
        for (int i = 0; i < numParticles; ++i) {
            _particles[i] = new Particle();
            _particles[i]._position = new Vector3f();
            _particles[i]._velocity = new Vector3f();
            _particles[i]._diffuseColour = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
            _particles[i]._modelMatrix = new Matrix4f();
            _particles[i]._rotation = 0.0f;
            _particles[i]._rotationDelta = 0.0f;
            _particles[i]._alphaDelta = 0.0f;
            _particles[i]._dead = true;
        }

        _displayMesh = renderer.createSpriteWithOriginAtCenter(spriteName, spriteWidth, spriteHeight, diffuseColour,
                diffuseTextureFileName, materialCache, textureCache);
        _originalDiffuseTexture = _displayMesh.getFirstDiffuseTexture();
    }

    public boolean isAlive() {
        return !_dead;
    }

    public void freeNativeResources() {
        _displayMesh.freeNativeResources();
        _originalDiffuseTexture.freeNativeResources();
        for (var a : _particles) {
            if (a._diffuseTexture != null) {
                a._diffuseTexture.freeNativeResources();
            }
        }
    }

    public void emitAll(long nowMs, Vector3f startPosition, int minLifeTimeMs, int maxLifeTimeMs) {
        // No default implementation. This method is optional
    }

    public void emitSome(long nowMs, Vector3f startPosition, int minLifeTimeMs, int maxLifeTimeMs) {
        // No default implementation. This method is optional
    }

    public void emitOne(long nowMs, Vector3f startPosition, int minLifeTimeMs, int maxLifeTimeMs) {
        // No default implementation. This method is optional
    }

    public abstract void think(long nowMs);

    public void draw(Matrix4f vpMatrix) {
        if (!_spawned || _dead) {
            return;
        }
        for (var particle : _particles) {
            if (!particle._dead) {
                _mvpMatrix.set(vpMatrix).mul(particle._modelMatrix);
                _displayMesh.draw(_renderer, _renderer.getDiffuseTextureProgram(), _mvpMatrix, particle._diffuseColour);
            }
        }
    }

    protected Vector3f generateRandomVelocityVector(int minVelocity, int maxVelocity) {
        int angle0 = randomInteger(0, 359);
        int angle1 = randomInteger(0, 359);
        float x = (float)Math.sin(angle0);
        float y = (float)Math.cos(angle0);
        float z = (float)Math.sin(angle1);
        Vector3f direction = new Vector3f(x, y, z).normalize();
        return direction.mul(randomInteger(minVelocity, maxVelocity) * VELOCITY_SCALE);
    }

    protected int randomInteger(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}
