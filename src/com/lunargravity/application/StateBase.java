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
    public ViewportConfig onViewportSizeChanged(int viewport, ViewportConfig viewportConfig, int windowWidth, int windowHeight) {
        //  1. if we're in the menu then
        //       there's one viewport and it occupies the entire window always
        //  2. else if it's a single player game then
        //       there's one viewport and it occupies the entire window always
        //  3. else if it's a two players game then
        //       if the current state is 'get ready', running, 'player died' or completed then
        //           there's two viewports that split the window vertically
        //       else
        //           there's one viewport and it occupies the entire window
        // TODO
        ViewportConfig vpc = new ViewportConfig();
        vpc._viewportIndex = 0; // TODO: this is temporary
        vpc._positionX = 0;
        vpc._positionY = 0;
        vpc._width = windowWidth;
        vpc._height = windowHeight;
        vpc._verticalFovDegrees = viewportConfig._verticalFovDegrees;
        vpc._perspectiveNcp = viewportConfig._perspectiveNcp;
        vpc._perspectiveFcp = viewportConfig._perspectiveFcp;
        return vpc;
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
