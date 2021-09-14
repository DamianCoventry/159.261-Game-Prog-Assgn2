package com.lunargravity.engine.scene;

import com.lunargravity.engine.graphics.*;

public interface ISceneAssetOwner {
    void onObjectLoaded(String name, String type, GlTransform transform);
    void onStaticMeshLoaded(GlStaticMesh staticMesh);
    void onMaterialLoaded(GlMaterial material);
    void onTextureLoaded(GlTexture texture);
    // TODO: handle sounds
    // TODO: handle music
    // TODO: handle sprites
    // TODO: handle particle systems
}
