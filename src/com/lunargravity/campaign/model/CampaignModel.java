package com.lunargravity.campaign.model;

import com.lunargravity.engine.scene.ISceneStateOwner;
import org.joml.Vector2f;

import java.util.HashMap;

public class CampaignModel implements ICampaignModel, ISceneStateOwner {
    private final int _numPlayers;

    public CampaignModel(int numPlayers) {
        _numPlayers = numPlayers;
    }

    @Override
    public String modelToJson() {
        // TODO
        return null;
    }

    @Override
    public void modelFromJson(String json) {
        // TODO
    }

    @Override
    public void temp() {
        // TODO
    }

    @Override
    public void stateSettingLoaded(String name, String value) {
        // TODO
    }
}
