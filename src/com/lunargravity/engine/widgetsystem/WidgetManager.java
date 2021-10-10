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

import com.lunargravity.engine.animation.AnimationManager;
import com.lunargravity.engine.core.IEngine;
import com.lunargravity.engine.core.IInputObserver;
import com.lunargravity.engine.desktopwindow.GlfwWindow;
import com.lunargravity.engine.graphics.GlViewport;
import com.lunargravity.engine.graphics.Renderer;
import com.lunargravity.engine.graphics.ViewportConfig;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.io.IOException;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL11C.glDepthMask;

public class WidgetManager implements IInputObserver {
    private final LinkedList<Widget> _allWidgets;
    private final LinkedList<Widget> _visibleWidgets;
    private final IEngine _engine;
    private final Renderer _renderer;
    private Widget _keyboardFocus;
    private Widget _mouseCapture;
    private Widget _hoveringOver;

    public WidgetManager(IEngine engine) {
        _allWidgets = new LinkedList<>();
        _visibleWidgets = new LinkedList<>();
        _engine = engine;
        _renderer = _engine.getRenderer();
        _keyboardFocus = null;
        _mouseCapture = null;
        _hoveringOver = null;
    }

    public AnimationManager getAnimationManager() {
        return _engine.getAnimationManager();
    }

    public Renderer getRenderer() {
        return _renderer;
    }

    public void freeNativeResources() {
        for (var widget : _allWidgets) {
            widget.getObserver().freeNativeResources();
        }
        for (var widget : _visibleWidgets) {
            widget.getObserver().freeNativeResources();
        }
        _allWidgets.clear();
        _visibleWidgets.clear();
    }

    public boolean isOpen(Widget widget) {
        return _allWidgets.contains(widget);
    }

    public void think() {
        for (var widget : _allWidgets) {
            widget.getObserver().widgetThink();
        }
    }

    public void draw(Matrix4f projectionMatrix) {
        glDepthMask(false);
        for (var widget : _visibleWidgets) {
            widget.draw(projectionMatrix);
        }
        glDepthMask(true);
    }

    public ViewportConfig onViewportSizeChanged(int viewport, ViewportConfig viewportConfig, int windowWidth, int windowHeight) {
        // Anything to do?
        return null;
    }

    public enum AlreadyOpenBehaviour { BRING_TO_FRONT, SEND_TO_BACK, RE_OPEN, CANCEL_OPEN }
    public enum OpenAs { FIRST, LAST }

    public void open(Widget widget, OpenAs openAs, AlreadyOpenBehaviour behaviour) throws IOException {
        if (isOpen(widget)) {
            switch (behaviour) {
                case BRING_TO_FRONT:
                    bringToFront(widget);
                    return;
                case SEND_TO_BACK:
                    sendToBack(widget);
                    return;
                case RE_OPEN:
                    close(widget);
                    break;
                case CANCEL_OPEN:
                    return;
            }
        }

        widget.getObserver().widgetOpening();
        _allWidgets.add(widget);

        widget.getObserver().widgetShowing();
        if (openAs == OpenAs.FIRST) {
            _visibleWidgets.addFirst(widget);
        }
        else {
            _visibleWidgets.addLast(widget);
        }
        widget.startFadingIn();
        widget.getObserver().widgetShown();

        widget.getObserver().widgetOpened();
    }

    public void close(Widget widget) {
        if (!isOpen(widget)) {
            return;
        }
        if (widget.getObserver().widgetClosing() == IWidgetObserver.CloseResult.CANCEL_CLOSE) {
            return;
        }
        if (isVisible(widget)) {
            widget.startFadingOutThenClose();
        }
        else {
            closeNow(widget);
        }
    }

    public void closeNow(Widget widget) {
        if (!isOpen(widget)) {
            return;
        }
        if (widget.getObserver().widgetClosing() == IWidgetObserver.CloseResult.CANCEL_CLOSE) {
            return;
        }

        if (isVisible(widget)) {
            widget.getObserver().widgetHiding();
            _visibleWidgets.remove(widget);
            widget.getObserver().widgetHidden();
        }

        _allWidgets.remove(widget);
        widget.getObserver().widgetClosed();
    }

