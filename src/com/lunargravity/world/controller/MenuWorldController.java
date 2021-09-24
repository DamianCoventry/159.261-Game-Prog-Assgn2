package com.lunargravity.world.controller;

import com.lunargravity.engine.scene.ISceneLogicOwner;
import com.lunargravity.world.model.IMenuWorldModel;
import org.joml.Vector2f;

import java.util.HashMap;

public class MenuWorldController implements IMenuWorldController, ISceneLogicOwner {
    private final IMenuWorldControllerObserver _observer;
    private final IMenuWorldModel _model;

    public MenuWorldController(IMenuWorldControllerObserver observer, IMenuWorldModel model) {
        _observer = observer;
        _model = model;
    }

    @Override
    public void onControllerThink() {
        // TODO
    }

    @Override
    public void doWorldControllerStuff() {
        // TODO
    }

    @Override
    public void doMenuWorldControllerStuff() {
        // TODO
    }

    @Override
    public void logicSettingLoaded(String name, String value) {
        // TODO
    }
}
