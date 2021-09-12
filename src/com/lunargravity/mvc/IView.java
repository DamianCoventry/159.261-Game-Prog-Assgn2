package com.lunargravity.mvc;

import org.joml.Matrix4f;

public interface IView {
    void think();
    void draw3d(int viewport, Matrix4f projectionMatrix);
    void draw2d(int viewport, Matrix4f projectionMatrix);
}
