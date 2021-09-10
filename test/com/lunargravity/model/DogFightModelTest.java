package com.lunargravity.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DogFightModelTest {

    @Test
    void modelWillNotCreateWithNullArgument() {
        assertThrows(IllegalArgumentException.class, () -> new DogFightModel(null));
    }

    @Test
    void modelWillNotCreateWithInvalidNumPlayers() {
        LevelInfo levelInfo = new LevelInfo();
        levelInfo.setNumPlayers(0);
        assertThrows(IllegalArgumentException.class, () -> new DogFightModel(levelInfo));
    }

    @Test
    void modelCreatesProperly() {
        LevelInfo levelInfo = new LevelInfo();
        levelInfo.setNumPlayers(1);
        DogFightModel dogFightModel = new DogFightModel(levelInfo);
        assertNotNull(dogFightModel);
    }

    @Test
    void fromJsonProducesCorrectObject() {
        final String json =
                """
                {
                    "_numPlayers": 2
                }""";

        final DogFightModel dogFightModel = DogFightModel.fromJson(json);
        assertEquals(2, dogFightModel.getPlayers().length);
        assertEquals(0, dogFightModel.getCrates().length);
        assertEquals(0, dogFightModel.getDeliveryZones().length);
        assertNull(dogFightModel.getEnemyShips());
        assertNull(dogFightModel.getFuelCanisters());
        assertNull(dogFightModel.getRepairGarages());
        assertNotNull(dogFightModel.getEnergyBeams());
        assertNotNull(dogFightModel.getPlasmas());
        assertNotNull(dogFightModel.getRockets());
        assertNotNull(dogFightModel.getExplosions());
        assertNotNull(dogFightModel.getWeapons());
    }

    @Test
    void toJsonProducesExpectedJson1() {
        final LevelInfo levelInfo = new LevelInfo();
        levelInfo.setNumPlayers(2);

        final DogFightModel dogFightModel = new DogFightModel(levelInfo);
        final String actual = dogFightModel.toJson();
        final String expected = "{\"_numPlayers\":2,\"_numCrates\":0,\"_numDeliveryZones\":0,\"_energyBeamStrengths\":[],\"_weaponTypes\":[]}";
        assertEquals(expected, actual);
    }

    @Test
    void toJsonProducesExpectedJson2() {
        final LevelInfo levelInfo = new LevelInfo();
        levelInfo.setNumPlayers(2);
        levelInfo.setEnergyBeamStrengths(new int[] { 5, 6, 7 });

        final DogFightModel dogFightModel = new DogFightModel(levelInfo);
        final String actual = dogFightModel.toJson();
        final String expected = "{\"_numPlayers\":2,\"_numCrates\":0,\"_numDeliveryZones\":0,\"_energyBeamStrengths\":[5,6,7],\"_weaponTypes\":[]}";
        assertEquals(expected, actual);
    }

    @Test
    void toJsonProducesExpectedJson3() {
        final LevelInfo levelInfo = new LevelInfo();
        levelInfo.setNumPlayers(2);
        levelInfo.setWeaponTypes(new WeaponType[] { WeaponType.RAIL_GUN, WeaponType.RAIL_GUN, WeaponType.PLASMA_GUN });

        final DogFightModel dogFightModel = new DogFightModel(levelInfo);
        final String actual = dogFightModel.toJson();
        final String expected = "{\"_numPlayers\":2,\"_numCrates\":0,\"_numDeliveryZones\":0,\"_energyBeamStrengths\":[],\"_weaponTypes\":[\"RAIL_GUN\",\"RAIL_GUN\",\"PLASMA_GUN\"]}";
        assertEquals(expected, actual);
    }

    @Test
    void toJsonProducesExpectedJson4() {
        final LevelInfo levelInfo = new LevelInfo();
        levelInfo.setNumPlayers(2);
        levelInfo.setEnemyShips(new EnemyShipType[] { EnemyShipType.WRAITH, EnemyShipType.KRAKEN, EnemyShipType.OGRE, EnemyShipType.KRAKEN });

        final DogFightModel dogFightModel = new DogFightModel(levelInfo);
        final String actual = dogFightModel.toJson();
        final String expected = "{\"_numPlayers\":2,\"_numCrates\":0,\"_numDeliveryZones\":0,\"_enemyShipTypes\":[\"WRAITH\",\"KRAKEN\",\"OGRE\",\"KRAKEN\"],\"_energyBeamStrengths\":[],\"_weaponTypes\":[]}";
        assertEquals(expected, actual);
    }

    @Test
    void getPlayersReturnsExpectedObjects() {
        final String json =
                """
                {
                    "_numPlayers": 2
                }""";

        final DogFightModel dogFightModel = DogFightModel.fromJson(json);
        assertEquals(2, dogFightModel.getPlayers().length);
        assertEquals(Player.MAX_LUNAR_LANDERS, dogFightModel.getPlayers()[0].getNumLunarLanders());
        assertNotNull(dogFightModel.getPlayers()[0].getCurrentLunarLander());
        assertEquals(LunarLander.State.NOT_YET_BORN, dogFightModel.getPlayers()[0].getCurrentLunarLander().getState());
        assertEquals(LunarLander.MAX_FUEL_LITRES, dogFightModel.getPlayers()[0].getCurrentLunarLander().getFuelLitres());
        assertEquals(LunarLander.MAX_HIT_POINTS, dogFightModel.getPlayers()[0].getCurrentLunarLander().getHitPoints());
        assertEquals(0, dogFightModel.getPlayers()[0].getCurrentLunarLander().getNumCollectedCrates());
        assertEquals(Player.MAX_LUNAR_LANDERS, dogFightModel.getPlayers()[1].getNumLunarLanders());
        assertNotNull(dogFightModel.getPlayers()[1].getCurrentLunarLander());
        assertEquals(LunarLander.State.NOT_YET_BORN, dogFightModel.getPlayers()[1].getCurrentLunarLander().getState());
        assertEquals(LunarLander.MAX_FUEL_LITRES, dogFightModel.getPlayers()[1].getCurrentLunarLander().getFuelLitres());
        assertEquals(LunarLander.MAX_HIT_POINTS, dogFightModel.getPlayers()[1].getCurrentLunarLander().getHitPoints());
        assertEquals(0, dogFightModel.getPlayers()[1].getCurrentLunarLander().getNumCollectedCrates());
    }
}