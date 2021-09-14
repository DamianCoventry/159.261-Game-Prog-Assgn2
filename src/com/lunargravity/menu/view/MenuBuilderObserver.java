package com.lunargravity.menu.view;

import com.lunargravity.engine.core.IManualFrameUpdater;
import com.lunargravity.engine.scene.ISceneBuilderObserver;
import org.joml.Matrix4f;

public class MenuBuilderObserver implements ISceneBuilderObserver {
    private final IManualFrameUpdater _manualFrameUpdater;

    public MenuBuilderObserver(IManualFrameUpdater manualFrameUpdater) {
        _manualFrameUpdater = manualFrameUpdater;
    }

    @Override
    public void onSceneBuildBeginning() {
        // Anything to do here?
    }

    @Override
    public void onSceneBuildEnded() {
        // Anything to do here?
    }

    @Override
    public void onSceneBuildProgressed(int currentItem, int totalItems) {
        _manualFrameUpdater.prepareNewFrame();

        Matrix4f perspectiveProjectionMatrix = _manualFrameUpdater.getPerspectiveProjectionMatrix();
        // TODO: draw 3d items

        Matrix4f orthographicProjectionMatrix = _manualFrameUpdater.getOrthographicProjectionMatrix();
        // TODO: draw 2d items

        _manualFrameUpdater.submitFrame();
    }
}
