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

package com.lunargravity.engine.timeouts;

import java.util.*;
import java.util.function.Function;

public class TimeoutManager {
    private static int UNIQUE_ID = 0;
    private static class TimeoutInfo {
        public long _timeoutMs;
        public long _lastCallTimeMs;
        public int _callCount;
        public Function<Integer, CallbackResult> _callback;
        public TimeoutInfo(long nowMs, long timeoutMs, Function<Integer, CallbackResult> callback) {
            _timeoutMs = timeoutMs;
            _lastCallTimeMs = nowMs;
            _callCount = 0;
            _callback = callback;
        }
    }

    private final ArrayList<Integer> _pendingRemovals;
    private final Map<Integer, TimeoutInfo> _pendingAdditions;
    private final Map<Integer, TimeoutInfo> _currentTimeouts;

    public TimeoutManager() {
        _pendingRemovals = new ArrayList<>();
        _pendingAdditions = new HashMap<>();
        _currentTimeouts = new HashMap<>();
    }

    public enum CallbackResult { KEEP_CALLING, REMOVE_THIS_CALLBACK }

    public int addTimeout(long timeoutMs, Function<Integer, CallbackResult> callback) {
        int timeoutId = ++UNIQUE_ID;
        _pendingAdditions.put(timeoutId, new TimeoutInfo(System.currentTimeMillis(), timeoutMs, callback));
        return timeoutId;
    }

    public void removeTimeout(int timeoutId) {
        _pendingRemovals.add(timeoutId);
    }

    public void dispatchTimeouts(long nowMs) {
        performPendingRemovals();
        performPendingAdditions();
        dispatchCurrentTimeouts(nowMs);
    }

    private void performPendingAdditions() {
        _currentTimeouts.putAll(_pendingAdditions);
        _pendingAdditions.clear();
    }

    private void performPendingRemovals() {
        for (var id : _pendingRemovals) {
            _currentTimeouts.remove(id);
        }
        _pendingRemovals.clear();
    }

    private void dispatchCurrentTimeouts(long nowMs) {
        for (var timeout : _currentTimeouts.entrySet()) {
            TimeoutInfo info = timeout.getValue();
            if (nowMs - info._lastCallTimeMs >= info._timeoutMs) {
                info._lastCallTimeMs = nowMs;
                TimeoutManager.CallbackResult result = info._callback.apply(++info._callCount);
                if (result == CallbackResult.REMOVE_THIS_CALLBACK) {
                    _pendingRemovals.add(timeout.getKey());
                }
            }
        }
    }
}
