package com.lunargravity.engine.graphics;

import java.io.IOException;
import java.util.ArrayList;

public class GlStaticMeshBuilder {
    private final DisplayMeshCache _displayMeshCache;
    private final MaterialCache _materialCache;
    private final TextureCache _textureCache;
    private final ArrayList<DisplayMesh> _meshes;

    public GlStaticMeshBuilder(DisplayMeshCache displayMeshCache, MaterialCache materialCache, TextureCache textureCache) {
        _displayMeshCache = displayMeshCache;
        _materialCache = materialCache;
        _textureCache = textureCache;
        _meshes = new ArrayList<>();
    }

    public ArrayList<DisplayMesh> getMeshes() {
        return _meshes;
    }

    public void build(String fileName) throws Exception {
        final DisplayMeshFileLoader loader = new DisplayMeshFileLoader(fileName);
        _meshes.clear();
        for (var object : loader.getObjects()) {
            if (object.getPieces().isEmpty()) {
                throw new RuntimeException("Object [" + object.getName() + "] has no pieces");
            }

            loadMaterials(loader.getMaterialFileNames());

            if (_displayMeshCache.get(object.getName()) == null) {
                buildStaticMesh(loader, object);
            }
        }
    }

    private void buildStaticMesh(DisplayMeshFileLoader loader, DisplayMeshFileLoader.Object object) {
        DisplayMesh displayMesh = new DisplayMesh(object.getName());
        for (var piece : object.getPieces()) {
            float[] vertices = loadVertices(loader, piece);
            float[] texCoordinates = loadTexCoordinates(loader, piece);
            float[] normals = loadNormals(loader, piece);
            displayMesh.addPiece(new GlStaticMeshPiece(piece.getMaterialName(), new PolyhedraVxTcNm(vertices, texCoordinates, normals)));
        }
        displayMesh.bindMaterials(_materialCache, _textureCache);
        _meshes.add(displayMesh);
    }

    private void loadMaterials(ArrayList<String> materialFileNames) throws Exception {
        for (var fileName : materialFileNames) {
            MaterialFileLoader loader = new MaterialFileLoader("meshes/" + fileName);
            for (var material : loader.getMaterials()) {
                loadMaterial(material);
            }
        }
    }

    private void loadMaterial(Material material) throws IOException {
        Material existingMaterial = _materialCache.get(material.getName());
        if (existingMaterial == null) {
            _materialCache.add(material);
            loadTextures(material);
        }
    }

    private void loadTextures(Material material) throws IOException {
        if (material.getAmbientTextureFileName() != null) {
            _textureCache.add(new GlTexture(BitmapImage.fromFile(material.getAmbientTextureFileName())));
        }
        if (material.getDiffuseTextureFileName() != null) {
            _textureCache.add(new GlTexture(BitmapImage.fromFile(material.getDiffuseTextureFileName())));
        }
        if (material.getSpecularTextureFileName() != null) {
            _textureCache.add(new GlTexture(BitmapImage.fromFile(material.getSpecularTextureFileName())));
        }
        if (material.getEmissiveTextureFileName() != null) {
            _textureCache.add(new GlTexture(BitmapImage.fromFile(material.getEmissiveTextureFileName())));
        }
        if (material.getSpecularExponentTextureFileName() != null) {
            _textureCache.add(new GlTexture(BitmapImage.fromFile(material.getSpecularExponentTextureFileName())));
        }
        if (material.getIndexOfRefractionTextureFileName() != null) {
            _textureCache.add(new GlTexture(BitmapImage.fromFile(material.getIndexOfRefractionTextureFileName())));
        }
        if (material.getDissolvedTextureFileName() != null) {
            _textureCache.add(new GlTexture(BitmapImage.fromFile(material.getDissolvedTextureFileName())));
        }
        if (material.getTransparencyTextureFileName() != null) {
            _textureCache.add(new GlTexture(BitmapImage.fromFile(material.getTransparencyTextureFileName())));
        }
        if (material.getTransmissionFilterTextureFileName() != null) {
            _textureCache.add(new GlTexture(BitmapImage.fromFile(material.getTransmissionFilterTextureFileName())));
        }
    }

