package com.lunargravity.engine.graphics;

import java.util.ArrayList;

public class DisplayMeshCache {
    private final ArrayList<DisplayMesh> _displayMeshes;

    public DisplayMeshCache() {
        _displayMeshes = new ArrayList<>();
    }

    public ArrayList<DisplayMesh> getDisplayMeshes() {
        return _displayMeshes;
    }

    public DisplayMesh getByExactName(String name) {
        for (var displayMesh : _displayMeshes) {
            if (name.equals(displayMesh.getName())) {
                return displayMesh;
            }
        }
        return null;
    }

    public DisplayMesh getByPartialName(String subString) {
        for (var displayMesh : _displayMeshes) {
            if (displayMesh.getName().contains(subString)) {
                return displayMesh;
            }
        }
        return null;
    }

    public DisplayMesh add(DisplayMesh material) {
        DisplayMesh cachedDisplayMesh = getByExactName(material.getName());
        if (cachedDisplayMesh != null) {
            return cachedDisplayMesh;
        }
        _displayMeshes.add(material);
        return material;
    }

    public void freeResources() {
        for (var mesh : _displayMeshes) {
            mesh.freeResources();
        }
        _displayMeshes.clear();
    }
}
