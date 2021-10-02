package com.lunargravity.engine.graphics;

import java.util.ArrayList;

public class MaterialCache {
    private final ArrayList<Material> _materials;

    public MaterialCache() {
        _materials = new ArrayList<>();
    }

    public Material get(String name) {
        for (var material : _materials) {
            if (name.equals(material.getName())) {
                return material;
            }
        }
        return null;
    }

    public Material add(Material material) {
        Material cachedMaterial = get(material.getName());
        if (cachedMaterial != null) {
            return cachedMaterial;
        }
        _materials.add(material);
        return material;
    }

    public void clear() {
        _materials.clear();
    }
}
