package com.lunargravity.engine.animation;

import org.joml.Vector3f;

import java.util.function.LongConsumer;

public class Vector3FCurve implements IFCurve {
    private final AnimationManager _animationManager;
    protected int _registeredId;
    protected Vector3f _currentValue;
    protected Vector3f _startValue;
    protected Vector3f _endValue;
    protected long _startTime;
    protected long _endTime;
    protected long _durationMs;
    protected EndBehaviour _endBehaviour;
    protected LongConsumer _completedFunction;
    protected boolean _completed;

    protected Vector3FCurve(AnimationManager animationManager) {
        _registeredId = animationManager.register(this);
        _animationManager = animationManager;
        _endBehaviour = EndBehaviour.STOP;

        _startValue = new Vector3f();
        _endValue = new Vector3f();
        _currentValue = new Vector3f();

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

    public void setValue(Vector3f value) {
        _currentValue = value;
    }
    public Vector3f getValue() {
        return _currentValue;
    }
    public Vector3f getEndValue() {
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

    @Override
    public void update(long nowMs) {
        // No default implementation
    }

    @Override
    public void setEndBehaviour(IFCurve.EndBehaviour endBehaviour) {
        _endBehaviour = endBehaviour;
    }

    public void setCompletedFunction(LongConsumer completedFunction) {
        _completedFunction = completedFunction;
    }

    public void start(Vector3f startValue, Vector3f endValue, long durationMs) {
        if (_registeredId == 0) {
            return;
        }
        _startValue.set(startValue);
        _endValue.set(endValue);
        _currentValue.set(startValue);
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

    @Override
    public void completed(long elapsedTime) {
        _currentValue.set(_endValue);
        _completed = true;
        if (_completedFunction != null) {
            _completedFunction.accept(elapsedTime);
        }
    }
}
