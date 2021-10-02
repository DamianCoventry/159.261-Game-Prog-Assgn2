package com.lunargravity.world.controller;

public interface IGameWorldController extends IWorldController {
    void playerStartThrust(int player);
    void playerStartRotateCcw(int player);
    void playerStartRotateCw(int player);
    void playerStartShoot(int player);
    void playerStopThrust(int i);
    void playerStopRotateCcw(int i);
    void playerStopRotateCw(int i);
    void playerStopShoot(int i);
    void playerKick(int player);
}
