package com.lunargravity.world.view;

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
    private final ArrayList<PlanetarySystem> _planetarySystems;
    private final ArrayList<GlStaticMesh> _staticMeshes;
    private final ArrayList<GlMaterial> _materials;
    private final ArrayList<GlTexture> _textures;
    private final ArrayList<GlObject> _cameras;
    private GlObject _currentCamera;
    private GlObject _sun;

    public MenuWorldView(IMenuWorldModel model) {
        _model = model;
        _planetarySystems = new ArrayList<>();
        _staticMeshes = new ArrayList<>();
        _materials = new ArrayList<>();
        _textures = new ArrayList<>();
        _cameras = new ArrayList<>();
    }

    @Override
    public void onViewThink() {
        // TODO
    }

    @Override
    public void onDrawView3d(int viewport, Matrix4f projectionMatrix) {
        if (_cameras.size() == 0) {
            // begin temp
            _cameras.add(new GlObject("temp", new GlTransform(new Vector3f(), new Quaternionf())));
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
    public void onDrawView2d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void drawWorldViewStuff() {
        // TODO
    }

    @Override
    public void drawMenuWorldViewStuff() {
        // TODO
    }

    @Override
    public void onObjectLoaded(String name, String type, GlTransform transform) {
        if (name.startsWith("Camera")) {
            _cameras.add(new GlObject(name, transform));
            _currentCamera = _cameras.get(0);
        }
        if (name.startsWith("Sun")) {
            _sun = new GlObject(name, transform, findStaticMesh(type));
        }
        else if (name.startsWith("Planet")) {
            GlObject planet = new GlObject(name, transform, findStaticMesh(type));
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
            planetarySystem._moons.add(new GlObject(moonName, transform, findStaticMesh(type)));
        }
    }

    @Override
    public void onStaticMeshLoaded(GlStaticMesh staticMesh) {
        staticMesh.bindMaterials(_materials);
        _staticMeshes.add(staticMesh);
    }

    @Override
    public void onMaterialLoaded(GlMaterial material) {
        material.bindTextures(_textures);
        _materials.add(material);
    }

    @Override
    public void onTextureLoaded(GlTexture texture) {
        _textures.add(texture);
    }

    @Override
    public void onWidgetLoaded(WidgetCreateInfo wci) {
        // TODO
    }

    public GlStaticMesh findStaticMesh(String name) {
        for (final var staticMesh : _staticMeshes) {
            if (name.equals(staticMesh.getName())) {
                return staticMesh;
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
