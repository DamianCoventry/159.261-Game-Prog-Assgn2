package com.lunargravity.campaign.view;

import com.lunargravity.campaign.model.ICampaignModel;
import org.joml.Matrix4f;

public class CampaignView implements ICampaignView {
    private final ICampaignModel _model;

    public CampaignView(ICampaignModel model) {
        _model = model;
    }

    @Override
    public void think() {
        // TODO
    }

    @Override
    public void draw3d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void draw2d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void temp() {
        // TODO
    }
}
