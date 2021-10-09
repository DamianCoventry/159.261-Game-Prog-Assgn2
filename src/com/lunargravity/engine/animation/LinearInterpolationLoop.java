package com.lunargravity.engine.animation;

public class LinearInterpolationLoop extends FCurve {
    public LinearInterpolationLoop(AnimationManager animationManager) {
        super(animationManager);
        _endBehaviour = EndBehaviour.LOOP;
    }

    @Override
    protected void update(long nowMs) {
        float percentage = (float)(nowMs - _startTime) / (float)_durationMs;
        if (percentage > 1.0f) {
            percentage -= 1.0f;
            _startTime = nowMs;
            _endTime = _startTime + _durationMs;
        }
        _currentValue = _startValue + ((_endValue - _startValue) * percentage);
    }
}
