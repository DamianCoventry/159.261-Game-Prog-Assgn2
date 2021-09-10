package com.lunargravity.model;

public class LevelInfo {
    private int _numPlayers;
    private int _numCrates;
    private int _numDeliveryZones;
    private EnemyShipType[] _enemyShipTypes;
    private int[] _fuelCanisterLitres;
    private int[] _repairGarageCapacities;
    private int[] _energyBeamStrengths;
    private WeaponType[] _weaponTypes;

    public LevelInfo() {
        _numPlayers = 0;
        _numCrates = 0;
        _numDeliveryZones = 0;
        _enemyShipTypes = null;
        _fuelCanisterLitres = null;
        _repairGarageCapacities = null;
        _energyBeamStrengths = null;
        _weaponTypes = null;
    }

    public LevelInfo(int numPlayers, int numCrates, int numDeliveryZones, EnemyShipType[] enemyShips,
                     int[] fuelCanisterLitres, int[] repairGarageCapacities, int[] energyBeamStrengths,
                     WeaponType[] weaponTypes) {
        _numPlayers = numPlayers;
        _numCrates = numCrates;
        _numDeliveryZones = numDeliveryZones;
        _enemyShipTypes = enemyShips;
        _fuelCanisterLitres = fuelCanisterLitres;
        _repairGarageCapacities = repairGarageCapacities;
        _energyBeamStrengths = energyBeamStrengths;
        _weaponTypes = weaponTypes;
    }

    public int getNumPlayers() {
        return _numPlayers;
    }
    public void setNumPlayers(int numPlayers) {
        _numPlayers = numPlayers;
    }

    public int getNumCrates() {
        return _numCrates;
    }
    public void setNumCrates(int numCrates) {
        _numCrates = numCrates;
    }

    public int getNumDeliveryZones() {
        return _numDeliveryZones;
    }
    public void setNumDeliveryZones(int numDeliveryZones) {
        _numDeliveryZones = numDeliveryZones;
    }

    public EnemyShipType[] getEnemyShips() {
        return _enemyShipTypes;
    }
    public void setEnemyShips(EnemyShipType[] enemyShipTypes) {
        _enemyShipTypes = enemyShipTypes;
    }

    public int[] getFuelCanisterLitres() {
        return _fuelCanisterLitres;
    }
    public void setFuelCanisterLitres(int[] fuelCanisterLitres) {
        _fuelCanisterLitres = fuelCanisterLitres;
    }

    public int[] getRepairGarageCapacities() {
        return _repairGarageCapacities;
    }
    public void setRepairGarageCapacities(int[] repairGarageCapacities) {
        _repairGarageCapacities = repairGarageCapacities;
    }

    public int[] getEnergyBeamStrengths() {
        return _energyBeamStrengths;
    }
    public void setEnergyBeamStrengths(int[] energyBeamStrengths) {
        _energyBeamStrengths = energyBeamStrengths;
    }

    public WeaponType[] getWeaponTypes() {
        return _weaponTypes;
    }
    public void setWeaponTypes(WeaponType[] weaponTypes) {
        _weaponTypes = weaponTypes;
    }
}
