package com.lunargravity.engine.graphics;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class MaterialFileLoader {
    private final ArrayList<Material> _materials;

    public MaterialFileLoader(String fileName) throws Exception {
        _materials = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            File file = new File(fileName);
            bufferedReader = new BufferedReader(new FileReader(file));
            String line = bufferedReader.readLine();
            while (line != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    parseLine(line);
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

    public ArrayList<Material> getMaterials() {
        return _materials;
    }

    private void parseLine(String line) {
        String[] words = line.split(" ");
        switch (words[0]) {
            case "newmtl" -> parseNewMaterial(words);
            case "Ka" ->  // ambient colour
                    parseAmbientColour(words);
            case "Kd" ->  // diffuse colour
                    parseDiffuseColour(words);
            case "Ks" ->  // specular colour
                    parseSpecularColour(words);
            case "Ke" ->  // emissive colour
                    parseEmissiveColour(words);
            case "Ns" ->  // specular exponent
                    parseSpecularExponent(words);
            case "Ni" ->  // index of refraction
                    parseOfRefraction(words);
            case "d" ->  // Dissolved
                    parseDissolved(words);
            case "Tr" ->  // Transparency
                    parseTransparency(words);
            case "Tf" ->  // transmission filter colour
                    parseTransmissionFilterColour(words);
            case "illum" ->  // illumination model
                    parseIlluminationModel(words);
            case "map_Ka" ->  // ambient texture
                    parseAmbientTexture(words);
            case "map_Kd" ->  // diffuse texture
                    parseDiffuseTexture(words);
            case "map_Ks" ->  // specular texture
                    parseSpecularTexture(words);
            case "map_Ke" ->  // emissive texture
                    parseEmissiveTexture(words);
            case "map_Ns" ->  // specular exponent texture
                    parseSpecularExponentTexture(words);
            case "map_Ni" ->  // index of refraction texture
                    parseIndexOfRefractionTexture(words);
            case "map_d" ->  // Dissolved texture
                    parseDissolvedTexture(words);
            case "map_Tr" ->  // Transparency texture
                    parseTransparencyTexture(words);
            case "map_Tf" ->  // transmission filter texture
                    parseTransmissionFilterTexture(words);
        }
    }

    private void parseNewMaterial(String[] words) {
        if (words.length == 2) {
            _materials.add(new Material(words[1]));
        }
    }

    private void parseAmbientColour(String[] words) {
        if (words.length == 4 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setAmbientColour(parseColour(words));
        }
    }

    private void parseDiffuseColour(String[] words) {
        if (words.length == 4 && !_materials.isEmpty()) {
            Vector3f colour = parseColour(words);
            if (colour == null) {
                throw new RuntimeException("Diffuse colour is null");
            }
            _materials.get(_materials.size() - 1).setDiffuseColour(new Vector4f(colour.x, colour.y, colour.z, 1.0f));
        }
    }

    private void parseSpecularColour(String[] words) {
        if (words.length == 4 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setSpecularColour(parseColour(words));
        }
    }

    private void parseEmissiveColour(String[] words) {
        if (words.length == 4 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setEmissiveColour(parseColour(words));
        }
    }

    private void parseSpecularExponent(String[] words) {
        // Blender stores its 'Roughness' value from its Principled BSDF within the Ns value.
        // It maps (0, 1) from the Blender GUI to (900, 0) for some weird reason.
        if (words.length == 2 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setSpecularExponent(Float.parseFloat(words[1]));
        }
    }

    private void parseOfRefraction(String[] words) {
        if (words.length == 2 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setIndexOfRefraction(Float.parseFloat(words[1]));
        }
    }

    private void parseDissolved(String[] words) {
        // 0 == fully transparent, 1 = fully opaque
        if (words.length == 2 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setDissolved(Float.parseFloat(words[1]));
        }
    }

    private void parseTransparency(String[] words) {
        // 0 == fully opaque, 1 = fully transparent
        if (words.length == 2 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setTransparency(Float.parseFloat(words[1]));
        }
    }

    private void parseTransmissionFilterColour(String[] words) {
        if (words.length == 4 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setTransmissionFilterColour(parseColour(words));
        }
    }

    private void parseIlluminationModel(String[] words) {
        if (words.length == 2 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setIlluminationModel(Integer.parseInt(words[1]));
        }
    }

    private void parseAmbientTexture(String[] words) {
        if (words.length == 2 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setAmbientTextureFileName(words[1]);
        }
    }

    private void parseDiffuseTexture(String[] words) {
        if (words.length == 2 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setDiffuseTextureFileName(words[1]);
        }
    }

    private void parseSpecularTexture(String[] words) {
        if (words.length == 2 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setSpecularTextureFileName(words[1]);
        }
    }

    private void parseEmissiveTexture(String[] words) {
        if (words.length == 2 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setEmissiveTextureFileName(words[1]);
        }
    }

    private void parseSpecularExponentTexture(String[] words) {
        if (words.length == 2 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setSpecularExponentTextureFileName(words[1]);
        }
    }

    private void parseIndexOfRefractionTexture(String[] words) {
        if (words.length == 2 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setIndexOfRefractionTextureFileName(words[1]);
        }
    }

    private void parseDissolvedTexture(String[] words) {
        if (words.length == 2 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setDissolvedTextureFileName(words[1]);
        }
    }

    private void parseTransparencyTexture(String[] words) {
        if (words.length == 2 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setTransparencyTextureFileName(words[1]);
        }
    }

    private void parseTransmissionFilterTexture(String[] words) {
        if (words.length == 2 && !_materials.isEmpty()) {
            _materials.get(_materials.size() - 1).setTransmissionFilterTextureFileName(words[1]);
        }
    }

    private Vector3f parseColour(String[] words) {
        if (words.length == 4) {
            return new Vector3f(
                    Float.parseFloat(words[1]),
                    Float.parseFloat(words[2]),
                    Float.parseFloat(words[3])
            );
        }
        return null;
    }
}
