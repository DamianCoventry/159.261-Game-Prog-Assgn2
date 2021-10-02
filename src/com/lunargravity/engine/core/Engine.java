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

package com.lunargravity.engine.core;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.system.NativeLibraryLoader;
import com.lunargravity.engine.desktopwindow.GlfwWindow;
import com.lunargravity.engine.desktopwindow.GlfwWindowConfig;
import com.lunargravity.engine.graphics.GlViewport;
import com.lunargravity.engine.graphics.Renderer;
import com.lunargravity.engine.graphics.ViewportConfig;
import com.lunargravity.engine.timeouts.TimeoutManager;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.*;

import java.io.File;
import java.io.IOException;

public class Engine implements IEngine {
    static private final float PHYSICS_TIME_STEP = 0.01666666f; // which is 60 FPS

    private final IFrameObserver _frameObserver;
    private final IInputObserver _inputObserver;
    private final IViewportSizeObserver _viewportSizeObserver;

    private final Renderer _renderer;
    private final TimeoutManager _timeoutManager;
    private final PhysicsSpace _physicsSpace;
    private PhysicsCollisionListener _physicsCollisionListener;
    private GlfwWindow _window;

    private long _currentFrameNo;
    private long _frameCount;
    private long _frameLengthMs;
    private long _currentTimeMs;
    private long _previousTimeMs;
    private long _previousTimestampMs;
    private double _frameDelta;
    private long _fps;

    public Engine(IFrameObserver frameObserver, IInputObserver inputObserver, IViewportSizeObserver viewportSizeObserver,
                  GlfwWindowConfig windowConfig) throws IOException {

        loadBullet3RuntimeLibrary();

        _frameObserver = frameObserver;
        _inputObserver = inputObserver;
        _viewportSizeObserver = viewportSizeObserver;
        _timeoutManager = new TimeoutManager();

        _physicsSpace = new PhysicsSpace(PhysicsSpace.BroadphaseType.DBVT);
        _physicsSpace.addOngoingCollisionListener(event -> {
            if (_physicsCollisionListener != null) {
                _physicsCollisionListener.collision(event);
            }
        });

        resetFrameCounters();
        createDesktopWindow(windowConfig);

        _renderer = new Renderer(
                (int)_window.getWidth(),
                (int)_window.getHeight(),
                new ViewportConfig[] {
                    ViewportConfig.createFullWindow((int)_window.getWidth(), (int)_window.getHeight())
        });
    }

    @Override
    public void freeResources() {
        _window.freeResources();
    }

    @Override
    public void run() throws Exception {
        resetFrameCounters();
        while (!_window.shouldClose()) {
            _currentTimeMs = System.currentTimeMillis();
            _previousTimeMs = updateFrameTime(_currentTimeMs, _previousTimeMs);

            _timeoutManager.dispatchTimeouts(_currentTimeMs);
            _frameObserver.onFrameBegin(_currentFrameNo, _currentTimeMs, _frameDelta);

            _physicsSpace.update(PHYSICS_TIME_STEP, 0);
            _physicsSpace.distributeEvents();
            _frameObserver.onFrameThink();

            _renderer.clearBuffers();
            for (int i = 0; i < _renderer.getNumPerspectiveViewports(); ++i) {
                GlViewport viewport = _renderer.getPerspectiveViewport(i);
                if (viewport != null) {
                    viewport.activate();
                    _frameObserver.onFrameDraw3d(i, viewport.getPerspectiveMatrix());
                }
            }

            _renderer.getOrthographicViewport().activate();
            _frameObserver.onFrameDraw2d(_renderer.getOrthographicViewport().getOrthographicMatrix());

            _window.flipPages();
            _frameObserver.onFrameEnd();
            updateFps();
        }
    }

    @Override
    public void exit() {
        _window.close();
    }

    @Override
    public void setDefaultViewport() {
        _renderer.setPerspectiveViewports(
                new ViewportConfig[]{
                        ViewportConfig.createFullWindow((int) _window.getWidth(), (int) _window.getHeight())
                });
    }

    @Override
    public Vector2f[] getPerspectiveViewportSizes() {
        Vector2f[] viewportSizes = new Vector2f[_renderer.getNumPerspectiveViewports()];
        for (int i = 0; i < _renderer.getNumPerspectiveViewports(); ++i) {
            ViewportConfig config = _renderer.getPerspectiveViewport(i).getConfig();
            viewportSizes[i] = new Vector2f(config._width, config._height);
        }
        return viewportSizes;
    }

    @Override
    public void prepareNewFrame() {
        _renderer.getOrthographicViewport().activate();
        _renderer.clearBuffers();
    }

    @Override
    public void submitFrame() {
        _window.flipPages();
    }

    @Override
    public Vector2f getOrthographicViewportSize() {
        return new Vector2f(
                _renderer.getOrthographicViewport().getConfig()._width,
                _renderer.getOrthographicViewport().getConfig()._height
        );
    }

