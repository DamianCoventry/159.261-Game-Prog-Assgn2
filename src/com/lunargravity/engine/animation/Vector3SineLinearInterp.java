package com.lunargravity.engine.animation;

public class Vector3SineLinearInterp extends Vector3FCurve {
    public Vector3SineLinearInterp(AnimationManager animationManager) {
        super(animationManager);
    }

    @Override
    public void update(long nowMs) {
        float percentage = (float)(nowMs - _startTime) / (float)_durationMs;
        float sinAngle = (float)Math.sin(Math.toRadians(90.0f * percentage));
        _currentValue.x = _startValue.x + ((_endValue.x - _startValue.x) * sinAngle);
        _currentValue.y = _startValue.y + ((_endValue.y - _startValue.y) * sinAngle);
        _currentValue.z = _startValue.z + ((_endValue.z - _startValue.z) * sinAngle);
    }
}
