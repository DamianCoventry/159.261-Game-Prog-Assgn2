package com.lunargravity.mvc;

import com.lunargravity.engine.scene.ISceneLogicOwner;

public interface IController extends ISceneLogicOwner {
    void onControllerThink();
}
