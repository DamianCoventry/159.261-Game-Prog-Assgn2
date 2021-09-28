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

import java.util.ArrayList;

public class GlStaticMesh {
    private final String _name;

    public GlStaticMesh(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void draw(Matrix4f mvp) {
        // TODO
    }

    public void bindMaterials(ArrayList<GlMaterial> materials) {
        // TODO
    }
}
