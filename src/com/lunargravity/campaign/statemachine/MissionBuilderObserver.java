package com.lunargravity.campaign.statemachine;

import com.lunargravity.engine.core.IManualFrameUpdater;
import com.lunargravity.engine.scene.ISceneBuilderObserver;
import org.joml.Matrix4f;

public class MissionBuilderObserver implements ISceneBuilderObserver {
    private final IManualFrameUpdater _manualFrameUpdater;

    public MissionBuilderObserver(IManualFrameUpdater manualFrameUpdater) {
        _manualFrameUpdater = manualFrameUpdater;
    }

    @Override
    public void freeResources() {
        // TODO
    }

    @Override
    public void sceneBuildBeginning() {
        // Anything to do here?
    }

    @Override
    public void sceneBuildEnded() {
        // Anything to do here?
    }

    @Override
    public void sceneBuildProgressed(int currentItem, int totalItems) {
        _manualFrameUpdater.prepareNewFrame();

        Matrix4f perspectiveProjectionMatrix = _manualFrameUpdater.getPerspectiveProjectionMatrix();
        // TODO: draw 3d items

        Matrix4f orthographicProjectionMatrix = _manualFrameUpdater.getOrthographicProjectionMatrix();
        // TODO: draw 2d items

        _manualFrameUpdater.submitFrame();
    }
}
