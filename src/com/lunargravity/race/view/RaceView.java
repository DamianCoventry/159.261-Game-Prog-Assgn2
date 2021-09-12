package com.lunargravity.race.view;

import com.lunargravity.race.model.IRaceModel;
import org.joml.Matrix4f;

public class RaceView implements IRaceView {
    private final IRaceModel _model;

    public RaceView(IRaceModel model) {
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
