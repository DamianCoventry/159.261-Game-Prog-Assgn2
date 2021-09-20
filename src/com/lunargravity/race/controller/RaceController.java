package com.lunargravity.race.controller;

import com.lunargravity.engine.scene.ISceneLogicOwner;
import com.lunargravity.race.model.IRaceModel;
import org.joml.Vector2f;

import java.util.HashMap;

public class RaceController implements IRaceController, ISceneLogicOwner {
    private final IRaceControllerObserver _observer;
    private final IRaceModel _model;

    public RaceController(IRaceControllerObserver observer, IRaceModel model) {
        _observer = observer;
        _model = model;
    }

    @Override
    public void onControllerThink() {
        // TODO
    }

    @Override
    public void temp() {
        // TODO
    }

    @Override
    public void onLogicSettingLoaded(String name, String value) {
        // TODO
    }
}
