//
// Lunar Gravity
//
// This game is based upon the Amiga video game Gravity Force that was
// released in 1989 by Stephan Wenzler
//
// https://www.mobygames.com/game/gravity-force
// https://www.youtube.com/watch?v=m9mFtCvnko8
//
// This implementation is Copyright (c) 2021, Damian Coventry
// All rights reserved
// Written for Massey University course 159.261 Game Programming (Assignment 2)
//

package com.lunargravity.menu.view;

import com.lunargravity.application.LargeNumberFont;
import com.lunargravity.engine.core.IEngine;
import com.lunargravity.engine.core.IManualFrameUpdater;
import com.lunargravity.engine.graphics.ViewportConfig;
import com.lunargravity.engine.scene.ISceneBuilderObserver;
import com.lunargravity.engine.widgetsystem.ImageWidget;
import com.lunargravity.engine.widgetsystem.Widget;
import com.lunargravity.engine.widgetsystem.WidgetCreateInfo;
import com.lunargravity.engine.widgetsystem.WidgetManager;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.IOException;
import java.util.HashMap;

public class MenuBuilderObserver implements ISceneBuilderObserver {
    private static final float LABEL_X_OFFSET = 600.0f;
    private static final float LABEL_Y_OFFSET = 435.0f;

    private final IManualFrameUpdater _manualFrameUpdater;
    private final WidgetManager _widgetManager;
    private final Widget _backgroundImage;
    private final LargeNumberFont _font;
    private static final Vector4f WHITE = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    public MenuBuilderObserver(IEngine engine, IManualFrameUpdater manualFrameUpdater) throws IOException {
        _manualFrameUpdater = manualFrameUpdater;
        _widgetManager = new WidgetManager(engine);
        _backgroundImage = createBackgroundImageWidget();
        _font = new LargeNumberFont(engine.getRenderer());
    }

    private Widget createBackgroundImageWidget() throws IOException {
        WidgetCreateInfo wci = new WidgetCreateInfo(ImageWidget.BACKGROUND_IMAGE, "ImageWidget");
        wci._position = new Vector2f(0, 0);
        wci._size = _manualFrameUpdater.getOrthographicViewportSize();
        wci._properties = new HashMap<>();
        wci._properties.put(ImageWidget.BACKGROUND_IMAGE, "images/LoadingMenu.png");
        wci._properties.put(Widget.INITIAL_POSITION, Widget.FULL_VIEWPORT);

        ViewportConfig viewportConfig = new ViewportConfig();
        viewportConfig._viewportIndex = 0; // TODO: this is temporary
        viewportConfig._positionX = viewportConfig._positionY = 0;
        viewportConfig._width = (int)_manualFrameUpdater.getOrthographicViewportSize().x;
        viewportConfig._height = (int)_manualFrameUpdater.getOrthographicViewportSize().y;

        return new Widget(viewportConfig, wci, new ImageWidget(_widgetManager));
    }

    @Override
    public void freeNativeResources() {
        _widgetManager.freeNativeResources();
        _backgroundImage.getObserver().freeNativeResources();
        _font.freeNativeResources();
    }

    @Override
    public void sceneBuildBeginning() throws IOException {
        _widgetManager.open(_backgroundImage, WidgetManager.OpenAs.FIRST, WidgetManager.AlreadyOpenBehaviour.BRING_TO_FRONT);
        _widgetManager.show(_backgroundImage, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void sceneBuildEnded() {
        _widgetManager.hideAll();
    }

    @Override
    public void sceneBuildProgressed(int currentItem, int totalItems) {
        _manualFrameUpdater.prepareNewFrame();

        //Matrix4f perspectiveProjectionMatrix = _manualFrameUpdater.getPerspectiveProjectionMatrix();
        // TODO: draw 3d items

//        Matrix4f projectionMatrix = _manualFrameUpdater.getOrthographicProjectionMatrix();
//
//        _widgetManager.draw(projectionMatrix);
//
//        if (totalItems > 0) {
//            _font.drawPercentage(projectionMatrix, currentItem * 100L / totalItems, LABEL_X_OFFSET, LABEL_Y_OFFSET, 1.0f, WHITE);
//        }

        _manualFrameUpdater.submitFrame();
    }
}
