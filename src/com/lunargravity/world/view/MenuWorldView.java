package com.lunargravity.world.view;

import com.lunargravity.world.model.IMenuWorldModel;
import org.joml.Matrix4f;

public class MenuWorldView implements IWorldView {
    private final IMenuWorldModel _model;

    public MenuWorldView(IMenuWorldModel model) {
        _model = model;
    }

    @Override
    public void think() {
        // TODO
    }

    @Override
    public void draw3d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void draw2d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void temp() {
        // TODO
    }
}