    public void closeAll() {
        @SuppressWarnings("unchecked")
        LinkedList<Widget> shallowCopy = (LinkedList<Widget>)_allWidgets.clone();
        for (Widget widget : shallowCopy) {
            close(widget);
        }
        _keyboardFocus = null;
        _mouseCapture = null;
    }

    public boolean isVisible(Widget widget) {
        return _visibleWidgets.contains(widget);
    }

    public enum ShowAs { FIRST, LAST }

    public void show(Widget widget, ShowAs showAs) throws IOException {
        if (!isOpen(widget)) {
            if (showAs == ShowAs.FIRST) {
                open(widget, OpenAs.FIRST, AlreadyOpenBehaviour.BRING_TO_FRONT);
            }
            else {
                open(widget, OpenAs.LAST, AlreadyOpenBehaviour.SEND_TO_BACK);
            }
            return;
        }

        if (isVisible(widget)) {
            if (showAs == ShowAs.FIRST) {
                bringToFront(widget);
            }
            else {
                sendToBack(widget);
            }
            return;
        }

        widget.getObserver().widgetShowing();
        if (showAs == ShowAs.FIRST) {
            _visibleWidgets.addFirst(widget);
            setKeyboardFocusToFrontOfList(); // this might end up too annoying
        }
        else {
            _visibleWidgets.addLast(widget);
        }
        widget.startFadingIn();
        widget.getObserver().widgetShown();
    }

    public void hide(Widget widget) {
        if (!isOpen(widget) || !isVisible(widget)) {
            return;
        }

        widget.startFadingOut();
    }

    public void hideNow(Widget widget) {
        if (!isOpen(widget) || !isVisible(widget)) {
            return;
        }

        widget.getObserver().widgetHiding();
        _visibleWidgets.remove(widget);
        setKeyboardFocusToFrontOfList(); // this might end up too annoying
        widget.getObserver().widgetHidden();
    }

    public void hideAll() {
        @SuppressWarnings("unchecked")
        LinkedList<Widget> shallowCopy = (LinkedList<Widget>)_visibleWidgets.clone();
        for (Widget widget : shallowCopy) {
            hide(widget);
        }
        _keyboardFocus = null;
        _mouseCapture = null;
    }
    
    public void bringToFront(Widget widget) throws IOException {
        if (!isOpen(widget)) {
            open(widget, OpenAs.FIRST, AlreadyOpenBehaviour.BRING_TO_FRONT);
            return;
        }

        if (!isVisible(widget)) {
            show(widget, ShowAs.FIRST);
            return;
        }

        if (_visibleWidgets.getFirst() == widget) {
            return;
        }

        int i = _visibleWidgets.indexOf(widget);
        if (i >= 0) {
            widget.getObserver().widgetZOrderChanging();
            _visibleWidgets.remove(i);
            _visibleWidgets.addFirst(widget);
            widget.startFadingIn();
            setKeyboardFocusToFrontOfList(); // this might end up too annoying
            widget.getObserver().widgetZOrderChanged();
        }
    }

    public void sendToBack(Widget widget) throws IOException {
        if (!isOpen(widget)) {
            open(widget, OpenAs.LAST, AlreadyOpenBehaviour.BRING_TO_FRONT);
            return;
        }

        if (!isVisible(widget)) {
            show(widget, ShowAs.LAST);
            return;
        }

        if (_visibleWidgets.getLast() == widget) {
            return;
        }

        int i = _visibleWidgets.indexOf(widget);
        if (i >= 0) {
            widget.getObserver().widgetZOrderChanging();
            _visibleWidgets.remove(i);
            _visibleWidgets.addLast(widget);
            setKeyboardFocusToFrontOfList(); // this might end up too annoying
            widget.startFadingIn();
            widget.getObserver().widgetZOrderChanged();
        }
    }

    public Widget getKeyboardFocus() {
        return _keyboardFocus;
    }
    public void setKeyboardFocus(Widget widget) {
        if (_keyboardFocus == widget) {
            return;
        }
        if (_keyboardFocus != null) {
            _keyboardFocus.getObserver().widgetLoseKeyboardFocus();
        }
        _keyboardFocus = widget;
        if (_keyboardFocus != null) {
            _keyboardFocus.getObserver().widgetGainKeyboardFocus();
        }
    }

