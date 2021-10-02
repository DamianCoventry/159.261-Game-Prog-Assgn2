package com.lunargravity.engine.graphics;

import com.jme3.bullet.PhysicsSpace;

import java.util.ArrayList;

public class DisplayMeshCache {
    private final ArrayList<DisplayMesh> _displayMeshes;

    public DisplayMeshCache() {
        _displayMeshes = new ArrayList<>();
    }

    public ArrayList<DisplayMesh> getDisplayMeshes() {
        return _displayMeshes;
    }

    public DisplayMesh get(String name) {
        for (var displayMesh : _displayMeshes) {
            if (name.equals(displayMesh.getName())) {
                return displayMesh;
            }
        }
        return null;
    }

    public DisplayMesh add(DisplayMesh material) {
        DisplayMesh cachedDisplayMesh = get(material.getName());
        if (cachedDisplayMesh != null) {
            return cachedDisplayMesh;
        }
        _displayMeshes.add(material);
        return material;
    }

    public void clear() {
        _displayMeshes.clear();
    }
}