    private float[] loadVertices(DisplayMeshFileLoader loader, DisplayMeshFileLoader.Piece piece) {
        int numFloats = piece.getFaces().size() * 3 * 3;
        int floatCount = 0;

        float[] vertices = new float[numFloats];
        for (int faceIndex = 0; faceIndex < piece.getFaces().size(); ++faceIndex) {
            DisplayMeshFileLoader.Face face = piece.getFaces().get(faceIndex);

            vertices[floatCount++] = loader.getVertices().get(face._vertices[0]).x;
            vertices[floatCount++] = loader.getVertices().get(face._vertices[0]).y;
            vertices[floatCount++] = loader.getVertices().get(face._vertices[0]).z;

            vertices[floatCount++] = loader.getVertices().get(face._vertices[1]).x;
            vertices[floatCount++] = loader.getVertices().get(face._vertices[1]).y;
            vertices[floatCount++] = loader.getVertices().get(face._vertices[1]).z;

            vertices[floatCount++] = loader.getVertices().get(face._vertices[2]).x;
            vertices[floatCount++] = loader.getVertices().get(face._vertices[2]).y;
            vertices[floatCount++] = loader.getVertices().get(face._vertices[2]).z;
        }

        return vertices;
    }

    private float[] loadTexCoordinates(DisplayMeshFileLoader loader, DisplayMeshFileLoader.Piece piece) {
        int numFloats = piece.getFaces().size() * 3 * 2;
        int floatCount = 0;

        float[] texCoordinates = new float[numFloats];
        for (int faceIndex = 0; faceIndex < piece.getFaces().size(); ++faceIndex) {
            DisplayMeshFileLoader.Face face = piece.getFaces().get(faceIndex);

            texCoordinates[floatCount++] = loader.getTexCoordinates().get(face._texCoordinates[0]).x;
            texCoordinates[floatCount++] = 1-loader.getTexCoordinates().get(face._texCoordinates[0]).y;

            texCoordinates[floatCount++] = loader.getTexCoordinates().get(face._texCoordinates[1]).x;
            texCoordinates[floatCount++] = 1-loader.getTexCoordinates().get(face._texCoordinates[1]).y;

            texCoordinates[floatCount++] = loader.getTexCoordinates().get(face._texCoordinates[2]).x;
            texCoordinates[floatCount++] = 1-loader.getTexCoordinates().get(face._texCoordinates[2]).y;
        }

        return texCoordinates;
    }

    private float[] loadNormals(DisplayMeshFileLoader loader, DisplayMeshFileLoader.Piece piece) {
        int numFloats = piece.getFaces().size() * 3 * 3;
        int floatCount = 0;

        float[] normals = new float[numFloats];
        for (int faceIndex = 0; faceIndex < piece.getFaces().size(); ++faceIndex) {
            DisplayMeshFileLoader.Face face = piece.getFaces().get(faceIndex);

            normals[floatCount++] = loader.getNormals().get(face._normals[0]).x;
            normals[floatCount++] = loader.getNormals().get(face._normals[0]).y;
            normals[floatCount++] = loader.getNormals().get(face._normals[0]).z;

            normals[floatCount++] = loader.getNormals().get(face._normals[1]).x;
            normals[floatCount++] = loader.getNormals().get(face._normals[1]).y;
            normals[floatCount++] = loader.getNormals().get(face._normals[1]).z;

            normals[floatCount++] = loader.getNormals().get(face._normals[2]).x;
            normals[floatCount++] = loader.getNormals().get(face._normals[2]).y;
            normals[floatCount++] = loader.getNormals().get(face._normals[2]).z;
        }

        return normals;
    }
}
