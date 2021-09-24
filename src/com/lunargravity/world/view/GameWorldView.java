package com.lunargravity.world.view;

import com.lunargravity.engine.graphics.GlMaterial;
import com.lunargravity.engine.graphics.GlStaticMesh;
import com.lunargravity.engine.graphics.GlTexture;
import com.lunargravity.engine.graphics.Transform;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.widgetsystem.WidgetCreateInfo;
import com.lunargravity.world.model.IGameWorldModel;
import org.joml.Matrix4f;

import java.io.IOException;

public class GameWorldView implements IGameWorldView, ISceneAssetOwner {
    private final IGameWorldModel _model;

    public GameWorldView(IGameWorldModel model) {
        _model = model;
    }

    @Override
    public void initialLoadCompleted() {
        // TODO
    }

    @Override
    public void viewThink() {
        // TODO
    }

    @Override
    public void drawView3d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void drawView2d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void drawWorldViewStuff() {
        // TODO
    }

    @Override
    public void drawGameWorldViewStuff() {
        // TODO
    }

    @Override
    public void objectLoaded(String name, String type, Transform transform) {
        // TODO
    }

    @Override
    public void staticMeshLoaded(GlStaticMesh staticMesh) {
        // TODO
    }

    @Override
    public void materialLoaded(GlMaterial material) {
        // TODO
    }

    @Override
    public void textureLoaded(GlTexture texture) {
        // TODO
    }

    @Override
    public void widgetLoaded(WidgetCreateInfo wci) throws IOException {
        // TODO
    }
}
