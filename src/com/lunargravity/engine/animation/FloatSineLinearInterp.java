package com.lunargravity.engine.animation;

public class FloatSineLinearInterp extends FloatFCurve {
    public FloatSineLinearInterp(AnimationManager animationManager) {
        super(animationManager);
    }

    @Override
    public void update(long nowMs) {
        float percentage = (float)(nowMs - _startTime) / (float)_durationMs;
        float sinAngle = (float)Math.sin(Math.toRadians(90.0f * percentage));
        _currentValue = _startValue + ((_endValue - _startValue) * sinAngle);
    }
}
