package com.lunargravity.engine.widgetsystem;

import com.lunargravity.engine.core.IInputConsumer;
import com.lunargravity.engine.graphics.*;
import org.joml.Matrix4f;

import java.util.LinkedList;

import static org.lwjgl.opengl.GL11C.glDepthMask;

public class WidgetManager implements IInputConsumer {
    private final LinkedList<Widget> _allWidgets;
    private final LinkedList<Widget> _visibleWidgets;
    private final GlRenderer _renderer;
    private Widget _keyboardFocus;
    private Widget _mouseCapture;

    public WidgetManager(GlRenderer renderer) {
        _allWidgets = new LinkedList<>();
        _visibleWidgets = new LinkedList<>();
        _renderer = renderer;
        _keyboardFocus = null;
        _mouseCapture = null;
    }

    public GlRenderer getRenderer() {
        return _renderer;
    }

    public void freeResources() {
        for (var widget : _allWidgets) {
            widget.getObserver().freeResources();
        }
        for (var widget : _visibleWidgets) {
            widget.getObserver().freeResources();
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

    public void draw2d(int viewport, Matrix4f projectionMatrix) {
        glDepthMask(false);
        for (var widget : _visibleWidgets) {
            widget.getObserver().widgetDraw2d(viewport, projectionMatrix);
        }
        glDepthMask(true);
    }

    public ViewportConfig onViewportSizeChanged(int viewport, ViewportConfig newViewportConfig, int windowWidth, int windowHeight) {
        // TODO
        return null;
    }

    public enum AlreadyOpenBehaviour { BRING_TO_FRONT, SEND_TO_BACK, RE_OPEN, CANCEL_OPEN }
    public enum OpenAs { FIRST, LAST }

    public void open(Widget widget, OpenAs openAs, AlreadyOpenBehaviour behaviour) {
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

        _allWidgets.remove(widget);

        widget.getObserver().widgetHiding();
        _visibleWidgets.remove(widget);
        widget.getObserver().widgetHidden();

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

    public void show(Widget widget, ShowAs showAs) {
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
        widget.getObserver().widgetShown();
    }

    public void hide(Widget widget) {
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
    
    public void bringToFront(Widget widget) {
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
            setKeyboardFocusToFrontOfList(); // this might end up too annoying
            widget.getObserver().widgetZOrderChanged();
        }
    }

    public void sendToBack(Widget widget) {
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

    @Override
    public void onKeyboardKeyEvent(int key, int scancode, int action, int mods) {
        if (_keyboardFocus != null) {
            _keyboardFocus.onKeyboardKeyEvent(key, scancode, action, mods);
        }
        else if (_visibleWidgets.getFirst() != null) {
            _visibleWidgets.getFirst().onKeyboardKeyEvent(key, scancode, action, mods);
        }
    }

    @Override
    public void onMouseButtonEvent(int button, int action, int mods) {
        if (_mouseCapture != null) {
            _mouseCapture.onMouseButtonEvent(button, action, mods);
            return;
        }
        // TODO: key widget under cursor
    }

    @Override
    public void onMouseCursorMovedEvent(double xPos, double yPos) {
        if (_mouseCapture != null) {
            _mouseCapture.onMouseCursorMovedEvent(xPos, yPos);
            return;
        }
        // TODO: key widget under cursor
    }

    @Override
    public void onMouseWheelScrolledEvent(double xOffset, double yOffset) {
        if (_mouseCapture != null) {
            _mouseCapture.onMouseWheelScrolledEvent(xOffset, yOffset);
            return;
        }
        // TODO: key widget under cursor
    }

    private void setKeyboardFocusToFrontOfList() {
        if (_visibleWidgets.isEmpty()) {
            setKeyboardFocus(null);
        }
        else {
            setKeyboardFocus(_visibleWidgets.getFirst());
        }
    }
}
