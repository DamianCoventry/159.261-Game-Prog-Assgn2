package com.lunargravity.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RaceModelTest {

    @Test
    void modelWillNotCreateWithNullArgument() {
        assertThrows(IllegalArgumentException.class, () -> new RaceModel(null));
    }

    @Test
    void modelWillNotCreateWithInvalidNumEnergyBeams() {
        LevelInfo levelInfo = new LevelInfo();
        levelInfo.setNumPlayers(1);
        assertThrows(IllegalArgumentException.class, () -> new RaceModel(levelInfo));
    }

    @Test
    void modelCreatesProperly() {
        LevelInfo levelInfo = new LevelInfo();
        levelInfo.setNumPlayers(1);
        levelInfo.setEnergyBeamStrengths(new int[] { 1, 2, 3, 4 });
        RaceModel raceModel = new RaceModel(levelInfo);
        assertNotNull(raceModel);
    }

    @Test
    void fromJsonProducesCorrectObject() {
        final String json =
                """
                {
                    "_numPlayers": 2,
                    "_energyBeamStrengths": [
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                    ]
                }""";

        final RaceModel raceModel = RaceModel.fromJson(json);
        assertEquals(2, raceModel.getPlayers().length);
        assertEquals(0, raceModel.getCrates().length);
        assertEquals(0, raceModel.getDeliveryZones().length);
        assertEquals(0, raceModel.getEnemyShips().length);
        assertEquals(0, raceModel.getFuelCanisters().length);
        assertEquals(0, raceModel.getRepairGarages().length);
        assertNotNull(raceModel.getEnergyBeams());
        assertEquals(10, raceModel.getEnergyBeams().size());
        assertNotNull(raceModel.getPlasmas());
        assertNotNull(raceModel.getRockets());
        assertNotNull(raceModel.getExplosions());
    }

    @Test
    void toJsonProducesExpectedJson() {
        final LevelInfo levelInfo = new LevelInfo();
        levelInfo.setNumPlayers(2);
        levelInfo.setEnergyBeamStrengths(new int[] { 1, 2, 3, 4 });

        final RaceModel raceModel = new RaceModel(levelInfo);
        final String actual = raceModel.toJson();
        final String expected = "{\"_numPlayers\":2,\"_numCrates\":0,\"_numDeliveryZones\":0,\"_enemyShipTypes\":[],\"_fuelCanisterLitres\":[],\"_repairGarageCapacities\":[],\"_energyBeamStrengths\":[1,2,3,4],\"_weaponTypes\":[]}";
        assertEquals(expected, actual);
    }

    @Test
    void getPlayersReturnsExpectedObjects() {
        final String json =
                """
                {
                    "_numPlayers": 2,
                    "_energyBeamStrengths": [
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                    ]
                }""";

        final RaceModel raceModel = RaceModel.fromJson(json);
        assertEquals(2, raceModel.getPlayers().length);
        assertEquals(Player.MAX_LUNAR_LANDERS, raceModel.getPlayers()[0].getNumLunarLanders());
        assertNotNull(raceModel.getPlayers()[0].getCurrentLunarLander());
        assertEquals(LunarLander.State.NOT_YET_BORN, raceModel.getPlayers()[0].getCurrentLunarLander().getState());
        assertEquals(LunarLander.MAX_FUEL_LITRES, raceModel.getPlayers()[0].getCurrentLunarLander().getFuelLitres());
        assertEquals(LunarLander.MAX_HIT_POINTS, raceModel.getPlayers()[0].getCurrentLunarLander().getHitPoints());
        assertEquals(0, raceModel.getPlayers()[0].getCurrentLunarLander().getNumCollectedCrates());
        assertEquals(Player.MAX_LUNAR_LANDERS, raceModel.getPlayers()[1].getNumLunarLanders());
        assertNotNull(raceModel.getPlayers()[1].getCurrentLunarLander());
        assertEquals(LunarLander.State.NOT_YET_BORN, raceModel.getPlayers()[1].getCurrentLunarLander().getState());
        assertEquals(LunarLander.MAX_FUEL_LITRES, raceModel.getPlayers()[1].getCurrentLunarLander().getFuelLitres());
        assertEquals(LunarLander.MAX_HIT_POINTS, raceModel.getPlayers()[1].getCurrentLunarLander().getHitPoints());
        assertEquals(0, raceModel.getPlayers()[1].getCurrentLunarLander().getNumCollectedCrates());
    }
}