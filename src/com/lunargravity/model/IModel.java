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

package com.lunargravity.model;

import org.jetbrains.annotations.*;

import java.util.ArrayList;

public interface IModel {
    @NotNull
    String toJson();
    @NotNull
    Player[] getPlayers();
    @NotNull
    Crate[] getCrates();
    @NotNull
    DeliveryZone[] getDeliveryZones();

    @Nullable
    EnemyShip[] getEnemyShips();
    @Nullable
    FuelCanister[] getFuelCanisters();
    @Nullable
    RepairGarage[] getRepairGarages();
    @Nullable
    ArrayList<EnergyBeam> getEnergyBeams();
    @Nullable
    ArrayList<Plasma> getPlasmas();
    @Nullable
    ArrayList<Rocket> getRockets();
    @Nullable
    ArrayList<Explosion> getExplosions();
    @Nullable
    ArrayList<IWeapon> getWeapons();
}
