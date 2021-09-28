//
// Lunar Gravity
//
// This game is based upon the Amiga video game Gravity Force that was
// released in 1989 by Stephan Wenzler
//
// https://www.mobygames.com/game/gravity-force
// https://www.youtube.com/watch?v=m9mFtCvnko8
//
// This implementation is Copyright (c) 2021, Damian Coventry
// All rights reserved
// Written for Massey University course 159.261 Game Programming (Assignment 2)
//

package com.lunargravity.world.controller;

import com.lunargravity.engine.scene.ISceneLogicOwner;
import com.lunargravity.world.model.IGameWorldModel;

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
