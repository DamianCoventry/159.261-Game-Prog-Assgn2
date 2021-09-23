package com.lunargravity.engine.graphics;

import org.joml.*;
import java.lang.Math;

public class Transform {
    public Vector3f _position;
    public Quaternionf _rotation;
    public Matrix4f _modelMatrix;
    public Matrix4f _viewMatrix;

    public Transform() {
        _position = new Vector3f(); // initialised as (0, 0, 0)
        _rotation = new Quaternionf(); // initialised as (0, 0, 0, 1)
        _modelMatrix = new Matrix4f(); // initialised as the identity matrix
        _viewMatrix = new Matrix4f(); // initialised as the identity matrix
    }

    public Transform(Vector3f position, Quaternionf rotation) {
        _position = position;
        _rotation = rotation;
        _modelMatrix = new Matrix4f(); // initialised as the identity matrix
        calculateModelMatrix();
        _viewMatrix = new Matrix4f(); // initialised as the identity matrix
        calculateViewMatrix();
    }

    public Transform(Vector3f position, Vector3f eulerDegrees, EulerOrder eulerOrder) {
        _position = position;

        Quaternionf xRot = new Quaternionf(); // initialised as (0, 0, 0, 1)
        xRot.rotationX((float)Math.toRadians(eulerDegrees.x));

        Quaternionf yRot = new Quaternionf(); // initialised as (0, 0, 0, 1)
        xRot.rotationY((float)Math.toRadians(eulerDegrees.y));

        Quaternionf zRot = new Quaternionf(); // initialised as (0, 0, 0, 1)
        xRot.rotationZ((float)Math.toRadians(eulerDegrees.z));

        switch (eulerOrder) {
            case XYZ:
                _rotation = zRot.mul(yRot).mul(xRot);
                break;
            case XZY:
                _rotation = yRot.mul(zRot).mul(xRot);
                break;
            case YXZ:
                _rotation = zRot.mul(xRot).mul(yRot);
                break;
            case YZX:
                _rotation = xRot.mul(zRot).mul(yRot);
                break;
            case ZXY:
                _rotation = yRot.mul(xRot).mul(zRot);
                break;
            case ZYX:
                _rotation = xRot.mul(yRot).mul(zRot);
                break;
        }

        _modelMatrix = new Matrix4f(); // initialised as the identity matrix
        calculateModelMatrix();
        _viewMatrix = new Matrix4f(); // initialised as the identity matrix
        calculateViewMatrix();
    }

    public void calculateModelMatrix() {
        _modelMatrix = _modelMatrix.identity().translate(_position).rotate(_rotation);
    }

    public Matrix4f getModelMatrix() {
        return _modelMatrix;
    }

    public Matrix4f toModelMatrix() {
        calculateModelMatrix();
        return _modelMatrix;
    }

    public void calculateViewMatrix() {
        _viewMatrix = _viewMatrix.identity().rotate(_rotation.invert()).translate(_position.negate());
    }

    public Matrix4f getViewMatrix() {
        return _viewMatrix;
    }

    public Matrix4f toViewMatrix() {
        calculateViewMatrix();
        return _viewMatrix;
    }
}
