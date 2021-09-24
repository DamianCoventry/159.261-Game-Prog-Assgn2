package com.lunargravity.engine.widgetsystem;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;

public class WidgetCreateInfo {
    public WidgetCreateInfo() {
    }

    public WidgetCreateInfo(String id, String type) {
        _id = id;
        _type = type;
    }

    public WidgetCreateInfo getChild(String id, String type) {
        if (_children == null) {
            return null;
        }
        for (var child : _children) {
            if (child._id.equals(id) && child._type.equals(type)) {
                return child;
            }
        }
        return null;
    }

    public String _id;
    public String _type;
    public Vector2f _position;
    public Vector2f _size;
    public HashMap<String, String> _properties;
    public ArrayList<WidgetCreateInfo> _children;
}
