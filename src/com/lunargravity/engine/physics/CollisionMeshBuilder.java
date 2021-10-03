package com.lunargravity.engine.physics;

import com.jme3.bullet.collision.shapes.*;
import com.jme3.bullet.collision.shapes.infos.IndexedMesh;
import com.jme3.math.Vector3f;
import com.lunargravity.engine.graphics.DisplayMeshFileLoader;

import java.util.HashMap;

public class CollisionMeshBuilder {
    private final HashMap<String, CollisionShape> _meshCollisionShapes;
    private CompoundCollisionShape _compoundCollisionShape;
    private String _compoundShapeName;

    public CollisionMeshBuilder() {
        _meshCollisionShapes = new HashMap<>();
    }

    public HashMap<String, CollisionShape> getMeshes() {
        return _meshCollisionShapes;
    }

    public void build(String fileName) throws Exception {
        final DisplayMeshFileLoader loader = new DisplayMeshFileLoader(fileName);
        if (loader.getObjects().isEmpty()) {
            throw new RuntimeException("Object file has no objects");
        }

        var vertices = new Vector3f[loader.getVertices().size()];
        for (int i = 0; i < loader.getVertices().size(); ++i) {
            org.joml.Vector3f vertex = loader.getVertices().get(i);
            vertices[i] = new Vector3f(vertex.x, vertex.y, vertex.z);
        }

        _meshCollisionShapes.clear();
        for (var object : loader.getObjects()) {
            if (object.getName().contains(".MeshShape")) {
                buildMeshShape(loader, vertices, object);
            }
            else if (object.getName().contains(".BoxShape")) {
                buildBoxShape(loader, vertices, object);
            }
            else if (object.getName().contains(".SphereShape")) {
                buildSphereShape(loader, vertices, object);
            }
            else if (object.getName().contains(".CompoundBox")) {
                addCompoundBoxShape(loader, vertices, object);
            }
            else if (object.getName().contains(".CompoundSphere")) {
                addCompoundSphereShape(loader, vertices, object);
            }
        }

        if (_compoundShapeName != null && _compoundCollisionShape != null) {
            _meshCollisionShapes.put(_compoundShapeName, _compoundCollisionShape);
        }
    }

    private void buildMeshShape(DisplayMeshFileLoader loader, Vector3f[] vertices, DisplayMeshFileLoader.Object object) {
        IndexedMesh indexedMesh = buildIndexedMesh(loader, vertices, object);
        _meshCollisionShapes.put(object.getName(), new MeshCollisionShape(true, indexedMesh));
    }

    private void addCompoundBoxShape(DisplayMeshFileLoader loader, Vector3f[] vertices, DisplayMeshFileLoader.Object object) {
        if (_compoundCollisionShape == null) {
            _compoundCollisionShape = new CompoundCollisionShape();
        }

        Vector3f minimum = new Vector3f();
        Vector3f maximum = new Vector3f();
        calculateAabb(loader, vertices, object, minimum, maximum);
        Vector3f halfExtent = new Vector3f(
                Math.abs(maximum.x - minimum.x) / 2.0f,
                Math.abs(maximum.y - minimum.y) / 2.0f,
                Math.abs(maximum.z - minimum.z) / 2.0f);

        Vector3f midPoint = calculateMidPoint(loader, vertices, object);

        _compoundCollisionShape.addChildShape(new BoxCollisionShape(halfExtent.x, halfExtent.y, halfExtent.z), midPoint);

        _compoundShapeName = createName(object.getName());
    }

    private void addCompoundSphereShape(DisplayMeshFileLoader loader, Vector3f[] vertices, DisplayMeshFileLoader.Object object) {
        if (_compoundCollisionShape == null) {
            _compoundCollisionShape = new CompoundCollisionShape();
        }

        Vector3f minimum = new Vector3f();
        Vector3f maximum = new Vector3f();
        calculateAabb(loader, vertices, object, minimum, maximum);
        float radius = Math.abs(maximum.x - minimum.x) / 2.0f;

        Vector3f midPoint = calculateMidPoint(loader, vertices, object);

        _compoundCollisionShape.addChildShape(new SphereCollisionShape(radius), midPoint);

        _compoundShapeName = createName(object.getName());
    }

    private String createName(String name) {
        int i = name.indexOf(".Compound");
        if (i < 0) {
            return name;
        }
        return name.substring(0, i);
    }

