package com.lunargravity.engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;

public class SmokeParticleSystem extends ParticleSystem {
    private static final int NUM_PARTICLE_RINGS = 3;
    private static final int ALPHA_DELTA_MIN = 4;
    private static final int ALPHA_DELTA_MAX = 16;
    private static final float ALPHA_DELTA_DIVISOR = 1000.0f;
    private static final float ROTATION_DELTA_POSITIVE = 0.008f;
    private static final float ROTATION_DELTA_NEGATIVE = -0.008f;
    private static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);
    private static final String[] SMOKE_TEXTURE_FILE_NAMES = {
            "images/Smoke0.png", "images/Smoke1.png", "images/Smoke2.png", "images/Smoke3.png"
    };
    private static final int NUM_SMOKE_TEXTURES = 4;

    private final TextureCache _textureCache;
    private int _smokeRadius;

    public SmokeParticleSystem(Renderer renderer, int numParticles, String spriteName,
                                   float spriteWidth, float spriteHeight,
                                   Vector4f diffuseColour, String diffuseTextureFileName,
                                   MaterialCache materialCache, TextureCache textureCache) throws IOException {
        super(renderer, numParticles, spriteName, spriteWidth, spriteHeight, diffuseColour, diffuseTextureFileName, materialCache, textureCache);
        _textureCache = textureCache;
        for (var textureFileName : SMOKE_TEXTURE_FILE_NAMES) {
            _textureCache.add(new GlTexture(BitmapImage.fromFile(textureFileName)));
        }
    }

    public void setSmokeRadius(int smokeRadius) {
        _smokeRadius = smokeRadius;
    }

    public void spawn(long nowMs, Vector3f startPosition, int minLifeTimeMs, int maxLifeTimeMs) {
        _spawned = true;
        _dead = false;

        int particlePerRing = _particles.length / NUM_PARTICLE_RINGS;
        if (particlePerRing == 0) {
            return; // Prevent DBZ below
        }
        int numRingParticles = 0;
        int ring = 0;
        int angleDegrees = randomInteger(0, 359);
        final int angleStep = 360 / particlePerRing;

        Vector3f offset = new Vector3f();
        final int radiusStep = _smokeRadius / NUM_PARTICLE_RINGS;
        int radiusMin = 0;
        int radiusMax = radiusStep;

        int count = 0;
        for (var particle : _particles) {
            final float distance = randomInteger(radiusMin, radiusMax);
            offset.x = (float)Math.sin(Math.toRadians(angleDegrees)) * distance;
            offset.y = (float)Math.cos(Math.toRadians(angleDegrees)) * distance;

            particle._position.set(startPosition).add(offset);
            particle._diffuseColour.set(1.0f, 1.0f, 1.0f, 1.0f);
            particle._modelMatrix.identity();
            particle._lifeTimeMs = nowMs + randomInteger(minLifeTimeMs, maxLifeTimeMs);
            particle._dead = false;
            particle._rotation = randomInteger(0, 359);
            particle._rotationDelta = randomInteger(1, 10) > 5 ? ROTATION_DELTA_POSITIVE : ROTATION_DELTA_NEGATIVE;
            particle._alphaDelta = (float)randomInteger(ALPHA_DELTA_MIN, ALPHA_DELTA_MAX) / ALPHA_DELTA_DIVISOR;

            int textureIndex = count % NUM_SMOKE_TEXTURES;
            particle._diffuseTexture = _textureCache.get(SMOKE_TEXTURE_FILE_NAMES[textureIndex]);
            ++count;

            angleDegrees += angleStep;
            if (angleDegrees >= 360) {
                angleDegrees -= 360;
            }

            if (++numRingParticles >= particlePerRing) {
                numRingParticles = 0;
                if (ring < NUM_PARTICLE_RINGS - 1) {
                    ++ring;
                    radiusMin = radiusMax;
                    radiusMax += radiusStep;
                }
            }
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
                particle._rotation += particle._rotationDelta;
                particle._diffuseColour.w -= particle._alphaDelta;
                particle._modelMatrix.identity().translate(particle._position).rotate(particle._rotation, Z_AXIS);
                particle._dead = (nowMs >= particle._lifeTimeMs || particle._diffuseColour.w <= 0.0f);
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
                _displayMesh.draw(_renderer, _renderer.getDiffuseTextureProgram(), _mvpMatrix, particle._diffuseColour);
            }
        }
    }
}
