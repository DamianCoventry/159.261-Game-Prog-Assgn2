package com.lunargravity.engine.graphics;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class DisplayMeshFileLoader {
    private final ArrayList<Object> _objects;
    private final ArrayList<String> _materialFileNames;
    private final ArrayList<Vector3f> _vertices;
    private final ArrayList<Vector2f> _texCoordinates;
    private final ArrayList<Vector3f> _normals;

    public static class Piece {
        private final String _materialName;
        private final ArrayList<Face> _faces;
        public Piece(String materialName) {
            _materialName = materialName;
            _faces = new ArrayList<>();
        }
        public String getMaterialName() {
            return _materialName;
        }
        public ArrayList<Face> getFaces() {
            return _faces;
        }
        public void addFace(Face face) {
            _faces.add(face);
        }
    }

    public static class Object {
        private final String _name;
        private final ArrayList<Piece> _pieces;
        private int _smoothingGroup;
        public Object(String name) {
            _name = name;
            _pieces = new ArrayList<>();
        }
        public String getName() {
            return _name;
        }
        public ArrayList<Piece> getPieces() {
            return _pieces;
        }
        public void addPiece(Piece piece) {
            _pieces.add(piece);
        }
        public void setSmoothingGroup(int shading) {
            _smoothingGroup = shading;
        }
        public int getSmoothingGroup() {
            return _smoothingGroup;
        }
    }

    public static class Face {
        public int[] _vertices;
        public int[] _texCoordinates;
        public int[] _normals;
        public Face(int[] vertices, int[] texCoordinates, int[] normals) {
            _vertices = vertices;
            _texCoordinates = texCoordinates;
            _normals = normals;
        }
    }

    public DisplayMeshFileLoader(String fileName) throws Exception {
        _objects = new ArrayList<>();
        _materialFileNames = new ArrayList<>();
        _vertices = new ArrayList<>();
        _texCoordinates = new ArrayList<>();
        _normals = new ArrayList<>();

        BufferedReader bufferedReader = null;
        try {
            File file = new File(fileName);
            bufferedReader = new BufferedReader(new FileReader(file));
            String line = bufferedReader.readLine();
            while (line != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    parseLine(line, fileName);
                }
                line = bufferedReader.readLine();
            }
        }
        finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }

    public ArrayList<Object> getObjects() {
        return _objects;
    }
    public ArrayList<String> getMaterialFileNames() {
        return _materialFileNames;
    }
    public ArrayList<Vector3f> getVertices() {
        return _vertices;
    }
    public ArrayList<Vector2f> getTexCoordinates() {
        return _texCoordinates;
    }
    public ArrayList<Vector3f> getNormals() {
        return _normals;
    }

    private void parseLine(String line, String fileName) throws Exception {
        String[] words = line.split(" ");
        switch (words[0]) {
            case "mtllib" -> parseMaterial(words);
            case "o" -> parseObject(words);
            case "v" -> parseVertex(words);
            case "vt" -> parseTexCoordinate(words);
            case "vn" -> parseNormal(words);
            case "usemtl" -> parseUseMaterial(words);
            case "s" -> parseSmoothingGroup(words);
            case "f" -> parseFace(words, fileName);
        }
    }

    private void parseObject(String[] words) {
        if (words.length == 2) {
            _objects.add(new Object(words[1]));
        }
    }

    private void parseMaterial(String[] words) {
        if (words.length == 2) {
            _materialFileNames.add(words[1]);
        }
    }

    private void parseVertex(String[] words) {
        if (words.length == 4) {
            _vertices.add(new Vector3f(
                    Float.parseFloat(words[1]),
                    Float.parseFloat(words[2]),
                    Float.parseFloat(words[3])
            ));
        }
    }

    private void parseTexCoordinate(String[] words) {
        if (words.length == 3) {
            _texCoordinates.add(new Vector2f(
                    Float.parseFloat(words[1]),
                    Float.parseFloat(words[2])
            ));
        }
    }

    private void parseNormal(String[] words) {
        if (words.length == 4) {
            _normals.add(new Vector3f(
                    Float.parseFloat(words[1]),
                    Float.parseFloat(words[2]),
                    Float.parseFloat(words[3])
            ));
        }
    }

    private void parseUseMaterial(String[] words) {
        if (words.length == 2 && !_objects.isEmpty()) {
            getCurrentObject().addPiece(new Piece(words[1]));
        }
    }

    private void parseSmoothingGroup(String[] words) {
        if (words.length == 2 && !words[1].equals("off") && !_objects.isEmpty()) {
            getCurrentObject().setSmoothingGroup(Integer.parseInt(words[1]));
        }
    }

    private void parseFace(String[] words, String fileName) throws Exception {
        if (words.length != 4 && !_objects.isEmpty()) {
            throw new Exception("Only triangles supported. fileName = ["+fileName+"]");
        }

        int[] vertex0 = parseInteger3(words[1]);
        int[] vertex1 = parseInteger3(words[2]);
        int[] vertex2 = parseInteger3(words[3]);

        int[] vertices = new int[3];
        vertices[0] = vertex0[0];
        vertices[1] = vertex1[0];
        vertices[2] = vertex2[0];

        int[] texCoordinates = new int[3];
        texCoordinates[0] = vertex0[1];
        texCoordinates[1] = vertex1[1];
        texCoordinates[2] = vertex2[1];

        int[] normals = new int[3];
        normals[0] = vertex0[2];
        normals[1] = vertex1[2];
        normals[2] = vertex2[2];

        getCurrentPiece().addFace(new Face(vertices, texCoordinates, normals));
    }

    private int[] parseInteger3(String triplet) throws Exception {
        String[] indices = triplet.split("/");
        if (indices.length != 3) {
            throw new Exception("Invalid face triplet");
        }
        int[] integer3 = new int[3];
        // Note that indices within the file are 1 based, NOT 0 based.
        integer3[0] = Integer.parseInt(indices[0]) - 1;
        integer3[1] = Integer.parseInt(indices[1]) - 1;
        integer3[2] = Integer.parseInt(indices[2]) - 1;
        return integer3;
    }

    private Object getCurrentObject() {
        if (_objects == null || _objects.size() == 0) {
            throw new RuntimeException("There is no current object");
        }
        return _objects.get(_objects.size() - 1);
    }

    private Piece getCurrentPiece() {
        Object object = getCurrentObject();
        if (object.getPieces() == null || object.getPieces().size() == 0) {
            throw new RuntimeException("There is no current object");
        }
        return object.getPieces().get(object.getPieces().size() - 1);
    }
}