    @Override
    public Matrix4f getPerspectiveProjectionMatrix() {
        if (_renderer.getNumPerspectiveViewports() == 0) {
            throw new RuntimeException("There are no viewports");
        }
        return _renderer.getPerspectiveViewport(0).getPerspectiveMatrix();
    }

    @Override
    public Matrix4f getOrthographicProjectionMatrix() {
        if (_renderer.getNumPerspectiveViewports() == 0) {
            throw new RuntimeException("There are no viewports");
        }
        return _renderer.getPerspectiveViewport(0).getOrthographicMatrix();
    }

    @Override
    public long getFps() {
        return _fps;
    }

    @Override
    public long getFrameLengthMs() {
        return _frameLengthMs;
    }

    @Override
    public long getNowMs() {
        return _currentTimeMs;
    }

    @Override
    public TimeoutManager getTimeoutManager() {
        return _timeoutManager;
    }

    @Override
    public PhysicsSpace getPhysicsSpace() {
        return _physicsSpace;
    }

    @Override
    public Renderer getRenderer() {
        return _renderer;
    }

    @Override
    public GlfwWindow.CursorPosition getMouseCursorPosition() {
        return _window.getMouseCursorPosition();
    }

    @Override
    public float getDesktopWindowWidth() {
        return _window.getWidth();
    }

    @Override
    public float getDesktopWindowHeight() {
        return _window.getHeight();
    }

    @Override
    public void setPhysicsCollisionListener(PhysicsCollisionListener listener) {
        _physicsCollisionListener = listener;
    }

    @Override
    public boolean isSoundEnabled() {
        // TODO
        return false;
    }

    @Override
    public void enableSound() {
        // TODO
    }

    @Override
    public void disableSound() {
        // TODO
    }

    @Override
    public void setSoundVolume(int volume) {
        // TODO
    }

    @Override
    public int getSoundVolume() {
        // TODO
        return 0;
    }

    @Override
    public boolean isMusicEnabled() {
        // TODO
        return false;
    }

    @Override
    public void enableMusic() {
        // TODO
    }

    @Override
    public void disableMusic() {
        // TODO
    }

    @Override
    public void setMusicVolume(int volume) {
        // TODO
    }

    @Override
    public int getMusicVolume() {
        // TODO
        return 0;
    }

    private void loadBullet3RuntimeLibrary() {
        File directory = new File("lib");
        boolean success = NativeLibraryLoader.loadLibbulletjme(true, directory, "Release", "Dp");
        if (!success) {
            throw new RuntimeException("Failed to load the Bullet3 run-time library");
        }
    }

    private void resetFrameCounters() {
        _currentFrameNo = _frameCount = 0;
        _frameLengthMs = _currentTimeMs = 0;
        _previousTimeMs = _previousTimestampMs = 0;
        _fps = 0; _frameDelta = 0.0;
    }

    private void createDesktopWindow(GlfwWindowConfig windowConfig) throws IOException {
        _window = new GlfwWindow(windowConfig, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                if (_renderer == null || _viewportSizeObserver == null) {
                    return;
                }
                for (int i = 0; i < _renderer.getNumPerspectiveViewports(); ++i) {
                    ViewportConfig newConfig = _viewportSizeObserver.onViewportSizeChanged(
                            i, _renderer.getPerspectiveViewport(i).getConfig(),
                            (int)_window.getWidth(), (int)_window.getHeight());
                    if (newConfig != null) {
                        _renderer.getPerspectiveViewport(i).setConfig(newConfig);
                    }
                }
            }
        });

        _window.setKeyCallback(new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (_inputObserver != null) {
                    _inputObserver.keyboardKeyEvent(key, scancode, action, mods);
                }
            }
        });

        _window.setMouseButtonCallback(new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (_inputObserver != null) {
                    try {
                        _inputObserver.mouseButtonEvent(button, action, mods);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        _window.setMouseCursorMovementCallback(new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xPos, double yPos) {
                if (_inputObserver != null) {
                    _inputObserver.mouseCursorMovedEvent(xPos, yPos);
                }
            }
        });

        _window.setMouseWheelCallback(new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xOffset, double yOffset) {
                if (_inputObserver != null) {
                    _inputObserver.mouseWheelScrolledEvent(xOffset, yOffset);
                }
            }
        });
    }

    private long updateFrameTime(long nowMs, long prevMs) {
        _frameLengthMs = nowMs - prevMs;
        return nowMs;
    }

    private void updateFps() {
        ++_frameCount; ++_currentFrameNo;
        if (_currentTimeMs - _previousTimestampMs >= 1000) {
            _previousTimestampMs = _currentTimeMs;
            _fps = _frameCount;
            _frameCount = 0;
        }
    }
}
