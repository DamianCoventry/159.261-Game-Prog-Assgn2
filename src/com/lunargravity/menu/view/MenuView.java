package com.lunargravity.menu.view;

import com.lunargravity.engine.graphics.GlMaterial;
import com.lunargravity.engine.graphics.GlStaticMesh;
import com.lunargravity.engine.graphics.GlTexture;
import com.lunargravity.engine.graphics.GlTransform;
import com.lunargravity.menu.model.IMenuModel;
import org.joml.Matrix4f;

public class MenuView implements IMenuView {
    private final IMenuModel _model;

    public MenuView(IMenuModel model) {
        _model = model;
    }

    @Override
    public void think() {
        // TODO
    }

    @Override
    public void draw3d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void draw2d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void temp() {
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
}
