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

package com.lunargravity.race.view;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.lunargravity.campaign.statemachine.GetReadyState;
import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.widgetsystem.ImageWidget;
import com.lunargravity.engine.widgetsystem.Widget;
import com.lunargravity.engine.widgetsystem.WidgetCreateInfo;
import com.lunargravity.engine.widgetsystem.WidgetManager;
import com.lunargravity.race.controller.IRaceController;
import com.lunargravity.race.model.IRaceModel;
import org.joml.Matrix4f;

import java.io.IOException;

public class RaceView implements
        IRaceView,
        ISceneAssetOwner,
        IRacePausedObserver,
        IRaceResultsObserver
{
    private static final String RACE_RESULTS = "raceResults";
    private static final String RACE_PAUSED = "racePaused";
    private static final String GET_READY = "getReady";
    private static final String RACE_COMPLETED = "raceCompleted";

    private final WidgetManager _widgetManager;
    private final IRaceController _controller;
    private final IRaceModel _model;
    private final DisplayMeshCache _displayMeshCache;
    private final MaterialCache _materialCache;
    private final TextureCache _textureCache;

    private Widget _raceResults;
    private Widget _racePaused;
    private Widget _getReady;
    private Widget _raceCompleted;

    public RaceView(WidgetManager widgetManager, IRaceController controller, IRaceModel model) {
        _widgetManager = widgetManager;
        _controller = controller;
        _model = model;
        _displayMeshCache = new DisplayMeshCache();
        _materialCache = new MaterialCache();
        _textureCache = new TextureCache();
    }

    @Override
    public void initialLoadCompleted() {
        // TODO
    }

    @Override
    public void viewThink() {
        // TODO
    }

    @Override
    public void drawView3d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void drawView2d(Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public DisplayMeshCache getDisplayMeshCache() {
        return _displayMeshCache;
    }

    @Override
    public MaterialCache getMaterialCache() {
        return _materialCache;
    }

    @Override
    public TextureCache getTextureCache() {
        return _textureCache;
    }

    @Override
    public void onFrameEnd() {
        // TODO
    }

    @Override
    public void setupForNewLevel() {
        // TODO
    }

    @Override
    public void freeNativeResources() {
        // TODO
    }

    @Override
    public void displayMeshLoaded(DisplayMesh displayMesh) {
        // TODO
    }

    @Override
    public void collisionMeshLoaded(String name, CollisionShape collisionMesh) {
        // TODO
    }

    @Override
    public void showResultsWidget() throws IOException {
        _widgetManager.hideAll();
        _widgetManager.show(_raceResults, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showPausedWidget() throws IOException {
        _widgetManager.hideAll();
        _widgetManager.show(_racePaused, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showGetReady(int i) throws IOException {
        if (_widgetManager.isVisible(_getReady)) {
            int clamped = Math.min(GetReadyState.MAX_SECONDS, Math.max(GetReadyState.MIN_SECONDS, i));
            _getReady.getObserver().setBackgroundImage(String.format("images/GetReady%d.png", clamped));
        }
        else {
            _widgetManager.hideAll();
            _getReady.getObserver().setBackgroundImage("images/GetReady3.png");
            _widgetManager.show(_getReady, WidgetManager.ShowAs.FIRST);
        }
    }

    @Override
    public void showCompletedWidget() throws IOException {
        _widgetManager.hideAll();
        _widgetManager.show(_raceCompleted, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void showLevelStatusBar() {
        _widgetManager.hideAll();
        // TODO: show the level status bar
    }

    @Override
    public void widgetLoaded(ViewportConfig viewportConfig, WidgetCreateInfo wci) throws IOException {
        if (wci == null) {
            System.out.print("RaceView.widgetLoaded() was passed a null WidgetCreateInfo object");
            return;
        }
        if (wci._id.equals(RACE_RESULTS) && wci._type.equals("RaceResultsWidget")) {
            _raceResults = new Widget(viewportConfig, wci, new RaceResultsWidget(_widgetManager, this));
        }
        else if (wci._id.equals(RACE_PAUSED) && wci._type.equals("RacePausedWidget")) {
            _racePaused = new Widget(viewportConfig, wci, new RacePausedWidget(_widgetManager, this));
        }
        else if (wci._id.equals(RACE_COMPLETED) && wci._type.equals("ImageWidget")) {
            _raceCompleted = new Widget(viewportConfig, wci, new ImageWidget(_widgetManager));
        }
        else if (wci._id.equals(GET_READY) && wci._type.equals("ImageWidget")) {
            _getReady = new Widget(viewportConfig, wci, new ImageWidget(_widgetManager));
        }
    }

    @Override
    public void resumeRaceButtonClicked() {
        _controller.resumeRace();
    }

    @Override
    public void mainMenuButtonClicked() {
        _controller.mainMenuRequested();
    }

    @Override
    public void startNextRaceButtonClicked() {
        _controller.startNextRace();
    }
}
