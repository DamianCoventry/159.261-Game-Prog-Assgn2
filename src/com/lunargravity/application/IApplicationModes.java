package com.lunargravity.application;

import com.lunargravity.engine.scene.ISceneLoadObserver;
import com.lunargravity.menu.controller.IMenuControllerEvents;
import com.lunargravity.world.controller.IMenuWorldControllerEvents;

public interface IApplicationModes {
    void startMenu(ISceneLoadObserver loadingProgress, IMenuWorldControllerEvents worldEventHandler, IMenuControllerEvents eventHandler);
    void startCampaignGame(ISceneLoadObserver loadingProgress);
    void startRaceGame(ISceneLoadObserver loadingProgress);
    void startDogfightGame(ISceneLoadObserver loadingProgress);
}
