package com.lunargravity.engine.scene;

public interface ISceneLoadObserver {
    void sceneLoadProgressed(int currentItem, int totalItems);
}
