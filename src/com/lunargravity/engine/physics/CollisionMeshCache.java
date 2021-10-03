package com.lunargravity.engine.physics;

import com.jme3.bullet.collision.shapes.CollisionShape;

import java.util.HashMap;

public class CollisionMeshCache {
    private final HashMap<String, CollisionShape> _collisionMeshes;

    public CollisionMeshCache() {
        _collisionMeshes = new HashMap<>();
    }

    public CollisionShape getByExactName(String name) {
        return _collisionMeshes.get(name);
    }

    public CollisionShape getByPartialName(String subString) {
        for (var collisionMeshName : _collisionMeshes.keySet()) {
            if (collisionMeshName.contains(subString)) {
                return _collisionMeshes.get(collisionMeshName);
            }
        }
        return null;
    }

    public CollisionShape add(String name, CollisionShape collisionShape) {
        CollisionShape shape = _collisionMeshes.get(name);
        if (shape != null) {
            return shape;
        }
        _collisionMeshes.put(name, collisionShape);
        return collisionShape;
    }

    public void clear() {
        _collisionMeshes.clear();
    }
}
