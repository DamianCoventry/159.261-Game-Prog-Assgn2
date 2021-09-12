package com.lunargravity.world.controller;

import com.lunargravity.world.model.IMenuWorldModel;

public class MenuWorldController implements IWorldController {
    private final IMenuWorldControllerEvents _eventHandler;
    private final IMenuWorldModel _model;

    public MenuWorldController(IMenuWorldControllerEvents eventHandler, IMenuWorldModel model) {
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
