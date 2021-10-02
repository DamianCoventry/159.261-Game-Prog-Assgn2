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
import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.widgetsystem.WidgetCreateInfo;
import com.lunargravity.world.model.IMenuWorldModel;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;

public class MenuWorldView implements IMenuWorldView, ISceneAssetOwner {
    static private final String PLANET_STRING_ID = ".planet.";

    private static class PlanetarySystem {
        public GlObject _planet;
        public ArrayList<GlObject> _moons;
        public PlanetarySystem(GlObject planet) {
            _planet = planet;
            _moons = new ArrayList<>();
        }
    }

    private final IMenuWorldModel _model;
    private final DisplayMeshCache _displayMeshCache;
    private final MaterialCache _materialCache;
    private final TextureCache _textureCache;

    private final ArrayList<PlanetarySystem> _planetarySystems;
    private final ArrayList<DisplayMesh> _displayMeshes;
    private final ArrayList<GlObject> _cameras;
    private GlObject _currentCamera;
    private GlObject _sun;

    public MenuWorldView(IMenuWorldModel model) {
        _model = model;
        _planetarySystems = new ArrayList<>();
        _displayMeshes = new ArrayList<>();
        _displayMeshCache = new DisplayMeshCache();
        _materialCache = new MaterialCache();
        _textureCache = new TextureCache();
        _cameras = new ArrayList<>();
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
        if (_cameras.size() == 0) {
            // begin temp
            _cameras.add(new GlObject("temp", new Transform(new Vector3f(), new Quaternionf())));
            _currentCamera = _cameras.get(0);
            // end temp
            //throw new RuntimeException("No cameras are defined");
        }

        Matrix4f viewProjectionMatrix = projectionMatrix.mul(_currentCamera._transform.getViewMatrix());
        if (_sun != null) { // Should always pass
            _sun.draw(viewProjectionMatrix);
        }

        for (final var planetarySystem : _planetarySystems) {
            planetarySystem._planet.draw(viewProjectionMatrix);
            for (final var moon : planetarySystem._moons) {
                moon.draw(viewProjectionMatrix);
            }
        }
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
    public void resetState() {
        // TODO
    }

    @Override
    public void freeResources() {
        // TODO
    }

    @Override
    public void drawMenuWorldViewStuff() {
        // TODO
    }

    @Override
    public void objectLoaded(String name, String type, Transform transform) {
        if (name.startsWith("Camera")) {
            _cameras.add(new GlObject(name, transform));
            _currentCamera = _cameras.get(0);
        }
        if (name.startsWith("Sun")) {
            _sun = new GlObject(name, transform, findDisplayMesh(type));
        }
        else if (name.startsWith("Planet")) {
            GlObject planet = new GlObject(name, transform, findDisplayMesh(type));
            _planetarySystems.add(new PlanetarySystem(planet));
        }
        else if (name.startsWith("Moon")) {
            int i = name.indexOf(PLANET_STRING_ID);
            if (i < 0) {
                throw new RuntimeException("The moon [" + name + "] is not associated with a planet");
            }
            String planetName = name.substring(i + PLANET_STRING_ID.length(), name.length() - 1);
            String moonName = name.substring(0, i - 1);
            PlanetarySystem planetarySystem = findPlanetarySystem(planetName);
            planetarySystem._moons.add(new GlObject(moonName, transform, findDisplayMesh(type)));
        }
    }

    @Override
    public void displayMeshLoaded(DisplayMesh displayMesh) {
        _displayMeshes.add(displayMesh);
    }

    @Override
    public void collisionMeshLoaded(String name, CollisionShape collisionMesh) {
        // TODO
    }

    @Override
    public void materialLoaded(Material material) {
        _materialCache.add(material);
    }

    @Override
    public void textureLoaded(GlTexture texture) {
        _textureCache.add(texture);
    }

    @Override
    public void widgetLoaded(ViewportConfig viewportConfig, WidgetCreateInfo wci) {
        if (wci == null) {
            System.out.print("MenuWorldView.widgetLoaded() was passed a null WidgetCreateInfo object");
            return;
        }
        // TODO
    }

    public DisplayMesh findDisplayMesh(String name) {
        for (final var displayMesh : _displayMeshes) {
            if (name.equals(displayMesh.getName())) {
                return displayMesh;
            }
        }
        throw new RuntimeException("There is no mesh named [" + name + "]");
    }

    public PlanetarySystem findPlanetarySystem(String name) {
        for (final var planetarySystem : _planetarySystems) {
            if (name.equals(planetarySystem._planet._name)) {
                return planetarySystem;
            }
        }
        throw new RuntimeException("There is no planetary system named [" + name + "]");
    }
}
