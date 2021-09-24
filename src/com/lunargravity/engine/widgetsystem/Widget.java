package com.lunargravity.engine.widgetsystem;

import com.lunargravity.engine.core.IInputConsumer;
import com.lunargravity.engine.graphics.GlRenderer;
import com.lunargravity.engine.graphics.ViewportConfig;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.io.IOException;
import java.util.ArrayList;

public class Widget implements IInputConsumer {
    public static final String INITIAL_POSITION = "initialPosition";
    public static final String CENTER_IN_VIEWPORT = "centerInViewport";
    public static final String CENTER_IN_PARENT = "centerInParent";

    private final WidgetObserver _observer;
    private final Widget _parent;
    private final ArrayList<Widget> _children;
    private final String _id;
    private final String _type;
    private final Matrix4f _projectionMatrix;
    private Vector2f _position;
    private Vector2f _size;

    public Widget(WidgetCreateInfo wci, WidgetObserver observer) throws IOException {
        _parent = null;
        _children = new ArrayList<>();
        _projectionMatrix = new Matrix4f();
        _id = wci._id;
        _type = wci._type;
        _position = calculateInitialPosition(wci, observer.getWidgetManager().getRenderer());
        _size = wci._size;

        _observer = observer;
        _observer.initialise(this, wci);
    }

    public Widget(Widget parent, WidgetCreateInfo wci, WidgetObserver observer) throws IOException {
        _parent = parent;
        _children = new ArrayList<>();
        _projectionMatrix = new Matrix4f();
        _id = wci._id;
        _type = wci._type;
        _position = calculateInitialPosition(wci, observer.getWidgetManager().getRenderer());
        _size = wci._size;

        _observer = observer;
        _observer.initialise(this, wci);
    }

    public String getId() {
        return _id;
    }
    public String getType() {
        return _type;
    }
    public WidgetObserver getObserver() {
        return _observer;
    }
    public Widget getParent() {
        return _parent;
    }

    public void draw(int viewport, Matrix4f projectionMatrix) {
        _projectionMatrix.set(projectionMatrix);
        _observer.widgetDraw(viewport, _projectionMatrix);
        if (_children != null) {
            for (var child : _children) {
                child.draw(viewport, projectionMatrix);
            }
        }
    }

    public void setPosition(Vector2f position) {
        _position = position;
    }
    public Vector2f getPosition() {
        return _position;
    }

    public void setSize(Vector2f size) {
        _size = size;
    }
    public Vector2f getSize() {
        return _size;
    }

    public void addChild(Widget widget) {
        if (!isChild(widget)) {
            _children.add(widget);
        }
    }

    public boolean removeChild(String name) {
        for (int i = 0; i < _children.size(); ++i) {
            if (name.equals(_children.get(i)._id)) {
                _children.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean removeChild(Widget widget) {
        return _children.remove(widget);
    }

    public void clearChildren() {
        _children.clear();
    }

    public boolean isChild(String name) {
        for (Widget child : _children) {
            if (name.equals(child._id)) {
                return true;
            }
        }
        return false;
    }

    public boolean isChild(Widget widget) {
        return _children.contains(widget);
    }

    public Widget getChild(String name) {
        for (Widget child : _children) {
            if (name.equals(child._id)) {
                return child;
            }
        }
        return null;
    }

    public Widget getChild(int i) {
        if (i >= 0 && i < _children.size()) {
            return _children.get(i);
        }
        return null;
    }

    public int childCount() {
        return _children.size();
    }

    public boolean containsPoint(Vector2f point) {
        return (point.x >= _position.x) && (point.x <= _position.x + _size.x) &&
               (point.y >= _position.y) && (point.y <= _position.y + _size.y);
    }

    @Override
    public void keyboardKeyEvent(int key, int scancode, int action, int mods) {
        if (_observer != null) {
            _observer.keyboardKeyEvent(key, scancode, action, mods);
        }
    }

    @Override
    public void mouseButtonEvent(int button, int action, int mods) {
        if (_observer != null) {
            _observer.mouseButtonEvent(button, action, mods);
        }
    }

    @Override
    public void mouseCursorMovedEvent(double xPos, double yPos) {
        if (_observer != null) {
            _observer.mouseCursorMovedEvent(xPos, yPos);
        }
    }

    @Override
    public void mouseWheelScrolledEvent(double xOffset, double yOffset) {
        if (_observer != null) {
            _observer.mouseWheelScrolledEvent(xOffset, yOffset);
        }
    }

    private Vector2f calculateInitialPosition(WidgetCreateInfo wci, GlRenderer renderer) {
        String initialPosition = wci._properties.get(INITIAL_POSITION);
        if (initialPosition != null) {
            if (initialPosition.equals(CENTER_IN_VIEWPORT) && renderer.getNumViewports() > 0) {
                ViewportConfig viewportConfig = renderer.getViewport(0).getConfig();
                wci._position.x = (viewportConfig._width / 2.0f) - (wci._size.x / 2.0f);
                wci._position.y = (viewportConfig._height / 2.0f) - (wci._size.y / 2.0f);
            }
            else if (initialPosition.equals(CENTER_IN_PARENT) && _parent != null) {
                wci._position.x = (_parent.getSize().x / 2.0f) - (wci._size.x / 2.0f);
                wci._position.y = (_parent.getSize().y / 2.0f) - (wci._size.y / 2.0f);
            }
        }

        return wci._position;
    }

    public Vector2f toViewportCoordinates(Vector2f coordinates) {
        Vector2f viewportCoordinates = new Vector2f(coordinates);
        Widget current = _parent;
        while (current != null) {
            viewportCoordinates.add(current._position);
            current = current._parent;
        }
        return viewportCoordinates;
    }

    public Vector2f toMyCoordinates(Vector2f parentCoordinates) {
        return new Vector2f(parentCoordinates.x - _position.x, parentCoordinates.y - _position.y);
    }

    public Widget getGreatestDescendant(Vector2f position) {
        Vector2f myCoordinates = toMyCoordinates(position);
        //System.out.println("Widget Coordinates: " + myCoordinates.x + ", " + myCoordinates.y);
        for (var widget : _children) {
            if (widget.containsPoint(myCoordinates)) {
                Widget descendant = widget.getGreatestDescendant(myCoordinates);
                return descendant != null ? descendant : widget;
            }
        }
        return null;
    }
}
