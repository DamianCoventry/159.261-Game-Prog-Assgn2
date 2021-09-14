package com.lunargravity.engine.scene;

import com.lunargravity.engine.graphics.GlStaticMesh;
import com.lunargravity.engine.graphics.GlTexture;

public class Scene implements IScene {
    @Override
    public void Load(String fileName, ISceneBuilderObserver loadProgressObserver) {
        // TODO
    }

    @Override
    public GlTexture[] getTextures() {
        // TODO
        return new GlTexture[0];
    }

    @Override
    public GlStaticMesh[] getStaticMeshes() {
        // TODO
        return new GlStaticMesh[0];
    }
}
