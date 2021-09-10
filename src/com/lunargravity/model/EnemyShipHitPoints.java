package com.lunargravity.model;

public class EnemyShipHitPoints {
    static public int get(EnemyShipType type) {
        switch (type) {
            case IMP: return 100;
            case KRAKEN: return 125;
            case OGRE: return 16;
            case WRAITH: return 200;
        }
        throw new RuntimeException("Unexpected EnemyShipType");
    }
}
