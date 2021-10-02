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

package com.lunargravity.engine.graphics;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Material {
    private final String _name;
    private boolean _bound;
    private Vector3f _ambientColour;
    private Vector4f _diffuseColour;
    private Vector3f _specularColour;
    private Vector3f _emissiveColour;
    private float _specularExponent;
    private float _indexOfRefraction;
    private float _dissolved;
    private float _transparency;
    private Vector3f _transmissionFilterColour;
    private int _illuminationModel;
    private String _ambientTextureFileName;
    private GlTexture _ambientTexture;
    private String _diffuseTextureFileName;
    private GlTexture _diffuseTexture;
    private String _specularTextureFileName;
    private GlTexture _specularTexture;
    private String _emissiveTextureFileName;
    private GlTexture _emissiveTexture;
    private String _specularExponentTextureFileName;
    private GlTexture _specularExponentTexture;
    private String _indexOfRefractionTextureFileName;
    private GlTexture _indexOfRefractionTexture;
    private String _dissolvedTextureFileName;
    private GlTexture _dissolvedTexture;
    private String _transparencyTextureFileName;
    private GlTexture _transparencyTexture;
    private String _transmissionFilterTextureFileName;
    private GlTexture _transmissionFilterTexture;

    public Material(String name) {
        _name = name;
        _bound = false;
    }

    public String getName() {
        return _name;
    }
    public boolean isBound() {
        return _bound;
    }

    public void bindTextures(TextureCache textureCache) {
        if (_ambientTextureFileName != null) {
            _ambientTexture = textureCache.get(_ambientTextureFileName);
        }
        if (_diffuseTextureFileName != null) {
            _diffuseTexture = textureCache.get(_diffuseTextureFileName);
        }
        if (_specularTextureFileName != null) {
            _specularTexture = textureCache.get(_specularTextureFileName);
        }
        if (_emissiveTextureFileName != null) {
            _emissiveTexture = textureCache.get(_emissiveTextureFileName);
        }
        if (_specularExponentTextureFileName != null) {
            _specularExponentTexture = textureCache.get(_specularExponentTextureFileName);
        }
        if (_indexOfRefractionTextureFileName != null) {
            _indexOfRefractionTexture = textureCache.get(_indexOfRefractionTextureFileName);
        }
        if (_dissolvedTextureFileName != null) {
            _dissolvedTexture = textureCache.get(_dissolvedTextureFileName);
        }
        if (_transparencyTextureFileName != null) {
            _transparencyTexture = textureCache.get(_transparencyTextureFileName);
        }
        if (_transmissionFilterTextureFileName != null) {
            _transmissionFilterTexture = textureCache.get(_transmissionFilterTextureFileName);
        }
        _bound = true;
    }

    public void setAmbientColour(Vector3f ambientColour) {
        _ambientColour = ambientColour;
    }
    public Vector3f getAmbientColour() {
        return _ambientColour;
    }

    public void setDiffuseColour(Vector4f diffuseColour) {
        _diffuseColour = diffuseColour;
    }
    public Vector4f getDiffuseColour() {
        return _diffuseColour;
    }

    public void setSpecularColour(Vector3f specularColour) {
        _specularColour = specularColour;
    }
    public Vector3f getSpecularColour() {
        return _specularColour;
    }

    public void setEmissiveColour(Vector3f emissiveColour) {
        _emissiveColour = emissiveColour;
    }
    public Vector3f getEmissiveColour() {
        return _emissiveColour;
    }

    public void setSpecularExponent(float specularExponent) {
        _specularExponent = specularExponent;
    }
    public float getSpecularExponent() {
        return _specularExponent;
    }

    public void setIndexOfRefraction(float indexOfRefraction) {
        _indexOfRefraction = indexOfRefraction;
    }
    public float getIndexOfRefraction() {
        return _indexOfRefraction;
    }

    public void setDissolved(float Dissolved) {
        _dissolved = Dissolved;
    }
    public float getDissolved() {
        return _dissolved;
    }

    public void setTransparency(float Transparency) {
        _transparency = Transparency;
    }
    public float getTransparency() {
        return _transparency;
    }

    public void setTransmissionFilterColour(Vector3f transmissionFilterColour) {
        _transmissionFilterColour = transmissionFilterColour;
    }
    public Vector3f getTransmissionFilterColour() {
        return _transmissionFilterColour;
    }

    public void setIlluminationModel(int illuminationModel) {
        _illuminationModel = illuminationModel;
    }
    public int getIlluminationModel() {
        return _illuminationModel;
    }

    public void setAmbientTextureFileName(String ambientTextureFileName) {
        _ambientTextureFileName = ambientTextureFileName;
    }
    public String getAmbientTextureFileName() {
        return _ambientTextureFileName;
    }
    public GlTexture getAmbientTexture() {
        return _ambientTexture;
    }

    public void setDiffuseTextureFileName(String diffuseTextureFileName) {
        _diffuseTextureFileName = diffuseTextureFileName;
    }
    public String getDiffuseTextureFileName() {
        return _diffuseTextureFileName;
    }
    public GlTexture getDiffuseTexture() {
        return _diffuseTexture;
    }

    public void setSpecularTextureFileName(String specularTextureFileName) {
        _specularTextureFileName = specularTextureFileName;
    }
    public String getSpecularTextureFileName() {
        return _specularTextureFileName;
    }
    public GlTexture getSpecularTexture() {
        return _specularTexture;
    }

    public void setEmissiveTextureFileName(String emissiveTextureFileName) {
        _emissiveTextureFileName = emissiveTextureFileName;
    }
    public String getEmissiveTextureFileName() {
        return _emissiveTextureFileName;
    }
    public GlTexture getEmissiveTexture() {
        return _emissiveTexture;
    }

    public void setSpecularExponentTextureFileName(String specularExponentTextureFileName) {
        _specularExponentTextureFileName = specularExponentTextureFileName;
    }
    public String getSpecularExponentTextureFileName() {
        return _specularExponentTextureFileName;
    }
    public GlTexture getSpecularExponentTexture() {
        return _specularExponentTexture;
    }

    public void setIndexOfRefractionTextureFileName(String indexOfRefractionTextureFileName) {
        _indexOfRefractionTextureFileName = indexOfRefractionTextureFileName;
    }
    public String getIndexOfRefractionTextureFileName() {
        return _indexOfRefractionTextureFileName;
    }
    public GlTexture getIndexOfRefractionTexture() {
        return _indexOfRefractionTexture;
    }

    public void setDissolvedTextureFileName(String dissolvedTextureFileName) {
        _dissolvedTextureFileName = dissolvedTextureFileName;
    }
    public String getDissolvedTextureFileName() {
        return _dissolvedTextureFileName;
    }
    public GlTexture getDissolvedTexture() {
        return _dissolvedTexture;
    }

    public void setTransparencyTextureFileName(String transparencyTextureFileName) {
        _transparencyTextureFileName = transparencyTextureFileName;
    }
    public String getTransparencyTextureFileName() {
        return _transparencyTextureFileName;
    }
    public GlTexture getTransparencyTexture() {
        return _transparencyTexture;
    }

    public void setTransmissionFilterTextureFileName(String transmissionFilterTextureFileName) {
        _transmissionFilterTextureFileName = transmissionFilterTextureFileName;
    }
    public String getTransmissionFilterTextureFileName() {
        return _transmissionFilterTextureFileName;
    }
    public GlTexture getTransmissionFilterTexture() {
        return _transmissionFilterTexture;
    }
}
