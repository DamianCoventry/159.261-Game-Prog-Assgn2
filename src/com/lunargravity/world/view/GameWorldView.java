package com.lunargravity.world.view;

import com.lunargravity.world.model.IGameWorldModel;
import org.joml.Matrix4f;

public class GameWorldView implements IWorldView {
    private final IGameWorldModel _model;

    public GameWorldView(IGameWorldModel model) {
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
