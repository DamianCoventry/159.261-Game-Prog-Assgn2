package com.lunargravity.engine.animation;

public class FloatLinearInterp extends FloatFCurve {
    public FloatLinearInterp(AnimationManager animationManager) {
        super(animationManager);
    }

    @Override
    public void update(long nowMs) {
        float percentage = (float)(nowMs - _startTime) / (float)_durationMs;
        _currentValue = _startValue + ((_endValue - _startValue) * percentage);
    }
}
