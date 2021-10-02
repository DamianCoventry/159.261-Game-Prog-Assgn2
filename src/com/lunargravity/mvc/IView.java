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

package com.lunargravity.mvc;

import com.lunargravity.engine.graphics.MaterialCache;
import com.lunargravity.engine.graphics.DisplayMeshCache;
import com.lunargravity.engine.graphics.TextureCache;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import org.joml.Matrix4f;

public interface IView extends ISceneAssetOwner {
    void initialLoadCompleted();
    void viewThink();
    void drawView3d(int viewport, Matrix4f projectionMatrix);
    void drawView2d(Matrix4f projectionMatrix);
    DisplayMeshCache getDisplayMeshCache();
    MaterialCache getMaterialCache();
    TextureCache getTextureCache();
    void onFrameEnd();
    void resetState();
}
