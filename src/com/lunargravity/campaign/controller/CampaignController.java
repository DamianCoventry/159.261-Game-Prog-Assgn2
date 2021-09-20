package com.lunargravity.campaign.controller;

import com.lunargravity.campaign.model.ICampaignModel;
import com.lunargravity.engine.scene.ISceneLogicOwner;
import org.joml.Vector2f;

import java.util.HashMap;

public class CampaignController implements ICampaignController, ISceneLogicOwner {
    private final ICampaignControllerObserver _observer;
    private final ICampaignModel _model;

    public CampaignController(ICampaignControllerObserver observer, ICampaignModel model) {
        _observer = observer;
        _model = model;
    }

    @Override
    public void onControllerThink() {
        // TODO
    }

    @Override
    public void temp() {
        // TODO
    }

    @Override
    public void onLogicSettingLoaded(String name, String value) {
        // TODO
    }
}
