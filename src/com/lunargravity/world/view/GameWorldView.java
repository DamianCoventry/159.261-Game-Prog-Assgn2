package com.lunargravity.world.view;

import com.lunargravity.engine.graphics.GlMaterial;
import com.lunargravity.engine.graphics.GlStaticMesh;
import com.lunargravity.engine.graphics.GlTexture;
import com.lunargravity.engine.graphics.GlTransform;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.widgetsystem.WidgetCreateInfo;
import com.lunargravity.world.model.IGameWorldModel;
import org.joml.Matrix4f;

public class GameWorldView implements IGameWorldView, ISceneAssetOwner {
    private final IGameWorldModel _model;

    public GameWorldView(IGameWorldModel model) {
        _model = model;
    }

    @Override
    public void onViewThink() {
        // TODO
    }

    @Override
    public void onDrawView3d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void onDrawView2d(int viewport, Matrix4f projectionMatrix) {
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
    public void onObjectLoaded(String name, String type, GlTransform transform) {
        // TODO
    }

    @Override
    public void onStaticMeshLoaded(GlStaticMesh staticMesh) {
        // TODO
    }

    @Override
    public void onMaterialLoaded(GlMaterial material) {
        // TODO
    }

    @Override
    public void onTextureLoaded(GlTexture texture) {
        // TODO
    }

    @Override
    public void onWidgetLoaded(WidgetCreateInfo wci) {
        // TODO
    }
}
