package com.lunargravity.campaign.controller;

import com.lunargravity.campaign.model.ICampaignModel;

public class CampaignController implements ICampaignController {
    private final ICampaignControllerEvents _eventHandler;
    private final ICampaignModel _model;

    public CampaignController(ICampaignControllerEvents eventHandler, ICampaignModel model) {
        _eventHandler = eventHandler;
        _model = model;
    }

    @Override
    public void think() {

    }

    @Override
    public void temp() {

    }
}
