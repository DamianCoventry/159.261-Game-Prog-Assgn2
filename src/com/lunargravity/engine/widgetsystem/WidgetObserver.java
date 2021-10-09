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

package com.lunargravity.engine.widgetsystem;

import com.lunargravity.engine.animation.LinearInterpolation;
import com.lunargravity.engine.core.IInputObserver;
import com.lunargravity.engine.graphics.BitmapImage;
import com.lunargravity.engine.graphics.GlDiffuseTextureProgram;
import com.lunargravity.engine.graphics.GlTexture;
import com.lunargravity.engine.graphics.PolyhedraVxTc;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.IOException;
import java.util.function.LongConsumer;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class WidgetObserver implements IWidgetObserver, IInputObserver {
    public static final String BACKGROUND_IMAGE = "backgroundImage";
    public static final String BACKGROUND_ALPHA = "backgroundAlpha";
    public static final String HOVER_IMAGE = "hoverImage";
    public static final long FADE_IN_OUT_TIME = 400;

    protected WidgetManager _widgetManager;
    protected final GlDiffuseTextureProgram _program;
    protected Widget _widget;
    protected GlTexture _backgroundTexture;
    protected GlTexture _hoverTexture;
    protected PolyhedraVxTc _polyhedra;
    protected Matrix4f _modelMatrix;
    protected Vector4f _backgroundColour;
    protected Vector4f _fadingColour;
    protected LinearInterpolation _backgroundAlpha;
    private boolean _fadingOut;

    protected WidgetObserver(WidgetManager widgetManager) {
        _widgetManager = widgetManager;
        _widget = null;
        _backgroundColour = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        _fadingColour = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        _program = _widgetManager.getRenderer().getDiffuseTextureProgram();
        _modelMatrix = new Matrix4f();
        _backgroundAlpha = new LinearInterpolation(_widgetManager.getAnimationManager());
        _fadingOut = false;
    }

    public WidgetManager getWidgetManager() {
        return _widgetManager;
    }

    public void startFadingIn() {
        _fadingOut = false;
        if (_widget.getParent() == null) {
            _backgroundAlpha.setCompletedFunction(null);
        }
        _backgroundAlpha.start(0.0f, _backgroundColour.w, FADE_IN_OUT_TIME);
    }

    public void startFadingOut() {
        if (_fadingOut) {
            return;
        }
        _fadingOut = true;

        if (_widget.getParent() == null) {
            _backgroundAlpha.setCompletedFunction(elapsedTime -> {
                _widgetManager.hideNow(_widget);
            });
        }
        _backgroundAlpha.start(_backgroundColour.w, 0.0f, FADE_IN_OUT_TIME);
    }

    public void startFadingOutThenClose() {
        if (_fadingOut) {
            return;
        }
        _fadingOut = true;

        if (_widget.getParent() == null) {
            _backgroundAlpha.setCompletedFunction(elapsedTime -> {
                _widgetManager.closeNow(_widget);
            });
        }
        _backgroundAlpha.start(_backgroundColour.w, 0.0f, FADE_IN_OUT_TIME);
    }

    public void setFadingCompletedFunction(LongConsumer completedFunction) {
        _backgroundAlpha.setCompletedFunction(completedFunction);
    }

    public void initialise(Widget widget, WidgetCreateInfo wci) throws IOException {
        _widget = widget;

        if (wci._properties == null) {
            throw new RuntimeException("Widget create info does not contain any properties");
        }

        String stringValue = wci._properties.get(BACKGROUND_IMAGE);
        if (stringValue == null) {
            throw new RuntimeException("Widget create info does not contain an entry for [" + BACKGROUND_IMAGE + "]");
        }
        _backgroundTexture = new GlTexture(BitmapImage.fromFile(stringValue));

        float alpha = 1.0f;
        stringValue = wci._properties.get(BACKGROUND_ALPHA);
        if (stringValue != null) {
            alpha = Math.min(1.0f, Math.max(0.0f, Float.parseFloat(stringValue)));
        }
        _backgroundColour.w = alpha;

        stringValue = wci._properties.get(HOVER_IMAGE);
        if (stringValue != null) {
            _hoverTexture = new GlTexture(BitmapImage.fromFile(stringValue));
        }

        _polyhedra = createPolyhedraVxTc(wci._size.x, wci._size.y);

        initialiseChildren(wci);
    }

    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        // nothing by default?
    }

    public Widget getWidget() {
        return _widget;
    }

    public void setBackgroundImage(String imageFileName) throws IOException {
        _backgroundTexture = new GlTexture(BitmapImage.fromFile(imageFileName));
    }
    public void setHoverImage(String imageFileName) throws IOException {
        _hoverTexture = new GlTexture(BitmapImage.fromFile(imageFileName));
    }

    protected PolyhedraVxTc createPolyhedraVxTc(float width, float height) {
        float[] vertices = new float[]{
                // triangle 0
                0, height, 0.1f,
                0, 0, 0.1f,
                width, 0, 0.1f,
                // triangle 1
                0, height, 0.1f,
                width, 0, 0.1f,
                width, height, 0.1f
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
    public void freeResources() {
        if (_polyhedra != null) {
            _polyhedra.freeResources();
        }
        if (_backgroundTexture != null) {
            _backgroundTexture.freeResources();
        }
        if (_hoverTexture != null) {
            _hoverTexture.freeResources();
        }
        _backgroundAlpha.unregister();
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
    public void widgetShown() throws IOException {
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
    public void widgetDraw(Matrix4f projectionMatrix) {
        if (_backgroundTexture == null || _polyhedra == null) {
            return;
        }

        Vector2f viewportPosition = _widget.toViewportCoordinates(_widget.getPosition());

        _modelMatrix = _modelMatrix.identity().translate(viewportPosition.x, viewportPosition.y, 0.0f);
        // there is no view matrix when using an orthographic projection matrix
        Matrix4f mvpMatrix = projectionMatrix.mul(_modelMatrix);

        _widgetManager.getRenderer().activateTextureImageUnit(0);
        if (_hoverTexture != null && _widget == _widgetManager.getHoveringOver()) {
            glBindTexture(GL_TEXTURE_2D, _hoverTexture.getId());
        }
        else {
            glBindTexture(GL_TEXTURE_2D, _backgroundTexture.getId());
        }

        if (_backgroundAlpha.isCompleted()) {
            _program.setDiffuseColour(_backgroundColour);
        }
        else {
            _fadingColour.w = _backgroundAlpha.getValue();
            _program.setDiffuseColour(_fadingColour);
        }

        _program.activate(mvpMatrix);
        _polyhedra.draw();
    }

    @Override
    public void widgetParentResized(float width, float height) {
        // TODO: Any standard behaviour?
    }

    @Override
    public void keyboardKeyEvent(int key, int scancode, int action, int mods) throws IOException {
        // TODO: Any standard behaviour?
    }

    @Override
    public void mouseButtonEvent(int button, int action, int mods) throws Exception {
        if (action == GLFW_PRESS) {
            _widgetManager.setMouseCapture(_widget);
        }
        else if (action == GLFW_RELEASE) {
            _widgetManager.setMouseCapture(null);
        }
    }

    @Override
    public void mouseCursorMovedEvent(double xPos, double yPos) {
        // TODO: Any standard behaviour?
    }

    @Override
    public void mouseWheelScrolledEvent(double xOffset, double yOffset) {
        // TODO: Any standard behaviour?
    }
}
