package com.lunargravity.mvc;

import com.lunargravity.engine.scene.ISceneAssetOwner;
import org.joml.Matrix4f;

public interface IView extends ISceneAssetOwner {
    void think();
    void draw3d(int viewport, Matrix4f projectionMatrix);
    void draw2d(int viewport, Matrix4f projectionMatrix);
}
