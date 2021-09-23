package com.lunargravity.engine.scene;

import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.widgetsystem.WidgetCreateInfo;

import java.io.IOException;

public class SceneBuilder {
    private final ISceneBuilderObserver _observer;
    private final ISceneStateOwner _stateOwner; // model
    private final ISceneAssetOwner _assetOwner; // view
    private final ISceneLogicOwner _logicOwner; // controller

    public SceneBuilder(ISceneBuilderObserver observer, ISceneStateOwner stateOwner, ISceneAssetOwner assetOwner, ISceneLogicOwner logicOwner) {
        _observer = observer;
        _stateOwner = stateOwner;
        _assetOwner = assetOwner;
        _logicOwner = logicOwner;
    }

    public void build(String fileName) throws IOException, InterruptedException {
        _observer.sceneBuildBeginning();
        _observer.sceneBuildProgressed(0, 100); // TODO
        Thread.sleep(500); // Temp

        _stateOwner.onStateSettingLoaded("blah", "blah");
        _logicOwner.onLogicSettingLoaded("blah", "blah");
        _observer.sceneBuildProgressed(33, 100); // TODO
        Thread.sleep(500); // Temp

        //_assetOwner.onTextureLoaded(new GlTexture("blah"));
        _assetOwner.onStaticMeshLoaded(new GlStaticMesh("blah"));
        _assetOwner.onWidgetLoaded(new WidgetCreateInfo("id", "type"));
        _observer.sceneBuildProgressed(66, 100); // TODO
        Thread.sleep(500); // Temp

        _assetOwner.onObjectLoaded("blah", "blah", new Transform());
        _assetOwner.onMaterialLoaded(new GlMaterial("blah"));
        _assetOwner.onWidgetLoaded(new WidgetCreateInfo("id", "type"));
        _observer.sceneBuildProgressed(100, 100); // TODO
        Thread.sleep(500); // Temp

        _observer.sceneBuildEnded();
    }
}
