package com.lunargravity.engine.widgetsystem;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;

public class WidgetCreateInfo {
    public WidgetCreateInfo(String id, String type) {
        _id = id;
        _type = type;
    }

    public String _id;
    public String _type;
    public Vector2f _position;
    public Vector2f _size;
    public HashMap<String, String> _properties;
    public ArrayList<WidgetCreateInfo> _children;
}
