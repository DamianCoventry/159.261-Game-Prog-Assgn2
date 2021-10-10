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

package com.lunargravity.engine.desktopwindow;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import java.io.IOException;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GlfwWindow {
    static private final int GL_MAJOR_VERSION = 4;
    static private final int GL_MINOR_VERSION = 5; // Version 4.5 was released in 2014
    static public final int MIN_WIDTH = 800;
    static public final int MIN_HEIGHT = 600;

    private GlfwWindowConfig _config;
    private GLFWWindowSizeCallback _sizeCallback;
    private long _window;
    private float _actualWidth;
    private float _actualHeight;
    private HashMap<Integer, Long> _mouseCursors;

    public GlfwWindow(GlfwWindowConfig config, GLFWWindowSizeCallback sizeCallback) throws IOException {
        setErrorCallback();
        validateConfig(config);
        initialise();
        createWindow(sizeCallback);

        if (_config._centered) {
            centerInPrimaryMonitor();
        }
        else {
            setPosition(_config._positionX, _config._positionY);
        }

        activate();

        if (config._iconFileNames != null) {
            setIcon(config._iconFileNames);
        }

        if (config._mouseCursors != null) {
            for (var key : config._mouseCursors.keySet()) {
                GlfwWindowConfig.MouseCursorConfig mcc = config._mouseCursors.get(key);
                addMouseCursor(key, mcc._fileName, mcc._clickXOffset, mcc._clickYOffset);
            }
            activateMouseCursor(config._initialMouseCursor);
        }

        glfwShowWindow(_window);
    }
    public void setKeyCallback(GLFWKeyCallbackI callback) {
        glfwSetKeyCallback(_window, callback);
    }

    public void setMouseButtonCallback(GLFWMouseButtonCallbackI callback) {
        glfwSetMouseButtonCallback(_window, callback);
    }

    public void setMouseWheelCallback(GLFWScrollCallbackI callback) {
        glfwSetScrollCallback(_window, callback);
    }

    public void setMouseCursorMovementCallback(GLFWCursorPosCallback callback) {
        glfwSetCursorPosCallback(_window, callback);
    }

    public static class CursorPosition {
        public float m_XPos;
        public float m_YPos;
        public CursorPosition(float xPos, float yPos) {
            m_XPos = xPos;
            m_YPos = yPos;
        }
    }

    public CursorPosition getMouseCursorPosition() {
        double[] xPos = new double[1];
        double[] yPos = new double[1];
        glfwGetCursorPos(_window, xPos, yPos);
        return new CursorPosition((float)xPos[0], (float)yPos[0]);
    }

    public void addMouseCursor(int uniqueId, String fileName, int clickXOffset, int clickYOffset) throws IOException {
        if (_mouseCursors == null) {
            _mouseCursors = new HashMap<>();
        }
        GLFWImage image = GlfwImage.fromFile(fileName);
        long mouseCursor = glfwCreateCursor(image, clickXOffset, clickYOffset);
        image.free();
        if (mouseCursor == NULL) {
            throw new RuntimeException("Unable to create a mouse cursor from the file [" + fileName + "]");
        }
        _mouseCursors.put(uniqueId, mouseCursor);
    }

    public void activateMouseCursor(int uniqueId) {
        if (_mouseCursors == null) {
            return;
        }
        if (_mouseCursors.containsKey(uniqueId)) {
            glfwSetCursor(_window, _mouseCursors.get(uniqueId));
        }
    }

    public void close() {
        glfwSetWindowShouldClose(_window, true);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(_window);
    }

    public void flipPages() {
        glfwSwapBuffers(_window);
        glfwPollEvents();
    }

    public void freeNativeResources() {
        if (_mouseCursors != null) {
            for (var mouseCursor : _mouseCursors.values()) {
                glfwDestroyCursor(mouseCursor);
            }
        }
        glfwDestroyWindow(_window);
        glfwTerminate();
    }

    public void centerInPrimaryMonitor() {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidMode == null) {
            throw new RuntimeException("Failed to get the video mode of the primary monitor");
        }

        glfwSetWindowPos(_window,
                (vidMode.width() - _config._width) / 2,
                (vidMode.height() - _config._height) / 2);
    }

    public void setPosition(int x, int y) {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
                _window,
                clampXCoordinate(x, (int)_actualWidth, vidMode),
                clampYCoordinate(y, (int)_actualHeight, vidMode));
    }

    public float getWidth() {
        return _actualWidth;
    }

    public float getHeight() {
        return _actualHeight;
    }

    private void setErrorCallback() {
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
    }

    private void validateConfig(GlfwWindowConfig config) {
        _config = new GlfwWindowConfig();
        _config._title = (config._title != null && !config._title.trim().isEmpty() ? config._title : "Video Game");
        _config._positionX = Math.max(0, config._positionX);
        _config._positionY = Math.max(0, config._positionY);
        _config._width = Math.max(config._width, MIN_WIDTH);
        _config._height = Math.max(config._height, MIN_HEIGHT);
        _config._resizeable = config._resizeable;
        _config._centered = config._centered;
    }

    private void initialise() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, _config._resizeable ? GL_TRUE :  GL_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, GL_MAJOR_VERSION);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, GL_MINOR_VERSION);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
    }

    private void createWindow(GLFWWindowSizeCallback sizeCallback) {
        _window = glfwCreateWindow(_config._width, _config._height, _config._title, 0, 0);
        if (_window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        _actualWidth = _config._width;
        _actualHeight = _config._height;
        _sizeCallback = sizeCallback;

        glfwSetWindowSizeLimits(_window, MIN_WIDTH, MIN_HEIGHT, GLFW_DONT_CARE, GLFW_DONT_CARE);

        glfwSetWindowSizeCallback(_window, (window, width, height) -> {
            _actualWidth = Math.max(width, MIN_WIDTH);
            _actualHeight = Math.max(height, MIN_HEIGHT);
            if (glfwGetCurrentContext() > 0 && _sizeCallback != null) {
                _sizeCallback.invoke(window, width, height);
            }
        });
    }

    private void activate() {
        glfwMakeContextCurrent(_window);
        glfwSwapInterval(1); // Sync to monitor's refresh rate

        // This line is critical for LWJGL's interoperation with GLFW's OpenGL context, or any context that is
        // managed externally. LWJGL detects the context that is current in the current thread, creates the
        // ContextCapabilities instance and makes the OpenGL bindings available for use.
        GL.createCapabilities();
    }

    private void setIcon(String[] iconFileNames) throws IOException {
        GLFWImage.Buffer images = GLFWImage.malloc(iconFileNames.length);

        int i = 0;
        for (var fileName : iconFileNames) {
            GLFWImage image = GlfwImage.fromFile(fileName);
            images.put(i++, image);
            image.free();
        }

        glfwSetWindowIcon(_window, images);
        images.free();
    }

    static private int clampXCoordinate(int x, int windowWidth, GLFWVidMode vidMode) {
        if (vidMode == null) {
            vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (vidMode == null) {
                return x;
            }
        }

        int adjustedX = Math.max(0, x);
        if (adjustedX + windowWidth > vidMode.width()) {
            adjustedX = vidMode.width() - windowWidth;
        }
        
        // if the x is now negative, then just center the window horizontally
        if (adjustedX < 0) {
            adjustedX = (vidMode.width() - windowWidth) / 2;
        }
        
        return adjustedX;
    }

    static private int clampYCoordinate(int y, int windowHeight, GLFWVidMode vidMode) {
        if (vidMode == null) {
            vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (vidMode == null) {
                return y;
            }
        }

        int adjustedY = Math.max(0, y);
        if (adjustedY + windowHeight > vidMode.height()) {
            adjustedY = vidMode.height() - windowHeight;
        }

        // if the y is now negative, then just center the window vertically
        if (adjustedY < 0) {
            adjustedY = (vidMode.height() - windowHeight) / 2;
        }

        return adjustedY;
    }
}
