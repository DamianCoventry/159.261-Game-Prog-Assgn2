package com.lunargravity.mvc;

import com.lunargravity.engine.scene.ISceneAssetOwner;
import org.joml.Matrix4f;

public interface IView extends ISceneAssetOwner {
    void initialLoadCompleted();
    void viewThink();
    void drawView3d(int viewport, Matrix4f projectionMatrix);
    void drawView2d(int viewport, Matrix4f projectionMatrix);
}
