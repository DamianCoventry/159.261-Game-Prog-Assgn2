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

package com.lunargravity.engine.scene;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.widgetsystem.WidgetCreateInfo;

import java.io.IOException;

public interface ISceneAssetOwner {
    void objectLoaded(String name, String type, Transform transform);
    void displayMeshLoaded(DisplayMesh displayMesh);
    void collisionMeshLoaded(String name, CollisionShape collisionMesh);
    void materialLoaded(Material material);
    void textureLoaded(GlTexture texture);
    void widgetLoaded(ViewportConfig viewportConfig, WidgetCreateInfo wci) throws IOException;
    // TODO: handle sounds
    // TODO: handle music
    // TODO: handle sprites
    // TODO: handle particle systems
}
