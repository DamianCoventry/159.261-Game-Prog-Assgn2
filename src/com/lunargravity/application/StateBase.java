package com.lunargravity.application;

import com.lunargravity.engine.core.IManualFrameUpdater;
import com.lunargravity.engine.graphics.GlRenderer;
import com.lunargravity.engine.graphics.ViewportConfig;
import com.lunargravity.engine.timeouts.TimeoutManager;
import org.joml.Matrix4f;

import java.io.IOException;
import java.util.function.Function;

public class StateBase implements IState {
    private final IStateMachineContext _context;

    public StateBase(IStateMachineContext context) {
        _context = context;
    }

    protected IStateMachineContext getContext() {
        return _context;
    }

    protected void changeState(IState state) {
        _context.changeState(state);
    }

    protected int addTimeout(long timeoutMs, Function<Integer, TimeoutManager.CallbackResult> callback) {
        return _context.getEngine().getTimeoutManager().addTimeout(timeoutMs, callback);
    }

    protected void removeTimeout(int timeoutId) {
        _context.getEngine().getTimeoutManager().removeTimeout(timeoutId);
    }

    @Override
    public void begin() throws IOException, InterruptedException {
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
    public IManualFrameUpdater getManualFrameUpdater() {
        return _context.getEngine();
    }

    @Override
    public GlRenderer getRenderer() {
        return _context.getEngine().getRenderer();
    }

    @Override
    public ViewportConfig onViewportSizeChanged(int viewport, ViewportConfig currentConfig, int windowWidth, int windowHeight) {
        // TODO
        return null;
    }

    @Override
    public void keyboardKeyEvent(int key, int scancode, int action, int mods) {
        // TODO
    }

    @Override
    public void mouseButtonEvent(int button, int action, int mods) {
        // TODO
    }

    @Override
    public void mouseCursorMovedEvent(double xPos, double yPos) {
        // TODO
    }

    @Override
    public void mouseWheelScrolledEvent(double xOffset, double yOffset) {
        // TODO
    }
}
