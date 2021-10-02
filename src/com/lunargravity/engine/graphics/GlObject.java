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

package com.lunargravity.engine.graphics;

import org.joml.Matrix4f;

public class GlObject {
    public String _name;
    public Transform _transform;
    public DisplayMesh _displayMesh;

    public GlObject(String name, Transform transform) {
        _name = name;
        _transform = transform;
        _displayMesh = null;
    }

    public GlObject(String name, Transform transform, DisplayMesh displayMesh) {
        _name = name;
        _transform = transform;
        _displayMesh = displayMesh;
    }

    public void draw(Matrix4f viewProjectionMatrix) {
        // TODO
    }

    public void draw(Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        // TODO
    }
}
