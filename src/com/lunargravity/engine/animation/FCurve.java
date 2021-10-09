package com.lunargravity.engine.animation;

import java.util.function.LongConsumer;

public abstract class FCurve {
    private final AnimationManager _animationManager;
    protected int _registeredId;
    protected float _currentValue;
    protected float _startValue;
    protected float _endValue;
    protected long _startTime;
    protected long _endTime;
    protected long _durationMs;
    protected EndBehaviour _endBehaviour;
    protected LongConsumer _completedFunction;
    protected boolean _completed;

    protected FCurve(AnimationManager animationManager) {
        _registeredId = animationManager.register(this);
        _animationManager = animationManager;
        _endBehaviour = EndBehaviour.STOP;

        _startValue = 0;
        _endValue = 0;
        _currentValue = 0;

        _startTime = _animationManager.getNowMs();
        _endTime = 0;
        _durationMs = 0;
        _completed = false;
    }

    public void unregister() {
        if (_registeredId > 0) {
            _animationManager.unregister(_registeredId);
            _registeredId = 0;
        }
    }

    public enum EndBehaviour { STOP, LOOP}

    public void setValue(float value) {
        _currentValue = value;
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
    public boolean isCompleted() {
        return _completed;
    }

    public EndBehaviour getEndBehaviour() {
        return _endBehaviour;
    }
    public void setEndBehaviour(EndBehaviour endBehaviour) {
        _endBehaviour = endBehaviour;
    }

    public void setCompletedFunction(LongConsumer completedFunction) {
        _completedFunction = completedFunction;
    }

    public void start(float startValue, float endValue, long durationMs) {
        if (_registeredId == 0) {
            return;
        }
        _startValue = startValue;
        _endValue = endValue;
        _currentValue = startValue;
        _durationMs = durationMs;
        _startTime = _animationManager.getNowMs();
        _endTime = _startTime + durationMs;
        _completed = false;
        _animationManager.start(_registeredId);
    }

    public void stop() {
        if (_registeredId > 0) {
            _animationManager.stop(_registeredId);
        }
    }

    public void completed(long elapsedTime) {
        _completed = true;
        if (_completedFunction != null) {
            _completedFunction.accept(elapsedTime);
        }
    }

    protected abstract void update(long nowMs);
}
