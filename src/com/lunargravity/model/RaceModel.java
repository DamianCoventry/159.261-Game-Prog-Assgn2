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

import com.google.gson.Gson;
import org.jetbrains.annotations.*;

import java.util.ArrayList;

public class RaceModel implements IModel {
    private final Player[] _players;
    private final Crate[] _crates;
    private final DeliveryZone[] _deliveryZones;
    private final EnemyShip[] _enemyShips;
    private final FuelCanister[] _fuelCanisters;
    private final RepairGarage[] _repairGarages;
    private final ArrayList<EnergyBeam> _energyBeams;
    private final ArrayList<Plasma> _plasmas;
    private final ArrayList<Rocket> _rockets;
    private final ArrayList<Explosion> _explosions;
    private final ArrayList<IWeapon> _weapons;

    static public RaceModel fromJson(String json) {
        if (json == null) {
            throw new IllegalArgumentException("json");
        }

        Gson parser = new Gson();
        return new RaceModel(parser.fromJson(json, LevelInfo.class));
    }

    // This constructor is public for unit tests only. The idea is for the production code to
    // create a model from JSON via the static public fromJson method.
    public RaceModel(LevelInfo levelInfo) {
        if (levelInfo == null) {
            throw new IllegalArgumentException("levelInfo");
        }
        if (levelInfo.getNumPlayers() < 1) {
            throw new IllegalArgumentException("levelInfo.getNumPlayers()");
        }
        if (levelInfo.getEnergyBeamStrengths() == null) {
            throw new IllegalArgumentException("levelInfo.getEnergyBeamStrengths()");
        }
        if (levelInfo.getEnergyBeamStrengths().length < 1) {
            throw new IllegalArgumentException("levelInfo.getEnergyBeamStrengths().length");
        }

        _players = new Player[levelInfo.getNumPlayers()];
        for (int i = 0; i < levelInfo.getNumPlayers(); ++i) {
            _players[i] = new Player();
        }

        _crates = new Crate[0];
        _deliveryZones = new DeliveryZone[0];
        _enemyShips = new EnemyShip[0];
        _fuelCanisters = new FuelCanister[0];
        _repairGarages = new RepairGarage[0];

        _energyBeams = new ArrayList<>();
        _explosions = new ArrayList<>();
        _plasmas = new ArrayList<>();
        _rockets = new ArrayList<>();
        _weapons = new ArrayList<>();

        for (int i = 0; i < levelInfo.getEnergyBeamStrengths().length; ++i) {
            _energyBeams.add(new EnergyBeam(levelInfo.getEnergyBeamStrengths()[i]));
        }
    }

    @Override
    public @NotNull String toJson() {
        EnemyShipType[] enemyShipTypes;
        if (_enemyShips != null) {
            enemyShipTypes = new EnemyShipType[_enemyShips.length];
            for (int i = 0; i < _enemyShips.length; ++i) {
                enemyShipTypes[i] = _enemyShips[i].getType();
            }
        }
        else {
            enemyShipTypes = null;
        }

        int[] fuelCanisterLitres;
        if (_fuelCanisters != null) {
            fuelCanisterLitres = new int[_fuelCanisters.length];
            for (int i = 0; i < _fuelCanisters.length; ++i) {
                fuelCanisterLitres[i] = _fuelCanisters[i].getStartingLitres();
            }
        }
        else {
            fuelCanisterLitres = null;
        }

        int[] repairGarageCapacities;
        if (_repairGarages != null) {
            repairGarageCapacities = new int[_repairGarages.length];
            for (int i = 0; i < _repairGarages.length; ++i) {
                repairGarageCapacities[i] = _repairGarages[i].getStartingCapacity();
            }
        }
        else {
            repairGarageCapacities = null;
        }

        int[] energyBeamStrengths;
        if (_energyBeams != null) {
            energyBeamStrengths = new int[_energyBeams.size()];
            for (int i = 0; i < _energyBeams.size(); ++i) {
                energyBeamStrengths[i] = _energyBeams.get(i).getDamagePerSecond();
            }
        }
        else {
            energyBeamStrengths = null;
        }

        WeaponType[] weaponTypes;
        if (_weapons != null) {
            weaponTypes = new WeaponType[_weapons.size()];
            for (int i = 0; i < _weapons.size(); ++i) {
                weaponTypes[i] = _weapons.get(i).getType();
            }
        }
        else {
            weaponTypes = null;
        }

        final Gson parser = new Gson();
        final LevelInfo levelInfo = new LevelInfo(
                _players.length, _crates.length, _deliveryZones.length, enemyShipTypes,
                fuelCanisterLitres, repairGarageCapacities, energyBeamStrengths, weaponTypes);
        return parser.toJson(levelInfo);
    }

    @Override
    public @NotNull Player[] getPlayers() {
        return _players;
    }

    @Override
    public @NotNull Crate[] getCrates() {
        return _crates;
    }

    @Override
    public @NotNull DeliveryZone[] getDeliveryZones() {
        return _deliveryZones;
    }

    @Override
    public @Nullable EnemyShip[] getEnemyShips() {
        return _enemyShips;
    }

    @Override
    public @Nullable FuelCanister[] getFuelCanisters() {
        return _fuelCanisters;
    }

    @Override
    public @Nullable RepairGarage[] getRepairGarages() {
        return _repairGarages;
    }

    @Override
    public @Nullable ArrayList<EnergyBeam> getEnergyBeams() {
        return _energyBeams;
    }

    @Override
    public @Nullable ArrayList<Plasma> getPlasmas() {
        return _plasmas;
    }

    @Override
    public @Nullable ArrayList<Rocket> getRockets() {
        return _rockets;
    }

    @Override
    public @Nullable ArrayList<Explosion> getExplosions() {
        return _explosions;
    }

    @Override
    public @Nullable ArrayList<IWeapon> getWeapons() {
        return _weapons;
    }
}