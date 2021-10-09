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

import com.google.gson.Gson;
import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.physics.CollisionMeshBuilder;
import com.lunargravity.mvc.IView;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SceneBuilder {
    private final ISceneBuilderObserver _observer;
    private final ISceneAssetOwner _assetOwner;
    private final IView _view;

    public SceneBuilder(ISceneBuilderObserver observer, IView view, ISceneAssetOwner assetOwner) {
        _observer = observer;
        _view = view;
        _assetOwner = assetOwner;
    }

    public void build(ViewportConfig viewportConfig, String fileName) throws Exception {
        if (fileName == null) {
            throw new IllegalArgumentException("fileName argument is null");
        }
        File file = new File(fileName);
        if (!file.exists()) {
            throw new IllegalArgumentException("There is no file system item with name [" + fileName + "]");
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException("The file system item [" + fileName + "] is not a file");
        }

        String text = Files.readString(Paths.get(fileName), StandardCharsets.UTF_8);
        if (text.isEmpty()) {
            throw new IllegalArgumentException("Scene file [" + fileName + "] is empty");
        }

        Gson gson = new Gson();
        Scene scene = gson.fromJson(text, Scene.class);
        if (scene == null) {
            throw new IllegalArgumentException("File [" + fileName + "] is an invalid scene file");
        }

        _observer.sceneBuildBeginning();

        int itemCount = 0;
        int totalItemCount = 0;

        if (scene._displayMeshFileNames != null) {
            totalItemCount += scene._displayMeshFileNames.size();
        }
        if (scene._widgets != null) {
            totalItemCount += scene._widgets.size();
        }

        DisplayMeshCache displayMeshCache = _view.getDisplayMeshCache();
        MaterialCache materialCache = _view.getMaterialCache();
        TextureCache textureCache = _view.getTextureCache();

        _observer.sceneBuildProgressed(0, totalItemCount);

        if (scene._displayMeshFileNames != null) {
            for (int i = 0; i < scene._displayMeshFileNames.size(); ++i) {
                GlStaticMeshBuilder builder = new GlStaticMeshBuilder(displayMeshCache, materialCache, textureCache);
                builder.build(scene._displayMeshFileNames.get(i)); // TODO: get load progress in here
                totalItemCount += builder.getMeshes().size();
                for (var mesh : builder.getMeshes()) {
                    _assetOwner.displayMeshLoaded(mesh);
                    _observer.sceneBuildProgressed(++itemCount, totalItemCount);
                }
            }
        }

        if (scene._collisionMeshFileNames != null) {
            for (int i = 0; i < scene._collisionMeshFileNames.size(); ++i) {
                CollisionMeshBuilder builder = new CollisionMeshBuilder();
                builder.build(scene._collisionMeshFileNames.get(i)); // TODO: get load progress in here
                totalItemCount += builder.getMeshes().size();
                for (var name : builder.getMeshes().keySet()) {
                    _assetOwner.collisionMeshLoaded(name, builder.getMeshes().get(name));
                    _observer.sceneBuildProgressed(++itemCount, totalItemCount);
                }
            }
        }

        if (scene._widgets != null) {
            for (int i = 0; i < scene._widgets.size(); ++i) {
                _assetOwner.widgetLoaded(viewportConfig, scene._widgets.get(i));
                _observer.sceneBuildProgressed(++itemCount, totalItemCount);
            }
        }

        _observer.sceneBuildProgressed(totalItemCount, totalItemCount);
        _observer.sceneBuildEnded();


//        _stateOwner.stateSettingLoaded("", "");
//        _logicOwner.logicSettingLoaded("", "");
//        _assetOwner.objectLoaded("name", "type", new Transform());
//        _assetOwner.displayMeshLoaded(new DisplayMesh("name"));
//        _assetOwner.materialLoaded(new Material("name"));
//        _assetOwner.textureLoaded(new GlTexture(BitmapImage.fromFile("fileName")));
//        _assetOwner.widgetLoaded(new WidgetCreateInfo());
    }
}
