package com.lunargravity.menu.controller;

import com.lunargravity.engine.scene.ISceneLogicOwner;
import com.lunargravity.menu.model.IMenuModel;

public class MenuController implements IMenuController, ISceneLogicOwner {
    private final IMenuControllerEvents _eventHandler;
    private final IMenuModel _model;

    public MenuController(IMenuControllerEvents eventHandler, IMenuModel model) {
        _eventHandler = eventHandler;
        _model = model;
    }

    @Override
    public void temp() {
        // TODO
    }

    @Override
    public void think() {
        // TODO
    }

    @Override
    public void onLogicSettingLoaded(String name, String value) {
        // TODO
    }
}
