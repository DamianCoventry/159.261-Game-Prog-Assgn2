package com.lunargravity.engine.widgetsystem;

import com.lunargravity.engine.core.IInputConsumer;
import org.joml.Vector2f;

import java.util.ArrayList;

public class Widget implements IInputConsumer {
    private final WidgetObserver _observer;
    private final ArrayList<Widget> _children;
    private final String _id;
    private final String _type;
    private Vector2f _position;
    private Vector2f _size;

    public Widget(WidgetCreateInfo wci, WidgetObserver observer) {
        _children = new ArrayList<>();
        _id = wci._id;
        _type = wci._type;
        _position = wci._position;
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

    public boolean addChild(Widget widget) {
        if (isChild(widget)) {
            return false;
        }
        _children.add(widget);
        return true;
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
