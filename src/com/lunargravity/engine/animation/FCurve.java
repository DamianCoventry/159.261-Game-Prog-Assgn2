package com.lunargravity.engine.animation;

import java.util.function.LongConsumer;

public abstract class FCurve {
    private final AnimationManager _animationManager;
    protected float _currentValue;
    protected float _startValue;
    protected float _endValue;
    protected long _startTime;
    protected long _endTime;
    protected long _durationMs;
    private LongConsumer _completedFunction;

    protected FCurve(AnimationManager animationManager) {
        _animationManager = animationManager;

        _startValue = 0;
        _endValue = 0;
        _currentValue = 0;

        _startTime = _animationManager.getNowMs();
        _endTime = 0;
        _durationMs = 0;
    }

    public float getValue() {
        return _currentValue;
    }
    public float getEndValue() {
        return _endValue;
    }
    public long getEndTime() {
        return _endTime;
    }
    public void setValue(float value) {
        _currentValue = value;
    }
    public void resetValue() {
        _currentValue = _startValue;
    }

    public void setCompletedFunction(LongConsumer completedFunction) {
        _completedFunction = completedFunction;
    }

    public void start(float startValue, float endValue, long durationMs) {
        _startValue = startValue;
        _endValue = endValue;
        _currentValue = startValue;
        _durationMs = durationMs;
        _startTime = _animationManager.getNowMs();
        _endTime = _startTime + durationMs;
        _animationManager.start(this);
    }

    public void completed(long elapsedTime) {
        if (_completedFunction != null) {
            _completedFunction.accept(elapsedTime);
        }
    }

    protected abstract void update(long nowMs);
}
