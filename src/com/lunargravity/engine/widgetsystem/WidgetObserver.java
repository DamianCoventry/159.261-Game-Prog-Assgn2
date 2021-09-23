package com.lunargravity.engine.widgetsystem;

import com.lunargravity.engine.graphics.PolyhedraVxTc;
import org.joml.Matrix4f;

import java.io.IOException;

public abstract class WidgetObserver implements IWidgetObserver {
    protected WidgetManager _widgetManager;
    protected Widget _widget;

    protected WidgetObserver(WidgetManager widgetManager) {
        _widgetManager = widgetManager;
        _widget = null;
    }

    public void initialise(Widget widget, WidgetCreateInfo wci) throws IOException {
        _widget = widget;
        createChildWidgets(wci);
    }

    protected abstract void createChildWidgets(WidgetCreateInfo wci) throws IOException;

    public Widget getWidget() {
        return _widget;
    }

    protected PolyhedraVxTc createPolyhedraVxTc(float x, float y, float width, float height) {
        float[] vertices = new float[]{
                // triangle 0
                x, y + height, 0.1f,
                x, y, 0.1f,
                x + width, y, 0.1f,
                // triangle 1
                x, y + height, 0.1f,
                x + width, y, 0.1f,
                x + width, y + height, 0.1f
        };
        float[] texCoordinates = new float[]{
                // triangle 0
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                // triangle 1
                0.0f, 0.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };
        return new PolyhedraVxTc(vertices, texCoordinates);
    }

    @Override
    public void widgetOpening() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void widgetOpened() {
        // TODO: Any standard behaviour?
    }

    @Override
    public CloseResult widgetClosing() {
        // TODO: Any standard behaviour?
        return CloseResult.PROCEED_WITH_CLOSE;
    }

    @Override
    public void widgetClosed() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void widgetShowing() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void widgetShown() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void widgetHiding() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void widgetHidden() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void widgetZOrderChanging() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void widgetZOrderChanged() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void widgetLoseKeyboardFocus() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void widgetGainKeyboardFocus() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void widgetLoseMouseCapture() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void widgetGainMouseCapture() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void widgetThink() {
        // TODO: Any standard behaviour?
    }

    @Override
    public void widgetDraw2d(int viewport, Matrix4f projectionMatrix) {
        // TODO: Any standard behaviour?
    }
}
