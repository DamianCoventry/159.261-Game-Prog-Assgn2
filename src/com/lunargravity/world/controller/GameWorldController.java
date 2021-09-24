package com.lunargravity.world.controller;

import com.lunargravity.engine.scene.ISceneLogicOwner;
import com.lunargravity.world.model.IGameWorldModel;
import org.joml.Vector2f;

import java.util.HashMap;

public class GameWorldController implements IGameWorldController, ISceneLogicOwner {
    private final IGameWorldControllerObserver _observer;
    private final IGameWorldModel _model;

    public GameWorldController(IGameWorldControllerObserver observer, IGameWorldModel model) {
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
    public void doGameWorldControllerStuff() {
        // TODO
    }

    @Override
    public void logicSettingLoaded(String name, String value) {
        // TODO
    }
}
