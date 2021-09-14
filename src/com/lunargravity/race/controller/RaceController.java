package com.lunargravity.race.controller;

import com.lunargravity.race.model.IRaceModel;

public class RaceController implements IRaceController {
    private final IRaceControllerEvents _eventHandler;
    private final IRaceModel _model;

    public RaceController(IRaceControllerEvents eventHandler, IRaceModel model) {
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

    @Override
    public void onLogicSettingLoaded(String name, String value) {
        // TODO
    }
}
