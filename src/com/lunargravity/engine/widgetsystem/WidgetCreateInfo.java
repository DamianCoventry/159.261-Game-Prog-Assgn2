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
