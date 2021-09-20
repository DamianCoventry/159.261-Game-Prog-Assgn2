package com.lunargravity.dogfight.controller;

import com.lunargravity.dogfight.model.IDogfightModel;
import com.lunargravity.engine.scene.ISceneLogicOwner;
import org.joml.Vector2f;

import java.util.HashMap;

public class DogfightController implements IDogfightController, ISceneLogicOwner {
    private final IDogfightControllerObserver _observer;
    private final IDogfightModel _model;

    public DogfightController(IDogfightControllerObserver observer, IDogfightModel model) {
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
