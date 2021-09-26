package com.lunargravity.menu.view;

import com.lunargravity.engine.graphics.GlMaterial;
import com.lunargravity.engine.graphics.GlStaticMesh;
import com.lunargravity.engine.graphics.GlTexture;
import com.lunargravity.engine.graphics.Transform;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.widgetsystem.Widget;
import com.lunargravity.engine.widgetsystem.WidgetCreateInfo;
import com.lunargravity.engine.widgetsystem.WidgetManager;
import com.lunargravity.menu.controller.IMenuController;
import com.lunargravity.menu.model.IMenuModel;
import com.lunargravity.mvc.IView;
import org.joml.Matrix4f;

import java.io.IOException;

public class MenuView implements
        IView,
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

    private Widget _main;
    private Widget _campaign;
    private Widget _options;
    private Widget _raceScoreboard;
    private Widget _dogfightScoreboard;

    public MenuView(WidgetManager widgetManager, IMenuController controller, IMenuModel model) {
        _widgetManager = widgetManager;
        _controller = controller;
        _model = model;
    }

    @Override
    public void initialLoadCompleted() {
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
    public void drawView2d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void objectLoaded(String name, String type, Transform transform) {
        // TODO
    }

    @Override
    public void staticMeshLoaded(GlStaticMesh staticMesh) {
        // TODO
    }

    @Override
    public void materialLoaded(GlMaterial material) {
        // TODO
    }

    @Override
    public void textureLoaded(GlTexture texture) {
        // TODO
    }

    @Override
    public void widgetLoaded(WidgetCreateInfo wci) throws IOException {
        if (wci == null) {
            System.out.print("MenuView.widgetLoaded() was passed a null WidgetCreateInfo object");
            return;
        }
        if (wci._id.equals(MAIN) && wci._type.equals("MainWidget")) {
            _main = new Widget(wci, new MainWidget(_widgetManager, this));
        }
        else if (wci._id.equals(CAMPAIGN) && wci._type.equals("CampaignWidget")) {
            _campaign = new Widget(wci, new CampaignWidget(_widgetManager, this));
        }
        else if (wci._id.equals(OPTIONS) && wci._type.equals("OptionsWidget")) {
            _options = new Widget(wci, new OptionsWidget(_widgetManager, this));
        }
        else if (wci._id.equals(RACE_SCOREBOARD) && wci._type.equals("RaceScoreboardWidget")) {
            _raceScoreboard = new Widget(wci, new RaceScoreboardWidget(_widgetManager, this));
        }
        else if (wci._id.equals(DOGFIGHT_SCOREBOARD) && wci._type.equals("DogfightScoreboardWidget")) {
            _dogfightScoreboard = new Widget(wci, new DogfightScoreboardWidget(_widgetManager, this));
        }
    }

    @Override
    public void campaignGameButtonClicked() {
        _widgetManager.hideAll();
        _widgetManager.show(_campaign, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void raceGameButtonClicked() {
        _widgetManager.hideAll();
        _widgetManager.show(_raceScoreboard, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void dogfightGameButtonClicked() {
        _widgetManager.hideAll();
        _widgetManager.show(_dogfightScoreboard, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void optionsButtonClicked() {
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
    public void mainMenuButtonClicked() {
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
