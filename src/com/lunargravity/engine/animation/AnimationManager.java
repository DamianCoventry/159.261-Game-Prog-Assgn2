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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class AnimationManager {
    private static int _nextId;
    private final IEngine _engine;
    private final HashMap<Integer, FCurve> _fCurves;
    private final HashSet<Integer> _activeFCurves;
    private final LinkedList<Integer> _pendingStarts;
    private final LinkedList<Integer> _pendingStops;

    public AnimationManager(IEngine engine) {
        _engine = engine;
        _fCurves = new HashMap<>();
        _pendingStarts = new LinkedList<>();
        _pendingStops = new LinkedList<>();
        _activeFCurves = new HashSet<>();
        _nextId = 0;
    }

    public long getNowMs() {
        return _engine.getNowMs();
    }

    public int register(FCurve fCurve) {
        int id = ++_nextId;
        _fCurves.put(id, fCurve);
        return id;
    }

    public void unregister(int id) {
        _fCurves.remove(id);
        if (_activeFCurves.contains(id)) {
            _pendingStops.add(id);
        }
    }

    public void start(int id) {
        if (_fCurves.containsKey(id)) {
            _activeFCurves.add(id); // Won't add duplicates
        }
    }

    public void stop(int id) {
        if (_fCurves.containsKey(id) && _activeFCurves.contains(id)) {
            _pendingStops.add(id);
        }
    }

    public void update(long nowMs) {
        _pendingStops.forEach(_activeFCurves::remove);
        _pendingStops.clear();

        _activeFCurves.addAll(_pendingStarts);
        _pendingStarts.clear();

        for (var id : _activeFCurves) {
            FCurve fCurve = _fCurves.get(id);
            fCurve.update(nowMs);
            if (fCurve.getEndBehaviour() == FCurve.EndBehaviour.STOP && nowMs >= fCurve.getEndTime()) {
                _pendingStops.add(id);
                fCurve.setValue(fCurve.getEndValue());
                fCurve.completed(nowMs - fCurve.getEndTime());
            }
        }
    }
}
