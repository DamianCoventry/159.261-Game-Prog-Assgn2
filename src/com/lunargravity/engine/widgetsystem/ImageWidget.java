package com.lunargravity.engine.widgetsystem;

import org.joml.Matrix4f;

import java.io.IOException;

public class ImageWidget extends WidgetObserver {
    public ImageWidget(WidgetManager widgetManager) {
        super(widgetManager);
    }

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        super.initialiseChildren(wci);
        // anything to do?
    }

    @Override
    public void freeResources() {
        super.freeResources();
        // anything to do?
    }

    @Override
    public void widgetDraw(int viewport, Matrix4f projectionMatrix) {
        super.widgetDraw(viewport, projectionMatrix);
        // anything to do?
    }
}
