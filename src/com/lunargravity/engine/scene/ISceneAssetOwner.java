package com.lunargravity.engine.scene;

import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.widgetsystem.WidgetCreateInfo;

import java.io.IOException;

public interface ISceneAssetOwner {
    void objectLoaded(String name, String type, Transform transform);
    void staticMeshLoaded(GlStaticMesh staticMesh);
    void materialLoaded(GlMaterial material);
    void textureLoaded(GlTexture texture);
    void widgetLoaded(ViewportConfig viewportConfig, WidgetCreateInfo wci) throws IOException;
    // TODO: handle sounds
    // TODO: handle music
    // TODO: handle sprites
    // TODO: handle particle systems
}
