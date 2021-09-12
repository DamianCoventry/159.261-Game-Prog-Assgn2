package com.lunargravity.application;

import com.lunargravity.engine.graphics.GlViewportConfig;
import org.joml.Matrix4f;

public class StateBase implements IState {
    private final IStateMachineContext _context;

    public StateBase(IStateMachineContext context) {
        _context = context;
    }

    protected IStateMachineContext getContext() {
        return _context;
    }

    @Override
    public void begin() {
        // TODO
    }

    @Override
    public void end() {
        // TODO
    }

    @Override
    public void think() {
        // TODO
    }

    @Override
    public void draw3d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void draw2d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public GlViewportConfig onViewportSizeChanged(int viewport, GlViewportConfig currentConfig, int windowWidth, int windowHeight) {
        // TODO
        return null;
    }

    @Override
    public void onKeyboardKeyEvent(int key, int scancode, int action, int mods) {
        // TODO
    }

    @Override
    public void onMouseButtonEvent(int button, int action, int mods) {
        // TODO
    }

    @Override
    public void onMouseCursorMovedEvent(double xPos, double yPos) {
        // TODO
    }

    @Override
    public void onMouseWheelScrolledEvent(double xOffset, double yOffset) {
        // TODO
    }
}
