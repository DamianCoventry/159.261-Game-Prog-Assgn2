package com.lunargravity.engine.animation;

public class Vector3LinearInterp extends Vector3FCurve {
    public Vector3LinearInterp(AnimationManager animationManager) {
        super(animationManager);
    }

    @Override
    public void update(long nowMs) {
        float percentage = (float)(nowMs - _startTime) / (float)_durationMs;
        _currentValue.x = _startValue.x + ((_endValue.x - _startValue.x) * percentage);
        _currentValue.y = _startValue.y + ((_endValue.y - _startValue.y) * percentage);
        _currentValue.z = _startValue.z + ((_endValue.z - _startValue.z) * percentage);
    }
}
