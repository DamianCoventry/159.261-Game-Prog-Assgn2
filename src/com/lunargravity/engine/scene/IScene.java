package com.lunargravity.engine.scene;

import com.lunargravity.engine.graphics.*;

public interface IScene {
    void Load(String fileName, ISceneBuilderObserver loadProgressObserver);
    GlTexture[] getTextures();
    GlStaticMesh[] getStaticMeshes();
}
