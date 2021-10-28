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

package com.lunargravity.world.view;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.lunargravity.engine.animation.FloatLinearInterpLoop;
import com.lunargravity.engine.core.IEngine;
import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.widgetsystem.WidgetCreateInfo;
import com.lunargravity.engine.widgetsystem.WidgetManager;
import com.lunargravity.mvc.IView;
import com.lunargravity.world.model.IMenuWorldModel;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class MenuWorldView implements IView, ISceneAssetOwner {
    private static final long MOON_ROTATION_TIME = 360000; // 360 seconds to rotate 360°
    private static final float CAMERA_HEIGHT = 3.0f;
    private static final float CAMERA_DISTANCE = 15.0f;
    private static final Vector3f MOON_AMBIENT_LIGHT = new Vector3f(0.05f, 0.05f, 0.05f);
    private static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);

    private final DisplayMeshCache _displayMeshCache;
    private final MaterialCache _materialCache;
    private final TextureCache _textureCache;
    private final Renderer _renderer;
    private final WidgetManager _widgetManager;
    private final IMenuWorldModel _model;
    private DisplayMesh _moonDisplayMesh;
    private DisplayMesh _spaceDisplayMesh;
    private final Matrix4f _vpMatrix;
    private final Matrix4f _mvMatrix;
    private final Matrix4f _moonModelMatrix;
    private final Transform _camera;
    private final FloatLinearInterpLoop _moonRotation;

    public MenuWorldView(WidgetManager widgetManager, IEngine engine, IMenuWorldModel model) {
        _renderer = engine.getRenderer();
        _widgetManager = widgetManager;
        _model = model;
        _displayMeshCache = new DisplayMeshCache();
        _materialCache = new MaterialCache();
        _textureCache = new TextureCache();
        _mvMatrix = new Matrix4f();
        _vpMatrix = new Matrix4f();
        _moonModelMatrix = new Matrix4f();

        _camera = new Transform();
        _camera.calculateViewMatrix();

        _moonRotation = new FloatLinearInterpLoop(engine.getAnimationManager());
    }

    @Override
    public void initialLoadCompleted() {
        _moonRotation.start(0.0f, 359.0f, MOON_ROTATION_TIME);
    }

    @Override
    public void viewThink() {
        // Nothing to do
    }

    @Override
    public void drawView3d(int viewport, Matrix4f projectionMatrix) {
        if (viewport < 0 || viewport > 1) {
            return;
        }

        var angle = (float)Math.toRadians(_moonRotation.getCurrentValue());
        _camera._position.x = (float)Math.sin(angle) * CAMERA_DISTANCE;
        _camera._position.y = CAMERA_HEIGHT;
        _camera._position.z = (float)Math.cos(angle) * CAMERA_DISTANCE;
        _camera._viewMatrix.setLookAt(_camera._position, new Vector3f(), Y_AXIS);

        _vpMatrix.set(projectionMatrix).mul(_camera.getViewMatrix());
        _spaceDisplayMesh.draw(_renderer, _renderer.getDiffuseTextureProgram(), _vpMatrix);

        _moonModelMatrix.identity();
        _mvMatrix.set(_camera.getViewMatrix()).mul(_moonModelMatrix);
        GLDirectionalLightProgram program = _renderer.getDirectionalLightProgram();
        program.setAmbientLight(MOON_AMBIENT_LIGHT);
        _moonDisplayMesh.draw(_renderer, program, _mvMatrix, projectionMatrix);
    }

    @Override
    public void drawView2d(Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public DisplayMeshCache getDisplayMeshCache() {
        return _displayMeshCache;
    }

    @Override
    public MaterialCache getMaterialCache() {
        return _materialCache;
    }

    @Override
    public TextureCache getTextureCache() {
        return _textureCache;
    }

    @Override
    public void onFrameEnd() {
        // TODO
    }

    @Override
    public void setupForNewLevel() {
        // Nothing to do
    }

    @Override
    public void freeNativeResources() {
        if (_displayMeshCache != null) {
            _displayMeshCache.freeNativeResources();
        }
        if (_materialCache != null) {
            _materialCache.clear();
        }
        if (_textureCache != null) {
            _textureCache.freeNativeResources();
        }
        if (_moonDisplayMesh != null) {
            _moonDisplayMesh.freeNativeResources();
        }
        if (_spaceDisplayMesh != null) {
            _spaceDisplayMesh.freeNativeResources();
        }
        if (_moonRotation != null) {
            _moonRotation.unregister();
        }
    }

    @Override
    public void displayMeshLoaded(DisplayMesh displayMesh) {
        if (displayMesh.getName().equals("EarthMoon.Display")) {
            _moonDisplayMesh = displayMesh;
        }
        _displayMeshCache.add(displayMesh);

        if (displayMesh.getName().equals("OuterSpace.Display")) {
            _spaceDisplayMesh = displayMesh;
        }
        _displayMeshCache.add(displayMesh);
    }

    @Override
    public void collisionMeshLoaded(String name, CollisionShape collisionMesh) {
        // TODO
    }

    @Override
    public void widgetLoaded(ViewportConfig viewportConfig, WidgetCreateInfo wci) {
        // TODO
    }
}
