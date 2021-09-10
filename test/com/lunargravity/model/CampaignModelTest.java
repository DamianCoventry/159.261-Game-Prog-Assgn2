package com.lunargravity.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CampaignModelTest {

    @Test
    void modelWillNotCreateWithNullArgument() {
        assertThrows(IllegalArgumentException.class, () -> new CampaignModel(null));
    }

    @Test
    void modelWillNotCreateWithInvalidNumPlayers() {
        LevelInfo levelInfo = new LevelInfo();
        levelInfo.setNumPlayers(0);
        levelInfo.setNumCrates(1);
        levelInfo.setNumDeliveryZones(1);
        assertThrows(IllegalArgumentException.class, () -> new CampaignModel(levelInfo));
    }

    @Test
    void modelWillNotCreateWithInvalidNumCrates() {
        LevelInfo levelInfo = new LevelInfo();
        levelInfo.setNumPlayers(1);
        levelInfo.setNumCrates(0);
        levelInfo.setNumDeliveryZones(1);
        assertThrows(IllegalArgumentException.class, () -> new CampaignModel(levelInfo));
    }

    @Test
    void modelWillNotCreateWithInvalidNumDeliveryZones() {
        LevelInfo levelInfo = new LevelInfo();
        levelInfo.setNumPlayers(1);
        levelInfo.setNumCrates(1);
        levelInfo.setNumDeliveryZones(0);
        assertThrows(IllegalArgumentException.class, () -> new CampaignModel(levelInfo));
    }

    @Test
    void modelCreatesProperly() {
        LevelInfo levelInfo = new LevelInfo();
        levelInfo.setNumPlayers(1);
        levelInfo.setNumCrates(1);
        levelInfo.setNumDeliveryZones(1);
        CampaignModel campaignModel = new CampaignModel(levelInfo);
        assertNotNull(campaignModel);
    }

    @Test
    void fromJsonProducesCorrectObject() {
        final String json =
                """
                {
                    "_numPlayers": 2,
                    "_numCrates": 4,
                    "_numDeliveryZones": 3,
                    "_enemyShipTypes": [
                        "IMP",
                        "KRAKEN",
                        "OGRE"
                    ],
                    "_fuelCanisterLitres": [
                        85,
                        80
                    ],
                    "_repairGarageCapacities": [
                        50
                    ]
                }""";

        final CampaignModel campaignModel = CampaignModel.fromJson(json);
        assertEquals(2, campaignModel.getPlayers().length);
        assertEquals(4, campaignModel.getCrates().length);
        assertEquals(3, campaignModel.getDeliveryZones().length);
        assertEquals(3, campaignModel.getEnemyShips().length);
        assertEquals(EnemyShipType.IMP, campaignModel.getEnemyShips()[0].getType());
        assertEquals(EnemyShipType.KRAKEN, campaignModel.getEnemyShips()[1].getType());
        assertEquals(EnemyShipType.OGRE, campaignModel.getEnemyShips()[2].getType());
        assertEquals(2, campaignModel.getFuelCanisters().length);
        assertEquals(85, campaignModel.getFuelCanisters()[0].getStartingLitres());
        assertEquals(85, campaignModel.getFuelCanisters()[0].getRemainingLitres());
        assertEquals(80, campaignModel.getFuelCanisters()[1].getStartingLitres());
        assertEquals(80, campaignModel.getFuelCanisters()[1].getRemainingLitres());
        assertEquals(1, campaignModel.getRepairGarages().length);
        assertEquals(50, campaignModel.getRepairGarages()[0].getStartingCapacity());
        assertEquals(50, campaignModel.getRepairGarages()[0].getRemainingCapacity());
        assertNotNull(campaignModel.getEnergyBeams());
        assertNotNull(campaignModel.getPlasmas());
        assertNotNull(campaignModel.getRockets());
        assertNotNull(campaignModel.getExplosions());
    }

    @Test
    void toJsonProducesExpectedJson1() {
        final LevelInfo levelInfo = new LevelInfo();
        levelInfo.setNumPlayers(2);
        levelInfo.setNumCrates(4);
        levelInfo.setNumDeliveryZones(3);
        levelInfo.setEnemyShips(new EnemyShipType[] { EnemyShipType.IMP, EnemyShipType.KRAKEN, EnemyShipType.OGRE });
        levelInfo.setFuelCanisterLitres(new int[] { 85, 80 });
        levelInfo.setRepairGarageCapacities(new int[] { 50 });
        levelInfo.setEnergyBeamStrengths(new int[] { 12, 34, 56, 78 });

        final CampaignModel campaignModel = new CampaignModel(levelInfo);
        final String actual = campaignModel.toJson();
        final String expected = "{\"_numPlayers\":2,\"_numCrates\":4,\"_numDeliveryZones\":3,\"_enemyShipTypes\":[\"IMP\",\"KRAKEN\",\"OGRE\"],\"_fuelCanisterLitres\":[85,80],\"_repairGarageCapacities\":[50],\"_energyBeamStrengths\":[12,34,56,78],\"_weaponTypes\":[]}";
        assertEquals(expected, actual);
    }

    @Test
    void toJsonProducesExpectedJson2() {
        final LevelInfo levelInfo = new LevelInfo();
        levelInfo.setNumPlayers(2);
        levelInfo.setNumCrates(4);
        levelInfo.setNumDeliveryZones(3);

        final CampaignModel campaignModel = new CampaignModel(levelInfo);
        final String actual = campaignModel.toJson();
        final String expected = "{\"_numPlayers\":2,\"_numCrates\":4,\"_numDeliveryZones\":3,\"_energyBeamStrengths\":[],\"_weaponTypes\":[]}";
        assertEquals(expected, actual);
    }

    @Test
    void getPlayersReturnsExpectedObjects() {
        final String json =
                """
                {
                    "_numPlayers": 2,
                    "_numCrates": 4,
                    "_numDeliveryZones": 3,
                    "_enemyShipTypes": [
                        "IMP",
                        "KRAKEN",
                        "OGRE"
                    ],
                    "_fuelCanisterLitres": [
                        85,
                        80
                    ],
                    "_repairGarageCapacities": [
                        50
                    ]
                }""";

        final CampaignModel campaignModel = CampaignModel.fromJson(json);
        assertEquals(2, campaignModel.getPlayers().length);
        assertEquals(Player.MAX_LUNAR_LANDERS, campaignModel.getPlayers()[0].getNumLunarLanders());
        assertNotNull(campaignModel.getPlayers()[0].getCurrentLunarLander());
        assertEquals(LunarLander.State.NOT_YET_BORN, campaignModel.getPlayers()[0].getCurrentLunarLander().getState());
        assertEquals(LunarLander.MAX_FUEL_LITRES, campaignModel.getPlayers()[0].getCurrentLunarLander().getFuelLitres());
        assertEquals(LunarLander.MAX_HIT_POINTS, campaignModel.getPlayers()[0].getCurrentLunarLander().getHitPoints());
        assertEquals(0, campaignModel.getPlayers()[0].getCurrentLunarLander().getNumCollectedCrates());
        assertEquals(Player.MAX_LUNAR_LANDERS, campaignModel.getPlayers()[1].getNumLunarLanders());
        assertNotNull(campaignModel.getPlayers()[1].getCurrentLunarLander());
        assertEquals(LunarLander.State.NOT_YET_BORN, campaignModel.getPlayers()[1].getCurrentLunarLander().getState());
        assertEquals(LunarLander.MAX_FUEL_LITRES, campaignModel.getPlayers()[1].getCurrentLunarLander().getFuelLitres());
        assertEquals(LunarLander.MAX_HIT_POINTS, campaignModel.getPlayers()[1].getCurrentLunarLander().getHitPoints());
        assertEquals(0, campaignModel.getPlayers()[1].getCurrentLunarLander().getNumCollectedCrates());
    }

    @Test
    void getCratesReturnsExpectedObjects() {
        final String json =
                """
                {
                    "_numPlayers": 2,
                    "_numCrates": 4,
                    "_numDeliveryZones": 3,
                    "_enemyShipTypes": [
                        "IMP",
                        "KRAKEN",
                        "OGRE"
                    ],
                    "_fuelCanisterLitres": [
                        85,
                        80
                    ],
                    "_repairGarageCapacities": [
                        50
                    ]
                }""";

        final CampaignModel campaignModel = CampaignModel.fromJson(json);
        assertEquals(4, campaignModel.getCrates().length);
        assertEquals(Crate.State.IDLE, campaignModel.getCrates()[0].getState());
        assertEquals(Crate.State.IDLE, campaignModel.getCrates()[1].getState());
        assertEquals(Crate.State.IDLE, campaignModel.getCrates()[2].getState());
        assertEquals(Crate.State.IDLE, campaignModel.getCrates()[3].getState());
    }

    @Test
    void getDeliveryZonesReturnsExpectedObjects() {
        final String json =
                """
                {
                    "_numPlayers": 2,
                    "_numCrates": 4,
                    "_numDeliveryZones": 3,
                    "_enemyShipTypes": [
                        "IMP",
                        "KRAKEN",
                        "OGRE"
                    ],
                    "_fuelCanisterLitres": [
                        85,
                        80
                    ],
                    "_repairGarageCapacities": [
                        50
                    ]
                }""";

        final CampaignModel campaignModel = CampaignModel.fromJson(json);
        assertEquals(3, campaignModel.getDeliveryZones().length);
        assertEquals(DeliveryZone.State.IDLE, campaignModel.getDeliveryZones()[0].getState());
        assertEquals(DeliveryZone.State.IDLE, campaignModel.getDeliveryZones()[1].getState());
        assertEquals(DeliveryZone.State.IDLE, campaignModel.getDeliveryZones()[2].getState());
    }

    @Test
    void getEnemyShipsReturnsExpectedObjects() {
        final String json =
                """
                {
                    "_numPlayers": 2,
                    "_numCrates": 4,
                    "_numDeliveryZones": 3,
                    "_enemyShipTypes": [
                        "IMP",
                        "KRAKEN",
                        "OGRE",
                        "WRAITH"
                    ],
                    "_fuelCanisterLitres": [
                        85,
                        80
                    ],
                    "_repairGarageCapacities": [
                        50
                    ]
                }""";

        final CampaignModel campaignModel = CampaignModel.fromJson(json);
        assertEquals(4, campaignModel.getEnemyShips().length);
        assertEquals(EnemyShipType.IMP, campaignModel.getEnemyShips()[0].getType());
        assertEquals(EnemyShip.State.NOT_YET_BORN, campaignModel.getEnemyShips()[0].getState());
        assertEquals(EnemyShipHitPoints.get(EnemyShipType.IMP), campaignModel.getEnemyShips()[0].getHitPoints());
        assertEquals(EnemyShipType.KRAKEN, campaignModel.getEnemyShips()[1].getType());
        assertEquals(EnemyShip.State.NOT_YET_BORN, campaignModel.getEnemyShips()[1].getState());
        assertEquals(EnemyShipHitPoints.get(EnemyShipType.KRAKEN), campaignModel.getEnemyShips()[1].getHitPoints());
        assertEquals(EnemyShipType.OGRE, campaignModel.getEnemyShips()[2].getType());
        assertEquals(EnemyShip.State.NOT_YET_BORN, campaignModel.getEnemyShips()[2].getState());
        assertEquals(EnemyShipHitPoints.get(EnemyShipType.OGRE), campaignModel.getEnemyShips()[2].getHitPoints());
        assertEquals(EnemyShipType.WRAITH, campaignModel.getEnemyShips()[3].getType());
        assertEquals(EnemyShip.State.NOT_YET_BORN, campaignModel.getEnemyShips()[3].getState());
        assertEquals(EnemyShipHitPoints.get(EnemyShipType.WRAITH), campaignModel.getEnemyShips()[3].getHitPoints());
    }

    @Test
    void getFuelCanistersReturnsExpectedObjects() {
        final String json =
                """
                {
                    "_numPlayers": 2,
                    "_numCrates": 4,
                    "_numDeliveryZones": 3,
                    "_enemyShipTypes": [
                        "IMP",
                        "KRAKEN",
                        "OGRE"
                    ],
                    "_fuelCanisterLitres": [
                        34,
                        56,
                        45
                    ],
                    "_repairGarageCapacities": [
                        50
                    ]
                }""";

        final CampaignModel campaignModel = CampaignModel.fromJson(json);
        assertEquals(3, campaignModel.getFuelCanisters().length);
        assertEquals(34, campaignModel.getFuelCanisters()[0].getStartingLitres());
        assertEquals(34, campaignModel.getFuelCanisters()[0].getRemainingLitres());
        assertEquals(56, campaignModel.getFuelCanisters()[1].getStartingLitres());
        assertEquals(56, campaignModel.getFuelCanisters()[1].getRemainingLitres());
        assertEquals(45, campaignModel.getFuelCanisters()[2].getStartingLitres());
        assertEquals(45, campaignModel.getFuelCanisters()[2].getRemainingLitres());
    }

    @Test
    void getRepairGaragesReturnsExpectedObjects() {
        final String json =
                """
                {
                    "_numPlayers": 2,
                    "_numCrates": 4,
                    "_numDeliveryZones": 3,
                    "_enemyShipTypes": [
                        "IMP",
                        "KRAKEN",
                        "OGRE"
                    ],
                    "_fuelCanisterLitres": [
                        85,
                        80
                    ],
                    "_repairGarageCapacities": [
                        50,
                        88,
                        34,
                        91
                    ]
                }""";

        final CampaignModel campaignModel = CampaignModel.fromJson(json);
        assertEquals(4, campaignModel.getRepairGarages().length);
        assertEquals(50, campaignModel.getRepairGarages()[0].getStartingCapacity());
        assertEquals(50, campaignModel.getRepairGarages()[0].getRemainingCapacity());
        assertEquals(88, campaignModel.getRepairGarages()[1].getStartingCapacity());
        assertEquals(88, campaignModel.getRepairGarages()[1].getRemainingCapacity());
        assertEquals(34, campaignModel.getRepairGarages()[2].getStartingCapacity());
        assertEquals(34, campaignModel.getRepairGarages()[2].getRemainingCapacity());
        assertEquals(91, campaignModel.getRepairGarages()[3].getStartingCapacity());
        assertEquals(91, campaignModel.getRepairGarages()[3].getRemainingCapacity());
    }
}