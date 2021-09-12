package com.lunargravity.world.controller;

import com.lunargravity.world.model.IGameWorldModel;

public class GameWorldController implements IWorldController {
    private final IGameWorldControllerEvents _eventHandler;
    private final IGameWorldModel _model;

    public GameWorldController(IGameWorldControllerEvents eventHandler, IGameWorldModel model) {
        _eventHandler = eventHandler;
        _model = model;
    }

    @Override
    public void think() {
        // TODO
    }

    @Override
    public void temp() {
        // TODO
    }
}
