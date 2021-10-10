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

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.widgetsystem.Widget;
import com.lunargravity.engine.widgetsystem.WidgetCreateInfo;
import com.lunargravity.engine.widgetsystem.WidgetManager;
import com.lunargravity.menu.controller.IMenuController;
import com.lunargravity.menu.model.IMenuModel;
import org.joml.Matrix4f;

import java.io.IOException;

public class MenuView implements
        IMenuView,
        ISceneAssetOwner,
        IMainWidgetObserver,
        ICampaignWidgetObserver,
        IOptionsWidgetObserver,
        IRaceScoreboardObserver,
        IDogfightScoreboardObserver
{
    private static final String MAIN = "main";
    private static final String CAMPAIGN = "campaign";
    private static final String OPTIONS = "options";
    private static final String RACE_SCOREBOARD = "raceScoreboard";
    private static final String DOGFIGHT_SCOREBOARD = "dogfightScoreboard";

    private final WidgetManager _widgetManager;
    private final IMenuController _controller;
    private final IMenuModel _model;
    private final DisplayMeshCache _displayMeshCache;
    private final MaterialCache _materialCache;
    private final TextureCache _textureCache;

    private Widget _main;
    private Widget _campaign;
    private Widget _options;
    private Widget _raceScoreboard;
    private Widget _dogfightScoreboard;

    public MenuView(WidgetManager widgetManager, IMenuController controller, IMenuModel model) {
        _widgetManager = widgetManager;
        _controller = controller;
        _model = model;
        _displayMeshCache = new DisplayMeshCache();
        _materialCache = new MaterialCache();
        _textureCache = new TextureCache();
    }

    @Override
    public void initialLoadCompleted() {
        // Nothing to do
    }

    @Override
    public void showMainWidget() throws IOException {
        _widgetManager.show(_main, WidgetManager.ShowAs.FIRST);
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
    public void widgetLoaded(ViewportConfig viewportConfig, WidgetCreateInfo wci) throws IOException {
        if (wci == null) {
            System.out.print("MenuView.widgetLoaded() was passed a null WidgetCreateInfo object");
            return;
        }
        if (wci._id.equals(MAIN) && wci._type.equals("MainWidget")) {
            _main = new Widget(viewportConfig, wci, new MainWidget(_widgetManager, this));
        }
        else if (wci._id.equals(CAMPAIGN) && wci._type.equals("CampaignWidget")) {
            _campaign = new Widget(viewportConfig, wci, new CampaignWidget(_widgetManager, this));
        }
        else if (wci._id.equals(OPTIONS) && wci._type.equals("OptionsWidget")) {
            _options = new Widget(viewportConfig, wci, new OptionsWidget(_widgetManager, this, _controller.getPlayerInputBindings()));
        }
        else if (wci._id.equals(RACE_SCOREBOARD) && wci._type.equals("RaceScoreboardWidget")) {
            _raceScoreboard = new Widget(viewportConfig, wci, new RaceScoreboardWidget(_widgetManager, this));
        }
        else if (wci._id.equals(DOGFIGHT_SCOREBOARD) && wci._type.equals("DogfightScoreboardWidget")) {
            _dogfightScoreboard = new Widget(viewportConfig, wci, new DogfightScoreboardWidget(_widgetManager, this));
        }
    }

    @Override
    public void campaignGameButtonClicked() {
        _widgetManager.hideAll();
        //_widgetManager.show(_campaign, WidgetManager.ShowAs.FIRST);
        _controller.startNewCampaign(1); // this changes the application's current state
    }

    @Override
    public void raceGameButtonClicked() throws IOException {
        _widgetManager.hideAll();
        _widgetManager.show(_raceScoreboard, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void dogfightGameButtonClicked() throws IOException {
        _widgetManager.hideAll();
        _widgetManager.show(_dogfightScoreboard, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void optionsButtonClicked() throws IOException {
        _widgetManager.hideAll();
        _widgetManager.show(_options, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void exitApplicationButtonClicked() {
        _controller.exitApplication();
    }

    @Override
    public void singlePlayerCampaignButtonClicked() {
        _controller.startNewCampaign(1); // this changes the application's current state
    }

    @Override
    public void twoPlayersCampaignButtonClicked() {
        _controller.startNewCampaign(2); // this changes the application's current state
    }

    @Override
    public void loadSavedCampaignButtonClicked(String fileName) {
        _controller.loadExistingCampaign(fileName); // this changes the application's current state
    }

    @Override
    public void resetRaceScoreboardButtonClicked() {
        _controller.resetRaceScoreboard();
    }

    @Override
    public void startSinglePlayerRaceButtonClicked() {
        _controller.startNewRace(1);
    }

    @Override
    public void startTwoPlayersRaceButtonClicked() {
        _controller.startNewRace(2);
    }

    @Override
    public void resetDogfightScoreboardButtonClicked() {
        _controller.resetDogfightScoreboard();
    }

    @Override
    public void startSinglePlayerDogfightButtonClicked() {
        _controller.startNewDogfight(1);
    }

    @Override
    public void startTwoPlayersDogfightButtonClicked() {
        _controller.startNewDogfight(2);
    }

    @Override
    public void mainMenuButtonClicked() throws IOException {
        _widgetManager.hideAll();
        _widgetManager.show(_main, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void enableSoundCheckboxSet() {
        _controller.enableSound();
    }

    @Override
    public void enableSoundCheckboxCleared() {
        _controller.disableSound();
    }

    @Override
    public void soundVolumeNumericChanged(int volume) {
        _controller.setSoundVolume(volume);
    }

    @Override
    public void enableMusicCheckboxSet() {
        _controller.enableMusic();
    }

    @Override
    public void enableMusicCheckboxCleared() {
        _controller.disableMusic();
    }

    @Override
    public void musicVolumeNumericChanged(int volume) {
        _controller.setMusicVolume(volume);
    }

    @Override
    public void playerKeyBindingChanged(int player, Binding binding, int key) {
        switch (binding) {
            case ROTATE_CW -> _controller.setPlayerRotateRightKey(player, key);
            case ROTATE_CCW -> _controller.setPlayerRotateLeftKey(player, key);
            case THRUST -> _controller.setPlayerThrustKey(player, key);
            case KICK -> _controller.setPlayerKickKey(player, key);
            case SHOOT -> _controller.setPlayerShootKey(player, key);
        }
    }

    @Override
    public void setDefaultPlayerKeysButtonClicked() {
        _controller.setDefaultPlayerKeys();
    }
}
