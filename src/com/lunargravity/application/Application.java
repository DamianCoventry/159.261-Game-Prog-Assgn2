package com.lunargravity.application;

import com.lunargravity.controller.*;
import com.lunargravity.model.*;
import com.lunargravity.view.*;

public class Application implements IContext, IControllerEvents {
    private final IModel _model;
    private final IView _view;
    private final IController _controller;

    public Application() {
        final LevelInfo levelInfo = new LevelInfo();
        levelInfo.setNumPlayers(2);
        levelInfo.setNumCrates(4);
        levelInfo.setNumDeliveryZones(3);
        levelInfo.setEnemyShips(new EnemyShipType[] { EnemyShipType.IMP, EnemyShipType.KRAKEN, EnemyShipType.OGRE });
        levelInfo.setFuelCanisterLitres(new int[] { 85, 80 });
        levelInfo.setRepairGarageCapacities(new int[] { 50 });

        _model = new CampaignModel(levelInfo);
        var json = _model.toJson();

        _view = new View(_model);

        _controller = new Controller(this, _view);
    }

    @Override
    public void changeState(IState state) {
        // TODO
    }

    @Override
    public void TempControllerEvents() {
        // TODO
    }

    void run() {
        // TODO
    }

    public static void main(String[] args) {
        Application app;
        try {
            app = new Application();
            app.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
//        finally {
//            if (app != null) {
//                app.freeNativeResources(); // ensure release of OpenGL resources
//            }
//        }
    }
}
