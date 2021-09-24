package com.lunargravity.engine.scene;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        if (fileName == null) {
            throw new IllegalArgumentException("fileName not supplied");
        }

        String text = Files.readString(Paths.get(fileName), StandardCharsets.US_ASCII);
        if (text.isEmpty()) {
            throw new IllegalArgumentException("Scene file [" + fileName + "] is empty");
        }

        Gson gson = new Gson();
        Scene scene = gson.fromJson(text, Scene.class);
        if (scene == null) {
            throw new IllegalArgumentException("File [" + fileName + "] is an invalid scene file");
        }

        if (scene._widgets != null) {
            _observer.sceneBuildProgressed(0, scene._widgets.size());

            for (int i = 0; i < scene._widgets.size(); ++i) {
                _assetOwner.widgetLoaded(scene._widgets.get(i));
                _observer.sceneBuildProgressed(i, scene._widgets.size());
            }

            _observer.sceneBuildProgressed(scene._widgets.size(), scene._widgets.size());
        }

        _observer.sceneBuildEnded();


//        _stateOwner.stateSettingLoaded("", "");
//        _logicOwner.logicSettingLoaded("", "");
//        _assetOwner.objectLoaded("name", "type", new Transform());
//        _assetOwner.staticMeshLoaded(new GlStaticMesh("name"));
//        _assetOwner.materialLoaded(new GlMaterial("name"));
//        _assetOwner.textureLoaded(new GlTexture(BitmapImage.fromFile("fileName")));
//        _assetOwner.widgetLoaded(new WidgetCreateInfo());
    }
}
