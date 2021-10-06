package com.lunargravity.engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;

public class ParticleSystem {
    private static final float VELOCITY_SCALE = 0.016f;
    private final Renderer _renderer;
    private final DisplayMesh _displayMesh;
    private final Matrix4f _mvpMatrix;

    private static class Particle {
        public Vector3f _position;
        public Vector3f _velocity;
        public Matrix4f _modelMatrix;
        public long _lifeTimeMs;
        public boolean _dead;
    }
    private final Particle[] _particles;
    private boolean _spawned;
    private boolean _dead;

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
            _particles[i]._modelMatrix = new Matrix4f();
            _particles[i]._dead = true;
        }

        _displayMesh = renderer.createSprite(spriteName, spriteWidth, spriteHeight, diffuseColour,
                diffuseTextureFileName, materialCache, textureCache);
    }

    public void freeResources() {
        _displayMesh.freeResources();
    }

    public void spawn(long nowMs, Vector3f startPosition, int minVelocity, int maxVelocity, int minLifeTimeMs, int maxLifeTimeMs) {
        _spawned = true;
        _dead = false;

        for (var particle : _particles) {
            particle._position.set(startPosition);
            particle._velocity = generateVelocityVector(minVelocity, maxVelocity);
            particle._modelMatrix.identity();
            particle._lifeTimeMs = nowMs + randomInteger(minLifeTimeMs, maxLifeTimeMs);
            particle._dead = false;
        }
    }

    public void think(long nowMs) {
        if (!_spawned || _dead) {
            return;
        }
        int count = 0;
        for (var particle : _particles) {
            if (particle._dead) {
                ++count;
            }
            else {
                particle._position.add(particle._velocity);
                particle._modelMatrix.identity().translate(particle._position);
                particle._dead = nowMs >= particle._lifeTimeMs;
            }
        }
        _dead = count == _particles.length;
    }

    public void draw(Matrix4f vpMatrix) {
        if (!_spawned || _dead) {
            return;
        }
        for (var particle : _particles) {
            if (!particle._dead) {
                _mvpMatrix.set(vpMatrix).mul(particle._modelMatrix);
                _displayMesh.draw(_renderer, _renderer.getDiffuseTextureProgram(), _mvpMatrix);
            }
        }
    }

    private Vector3f generateVelocityVector(int minVelocity, int maxVelocity) {
        int angle0 = randomInteger(0, 359);
        int angle1 = randomInteger(0, 359);
        float x = (float)Math.sin(angle0);
        float y = (float)Math.cos(angle0);
        float z = (float)Math.sin(angle1);
        Vector3f direction = new Vector3f(x, y, z).normalize();
        return direction.mul(randomInteger(minVelocity, maxVelocity) * VELOCITY_SCALE);
    }

    private int randomInteger(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}
