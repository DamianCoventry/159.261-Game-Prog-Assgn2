package com.lunargravity.engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;

public class ExplosionParticleSystem extends ParticleSystem {
    private int _minVelocity;
    private int _maxVelocity;

    public ExplosionParticleSystem(Renderer renderer, int numParticles, String spriteName,
                                   float spriteWidth, float spriteHeight,
                                   Vector4f diffuseColour, String diffuseTextureFileName,
                                   MaterialCache materialCache, TextureCache textureCache) throws IOException {
        super(renderer, numParticles, spriteName, spriteWidth, spriteHeight, diffuseColour, diffuseTextureFileName, materialCache, textureCache);
    }

    public void setMinVelocity(int velocity) {
        _minVelocity = velocity;
    }

    public void setMaxVelocity(int velocity) {
        _maxVelocity = velocity;
    }

    public void emitAll(long nowMs, Vector3f startPosition, int minLifeTimeMs, int maxLifeTimeMs) {
        emitAll(nowMs, startPosition, minLifeTimeMs, maxLifeTimeMs, _originalDiffuseTexture);
    }

    public void emitAll(long nowMs, Vector3f startPosition, int minLifeTimeMs, int maxLifeTimeMs, GlTexture diffuseTexture) {
        _spawned = true;
        _dead = false;

        for (var particle : _particles) {
            particle._position.set(startPosition);
            particle._velocity = generateRandomVelocityVector(_minVelocity, _maxVelocity);
            particle._modelMatrix.identity();
            particle._lifeTimeMs = nowMs + randomInteger(minLifeTimeMs, maxLifeTimeMs);
            particle._diffuseTexture = diffuseTexture;
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
                _displayMesh.draw(_renderer, _renderer.getDiffuseTextureProgram(), _mvpMatrix, particle._diffuseTexture, particle._diffuseColour);
            }
        }
    }
}
