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

package com.lunargravity.campaign.statemachine;

import com.lunargravity.engine.scene.ISceneBuilderObserver;

public class NullBuilderObserver implements ISceneBuilderObserver {
    @Override
    public void freeResources() {
        // Nothing to do
    }

    @Override
    public void sceneBuildBeginning() {
        // Nothing to do
    }

    @Override
    public void sceneBuildEnded() {
        // Nothing to do
    }

    @Override
    public void sceneBuildProgressed(int currentItem, int totalItems) {
        // Nothing to do
    }
}
