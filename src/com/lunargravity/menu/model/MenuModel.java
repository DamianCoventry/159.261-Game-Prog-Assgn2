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

package com.lunargravity.menu.model;

import com.lunargravity.engine.scene.ISceneStateOwner;

// TODO: is this class needed?
public class MenuModel implements IMenuModel, ISceneStateOwner {
    @Override
    public String modelToJson() {
        // TODO
        return null;
    }

    @Override
    public void modelFromJson(String json) {
        // TODO
    }

    @Override
    public void resetRaceScoreboard() {
        // TODO
    }

    @Override
    public void resetDogfightScoreboard() {
        // TODO
    }

    @Override
    public void stateSettingLoaded(String name, String value) {
        // TODO
    }
}
