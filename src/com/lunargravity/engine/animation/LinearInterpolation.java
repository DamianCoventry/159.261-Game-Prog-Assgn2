package com.lunargravity.engine.animation;

public class LinearInterpolation extends FCurve {
    public LinearInterpolation(AnimationManager animationManager) {
        super(animationManager);
    }

    @Override
    protected void update(long nowMs) {
        float percentage = (float)(nowMs - _startTime) / (float)_durationMs;
        _currentValue = _startValue + ((_endValue - _startValue) * percentage);
    }
}
