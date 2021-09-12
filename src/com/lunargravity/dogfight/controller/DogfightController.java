package com.lunargravity.dogfight.controller;

import com.lunargravity.dogfight.model.IDogfightModel;

public class DogfightController implements IDogfightController {
    private final IDogfightControllerEvents _eventHandler;
    private final IDogfightModel _model;

    public DogfightController(IDogfightControllerEvents eventHandler, IDogfightModel model) {
        _eventHandler = eventHandler;
        _model = model;
    }

    @Override
    public void think() {

    }

    @Override
    public void temp() {

    }
}