    public Widget getMouseCapture() {
        return _mouseCapture;
    }
    public void setMouseCapture(Widget widget) {
        if (_mouseCapture == widget) {
            return;
        }
        if (_mouseCapture != null) {
            _mouseCapture.getObserver().widgetLoseMouseCapture();
        }
        _mouseCapture = widget;
        if (_mouseCapture != null) {
            _mouseCapture.getObserver().widgetGainMouseCapture();
        }
    }

    public Widget getHoveringOver() {
        return _hoveringOver;
    }

    @Override
    public void keyboardKeyEvent(int key, int scancode, int action, int mods) throws IOException {
        if (_keyboardFocus != null) {
            _keyboardFocus.keyboardKeyEvent(key, scancode, action, mods);
        }
        else if (!_visibleWidgets.isEmpty() && _visibleWidgets.getFirst() != null) {
            _visibleWidgets.getFirst().keyboardKeyEvent(key, scancode, action, mods);
        }
    }

    @Override
    public void mouseButtonEvent(int button, int action, int mods) throws Exception {
        Widget greatestDescendant = null;
        Vector2f viewportCoordinates = toViewportCoordinates(_engine.getMouseCursorPosition());
        if (viewportCoordinates != null) {
            greatestDescendant = getGreatestDescendant(viewportCoordinates);
        }

        _hoveringOver = greatestDescendant;

        if (_mouseCapture != null) {
            _mouseCapture.mouseButtonEvent(button, action, mods);
        }
        else if (greatestDescendant != null) {
            greatestDescendant.mouseButtonEvent(button, action, mods);
        }
    }

    @Override
    public void mouseCursorMovedEvent(double xPos, double yPos) {
        Widget greatestDescendant = null;
        Vector2f viewportCoordinates = toViewportCoordinates(_engine.getMouseCursorPosition());
        if (viewportCoordinates != null) {
            greatestDescendant = getGreatestDescendant(viewportCoordinates);
        }

        _hoveringOver = greatestDescendant;

        if (_mouseCapture != null) {
            _mouseCapture.mouseCursorMovedEvent(xPos, yPos);
        }
        else if (greatestDescendant != null) {
            greatestDescendant.mouseCursorMovedEvent(xPos, yPos);
        }
    }

    @Override
    public void mouseWheelScrolledEvent(double xOffset, double yOffset) {
        Widget greatestDescendant = null;
        Vector2f viewportCoordinates = toViewportCoordinates(_engine.getMouseCursorPosition());
        if (viewportCoordinates != null) {
            greatestDescendant = getGreatestDescendant(viewportCoordinates);
        }

        _hoveringOver = greatestDescendant;

        if (_mouseCapture != null) {
            _mouseCapture.mouseWheelScrolledEvent(xOffset, yOffset);
        }
        else if (greatestDescendant != null) {
            greatestDescendant.mouseWheelScrolledEvent(xOffset, yOffset);
        }
    }

    private void setKeyboardFocusToFrontOfList() {
        if (_visibleWidgets.isEmpty()) {
            setKeyboardFocus(null);
        }
        else {
            setKeyboardFocus(_visibleWidgets.getFirst());
        }
    }

    private Vector2f toViewportCoordinates(GlfwWindow.CursorPosition cursorPosition) {
        // We flip the y coordinate because the mouse cursor is relative to the top left corner
        // of the window but the viewports are relative to the bottom left.
        final float yPosition = _engine.getDesktopWindowHeight() - cursorPosition.m_YPos;

        GlViewport viewport = _engine.getRenderer().getOrthographicViewport();
        if (viewport.containsPoint(cursorPosition.m_XPos, yPosition)) {
            return new Vector2f(cursorPosition.m_XPos - viewport.getConfig()._positionX,
                             yPosition - viewport.getConfig()._positionY);
        }
        return null;
    }

    private Widget getGreatestDescendant(Vector2f position) {
        //System.out.println("Viewport Coordinates: " + position.x + ", " + position.y);
        for (var widget : _visibleWidgets) {
            if (widget.containsPoint(position)) {
                Widget descendant = widget.getGreatestDescendant(position);
                return descendant != null ? descendant : widget;
            }
        }
        return null;
    }
}
