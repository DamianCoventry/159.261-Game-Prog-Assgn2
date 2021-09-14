package com.lunargravity.engine.scene;

import com.lunargravity.engine.graphics.*;

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

    public void build(String fileName) {
        _observer.onSceneBuildBeginning();
        _observer.onSceneBuildProgressed(0, 100); // TODO

        _stateOwner.onStateSettingLoaded("blah", "blah");
        _logicOwner.onLogicSettingLoaded("blah", "blah");
        _observer.onSceneBuildProgressed(33, 100); // TODO

        _assetOwner.onTextureLoaded(new GlTexture("blah"));
        _assetOwner.onStaticMeshLoaded(new GlStaticMesh("blah"));
        _observer.onSceneBuildProgressed(66, 100); // TODO

        _assetOwner.onObjectLoaded("blah", "blah", new GlTransform());
        _assetOwner.onMaterialLoaded(new GlMaterial("blah"));
        _observer.onSceneBuildProgressed(100, 100); // TODO

        _observer.onSceneBuildEnded();
    }
}
