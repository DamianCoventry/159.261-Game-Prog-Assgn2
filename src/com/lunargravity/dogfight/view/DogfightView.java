package com.lunargravity.dogfight.view;

import com.lunargravity.dogfight.model.IDogfightModel;
import org.joml.Matrix4f;

public class DogfightView implements IDogfightView {
    private final IDogfightModel _model;

    public DogfightView(IDogfightModel model) {
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
