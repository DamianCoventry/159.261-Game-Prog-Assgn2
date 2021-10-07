package com.lunargravity.engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;

public class SmallSmokeParticleSystem extends ParticleSystem {
    private static final int EMIT_BATCH_SIZE = 4;
    private static final int ALPHA_DELTA_MIN = 4;
    private static final int ALPHA_DELTA_MAX = 16;
    private static final int MIN_SMOKE_VELOCITY = 3;
    private static final int MAX_SMOKE_VELOCITY = 5;
    private static final float JITTER_AMOUNT = 0.17f;
    private static final float FRAME_RATE_DIVISOR = 1000.0f;
    private static final float ROTATION_DELTA_POSITIVE = 0.008f;
    private static final float ROTATION_DELTA_NEGATIVE = -0.008f;
    private static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);
    private static final String[] SMOKE_TEXTURE_FILE_NAMES = {
            "images/Smoke0.png", "images/Smoke1.png", "images/Smoke2.png", "images/Smoke3.png"
    };
    private static final int NUM_SMOKE_TEXTURES = 4;

    private final TextureCache _textureCache;
    private int _currentParticle;

    public SmallSmokeParticleSystem(Renderer renderer, int numParticles, String spriteName,
                                    float spriteWidth, float spriteHeight,
                                    Vector4f diffuseColour, String diffuseTextureFileName,
                                    MaterialCache materialCache, TextureCache textureCache) throws IOException {
        super(renderer, numParticles, spriteName, spriteWidth, spriteHeight, diffuseColour, diffuseTextureFileName, materialCache, textureCache);
        _currentParticle = 0;
        _textureCache = textureCache;
        for (var textureFileName : SMOKE_TEXTURE_FILE_NAMES) {
            _textureCache.add(new GlTexture(BitmapImage.fromFile(textureFileName)));
        }
    }

    public void emitSome(long nowMs, Vector3f startPosition, int minLifeTimeMs, int maxLifeTimeMs) {
        for (int i = 0; i < EMIT_BATCH_SIZE; ++i) {
            emitOne(nowMs, startPosition, minLifeTimeMs, maxLifeTimeMs);
        }
    }

    public void emitOne(long nowMs, Vector3f startPosition, int minLifeTimeMs, int maxLifeTimeMs) {
        _spawned = true;
        _dead = false;

        Particle p = _particles[_currentParticle];
        p._diffuseColour.set(1.0f, 1.0f, 1.0f, 1.0f);
        p._modelMatrix.identity();
        p._lifeTimeMs = nowMs + randomInteger(minLifeTimeMs, maxLifeTimeMs);
        p._velocity.y = (float)randomInteger(MIN_SMOKE_VELOCITY, MAX_SMOKE_VELOCITY) / FRAME_RATE_DIVISOR;
        p._dead = false;
        p._rotation = (float)Math.toRadians(randomInteger(0, 359));
        p._rotationDelta = randomInteger(1, 10) > 5 ? ROTATION_DELTA_POSITIVE : ROTATION_DELTA_NEGATIVE;
        p._alphaDelta = (float)randomInteger(ALPHA_DELTA_MIN, ALPHA_DELTA_MAX) / FRAME_RATE_DIVISOR;

        Vector3f jitter = new Vector3f();
        jitter.x = (float)Math.sin(p._rotation);
        jitter.y = (float)Math.cos(p._rotation);
        jitter.mul(JITTER_AMOUNT);
        p._position.set(startPosition).add(jitter);

        int textureIndex = _currentParticle % NUM_SMOKE_TEXTURES;
        p._diffuseTexture = _textureCache.get(SMOKE_TEXTURE_FILE_NAMES[textureIndex]);
        if (++_currentParticle >= _particles.length) {
            _currentParticle = 0;
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
                particle._position.add(particle._velocity);
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
                _displayMesh.draw(_renderer, _renderer.getDiffuseTextureProgram(), _mvpMatrix, particle._diffuseTexture, particle._diffuseColour);
            }
        }
    }
}
