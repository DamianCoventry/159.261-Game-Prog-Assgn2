package com.lunargravity.mvc;

import com.lunargravity.engine.scene.ISceneAssetOwner;
import org.joml.Matrix4f;

public interface IView extends ISceneAssetOwner {
    void onViewThink();
    void onDrawView3d(int viewport, Matrix4f projectionMatrix);
    void onDrawView2d(int viewport, Matrix4f projectionMatrix);
}
