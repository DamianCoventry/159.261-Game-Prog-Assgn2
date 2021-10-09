//
// Lunar Gravity
//
// This game is based upon the Amiga video game Gravity Force that was
// released in 1989 by Stephan Wenzler
//
// https://www.mobygames.com/game/gravity-force
// https://www.youtube.com/watch?v=m9mFtCvnko8
//
// This implementation is Copyright (c) 2021, Damian Coventry
// All rights reserved
// Written for Massey University course 159.261 Game Programming (Assignment 2)
//

package com.lunargravity.engine.animation;

import com.lunargravity.engine.core.IEngine;

import java.util.LinkedList;

public class AnimationManager {
    private static class AnimationInfo {
        public long _timeoutMs;
        public long _lastCallTimeMs;
        public AnimationInfo(long nowMs, long timeoutMs) {
            _timeoutMs = timeoutMs;
            _lastCallTimeMs = nowMs;
        }
    }

    private final IEngine _engine;
    private final LinkedList<FCurve> _pendingRemovals;
    private final LinkedList<FCurve> _pendingAdditions;
    private final LinkedList<FCurve> _currentFCurves;

    public AnimationManager(IEngine engine) {
        _engine = engine;
        _pendingRemovals = new LinkedList<>();
        _pendingAdditions = new LinkedList<>();
        _currentFCurves = new LinkedList<>();
    }

    public long getNowMs() {
        return _engine.getNowMs();
    }

    public void start(FCurve fCurve) {
        _pendingAdditions.add(fCurve);
    }

    public void stop(FCurve fCurve) {
        _pendingRemovals.remove(fCurve);
    }

    public void update(long nowMs) {
        performPendingRemovals();
        performPendingAdditions();
        updateAnimations(nowMs);
    }

    private void performPendingAdditions() {
        _currentFCurves.addAll(_pendingAdditions);
        _pendingAdditions.clear();
    }

    private void performPendingRemovals() {
        for (var id : _pendingRemovals) {
            _currentFCurves.remove(id);
        }
        _pendingRemovals.clear();
    }

    private void updateAnimations(long nowMs) {
        for (var fCurve : _currentFCurves) {
            fCurve.update(nowMs);
            if (nowMs >= fCurve.getEndTime()) {
                _pendingRemovals.add(fCurve);
                fCurve.setValue(fCurve.getEndValue());
                fCurve.completed(nowMs - fCurve.getEndTime());
            }
        }
    }
}
