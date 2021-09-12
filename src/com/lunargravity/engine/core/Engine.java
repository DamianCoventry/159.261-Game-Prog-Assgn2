package com.lunargravity.engine.core;

import com.lunargravity.engine.desktopwindow.*;
import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.scene.ISceneLoadObserver;
import org.lwjgl.glfw.*;

import java.io.IOException;

public class Engine implements IEngine {
    private final IFrameConsumer _frameConsumer;
    private final IInputConsumer _inputConsumer;
    private final GlRenderer _renderer;
    private final IViewportSizeObserver _viewportSizeObserver;

    private GlfwWindow _window;

    private long _currentFrameNo;
    private long _frameCount;
    private long _frameLengthMs;
    private long _currentTimeMs;
    private long _previousTimeMs;
    private long _previousTimestampMs;
    private double _frameDelta;
    private long _fps;

    public Engine(IFrameConsumer frameConsumer, IInputConsumer inputConsumer, IViewportSizeObserver viewportSizeObserver,
                  GlfwWindowConfig windowConfig) throws IOException {

        _frameConsumer = frameConsumer;
        _inputConsumer = inputConsumer;
        _viewportSizeObserver = viewportSizeObserver;

        resetFrameCounters();
        createDesktopWindow(windowConfig);

        _renderer = new GlRenderer(new GlViewportConfig[] {
                createDefaultViewportConfig()
        });
    }

    @Override
    public void freeResources() {
        _window.freeResources();
    }

    @Override
    public void run() {
        int i;
        GlViewport viewport;

        resetFrameCounters();

        while (!_window.shouldClose()) {
            _currentTimeMs = System.currentTimeMillis();
            _previousTimeMs = updateFrameTime(_currentTimeMs, _previousTimeMs);

            _frameConsumer.onFrameBegin(_currentFrameNo, _currentTimeMs, _frameDelta);

            // TODO: physics engine time step
            _frameConsumer.onFrameThink();

            _renderer.clearBuffers();
            for (i = 0; i < _renderer.getNumViewports(); ++i) {
                viewport = _renderer.getViewport(i);
                if (viewport != null) {
                    viewport.activate();
                    _frameConsumer.onFrameDraw3d(i, viewport.getPerspectiveMatrix());
                    _frameConsumer.onFrameDraw2d(i, viewport.getOrthographicMatrix());
                }
            }

            _window.flipPages();
            _frameConsumer.onFrameEnd();
            updateFps();
        }
    }

    @Override
    public void setDefaultViewport() {
        _renderer.setViewports(new GlViewportConfig[] {
                createDefaultViewportConfig()
        });
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
    public void loadScene(String fileName, ISceneLoadObserver loadProgressObserver) {
        // TODO
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
                for (int i = 0; i < _renderer.getNumViewports(); ++i) {
                    GlViewportConfig newConfig = _viewportSizeObserver.onViewportSizeChanged(
                            i, _renderer.getViewport(i).getConfig(),
                            (int)_window.getWidth(), (int)_window.getHeight());
                    if (newConfig != null) {
                        _renderer.getViewport(i).setConfig(newConfig);
                    }
                }
            }
        });

        _window.setKeyCallback(new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (_inputConsumer != null) {
                    _inputConsumer.onKeyboardKeyEvent(key, scancode, action, mods);
                }
            }
        });

        _window.setMouseButtonCallback(new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (_inputConsumer != null) {
                    _inputConsumer.onMouseButtonEvent(button, action, mods);
                }
            }
        });

        _window.setMouseCursorMovementCallback(new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xPos, double yPos) {
                if (_inputConsumer != null) {
                    _inputConsumer.onMouseCursorMovedEvent(xPos, yPos);
                }
            }
        });

        _window.setMouseWheelCallback(new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xOffset, double yOffset) {
                if (_inputConsumer != null) {
                    _inputConsumer.onMouseWheelScrolledEvent(xOffset, yOffset);
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

    private GlViewportConfig createDefaultViewportConfig() {
        GlViewportConfig config = new GlViewportConfig();
        config._positionX = 0;
        config._positionY = 0;
        config._width = (int)_window.getWidth();
        config._height = (int)_window.getHeight();
        config._verticalFovDegrees = 60.0;
        config._perspectiveNcp = 1.0f;
        config._perspectiveFcp = 10000.0f;
        return config;
    }
}