    private void buildBoxShape(DisplayMeshFileLoader loader, Vector3f[] vertices, DisplayMeshFileLoader.Object object) {
        Vector3f minimum = new Vector3f();
        Vector3f maximum = new Vector3f();
        calculateAabb(loader, vertices, object, minimum, maximum);

        Vector3f halfExtent = new Vector3f(
            Math.abs(maximum.x - minimum.x) / 2.0f,
            Math.abs(maximum.y - minimum.y) / 2.0f,
            Math.abs(maximum.z - minimum.z) / 2.0f);

        BoxCollisionShape bcs = new BoxCollisionShape(halfExtent.x, halfExtent.y, halfExtent.z);
        _meshCollisionShapes.put(object.getName(), bcs);
    }

    private void buildSphereShape(DisplayMeshFileLoader loader, Vector3f[] vertices, DisplayMeshFileLoader.Object object) {
        int totalVertices = 0;
        float averageLength = 0.0f;
        for (var piece : object.getPieces()) {
            totalVertices += piece.getFaces().size() * 3;
            for (var face : piece.getFaces()) {
                averageLength += vertices[face._vertices[0]].length();
                averageLength += vertices[face._vertices[1]].length();
                averageLength += vertices[face._vertices[2]].length();
            }
        }

        if (totalVertices > 0) {
            SphereCollisionShape scs = new SphereCollisionShape(averageLength / totalVertices);
            _meshCollisionShapes.put(object.getName(), scs);
        }
    }

    private IndexedMesh buildIndexedMesh(DisplayMeshFileLoader loader, Vector3f[] vertices, DisplayMeshFileLoader.Object object) {
        int totalVertices = 0;
        for (var piece : object.getPieces()) {
            totalVertices += piece.getFaces().size() * 3;
        }

        final int[] indices = new int[totalVertices];
        int count = 0;
        for (var piece : object.getPieces()) {
            for (var face : piece.getFaces()) {
                indices[count++] = face._vertices[0];
                indices[count++] = face._vertices[1];
                indices[count++] = face._vertices[2];
            }
        }

        return new IndexedMesh(vertices, indices);
    }
    
    private void calculateAabb(DisplayMeshFileLoader loader, Vector3f[] vertices, DisplayMeshFileLoader.Object object, Vector3f minimum, Vector3f maximum) {
        maximum.x = maximum.y = maximum.z = -Float.MAX_VALUE;
        minimum.x = minimum.y = minimum.z = Float.MAX_VALUE;

        for (var piece : object.getPieces()) {
            for (var face : piece.getFaces()) {
                maximum.x = Math.max(vertices[face._vertices[0]].x, maximum.x);
                maximum.x = Math.max(vertices[face._vertices[1]].x, maximum.x);
                maximum.x = Math.max(vertices[face._vertices[2]].x, maximum.x);

                maximum.y = Math.max(vertices[face._vertices[0]].y, maximum.y);
                maximum.y = Math.max(vertices[face._vertices[1]].y, maximum.y);
                maximum.y = Math.max(vertices[face._vertices[2]].y, maximum.y);

                maximum.z = Math.max(vertices[face._vertices[0]].z, maximum.z);
                maximum.z = Math.max(vertices[face._vertices[1]].z, maximum.z);
                maximum.z = Math.max(vertices[face._vertices[2]].z, maximum.z);

                minimum.x = Math.min(vertices[face._vertices[0]].x, minimum.x);
                minimum.x = Math.min(vertices[face._vertices[1]].x, minimum.x);
                minimum.x = Math.min(vertices[face._vertices[2]].x, minimum.x);

                minimum.y = Math.min(vertices[face._vertices[0]].y, minimum.y);
                minimum.y = Math.min(vertices[face._vertices[1]].y, minimum.y);
                minimum.y = Math.min(vertices[face._vertices[2]].y, minimum.y);

                minimum.z = Math.min(vertices[face._vertices[0]].z, minimum.z);
                minimum.z = Math.min(vertices[face._vertices[1]].z, minimum.z);
                minimum.z = Math.min(vertices[face._vertices[2]].z, minimum.z);
            }
        }
    }

    private Vector3f calculateMidPoint(DisplayMeshFileLoader loader, Vector3f[] vertices, DisplayMeshFileLoader.Object object) {
        Vector3f midPoint = new Vector3f();
        int totalVertices = 0;
        for (var piece : object.getPieces()) {
            for (var face : piece.getFaces()) {
                midPoint.addLocal(vertices[face._vertices[0]]);
                midPoint.addLocal(vertices[face._vertices[1]]);
                midPoint.addLocal(vertices[face._vertices[2]]);
                totalVertices += 3;
            }
        }
        return midPoint.divideLocal(totalVertices);
    }
}
