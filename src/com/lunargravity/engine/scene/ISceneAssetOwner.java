package com.lunargravity.engine.scene;

import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.widgetsystem.WidgetCreateInfo;

import java.io.IOException;

public interface ISceneAssetOwner {
    void onObjectLoaded(String name, String type, Transform transform);
    void onStaticMeshLoaded(GlStaticMesh staticMesh);
    void onMaterialLoaded(GlMaterial material);
    void onTextureLoaded(GlTexture texture);
    void onWidgetLoaded(WidgetCreateInfo wci) throws IOException;
    // TODO: handle sounds
    // TODO: handle music
    // TODO: handle sprites
    // TODO: handle particle systems
}
