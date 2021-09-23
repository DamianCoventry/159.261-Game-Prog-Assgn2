package com.lunargravity.menu.view;

import com.lunargravity.engine.graphics.*;
import com.lunargravity.engine.scene.ISceneAssetOwner;
import com.lunargravity.engine.widgetsystem.*;
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
        IOptionsWidgetObserver
{
    private static final String MAIN = "main";
    private static final String CAMPAIGN = "campaign";
    private static final String OPTIONS = "options";

    private final WidgetManager _widgetManager;
    private final IMenuController _controller;
    private final IMenuModel _model;

    private Widget _main;
    private Widget _campaign;
    private Widget _options;

    public MenuView(WidgetManager widgetManager, IMenuController controller, IMenuModel model) {
        _widgetManager = widgetManager;
        _controller = controller;
        _model = model;
    }

    @Override
    public void onViewThink() {
        // TODO
    }

    @Override
    public void onDrawView3d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void onDrawView2d(int viewport, Matrix4f projectionMatrix) {
        // TODO
    }

    @Override
    public void onObjectLoaded(String name, String type, Transform transform) {
        // TODO
    }

    @Override
    public void onStaticMeshLoaded(GlStaticMesh staticMesh) {
        // TODO
    }

    @Override
    public void onMaterialLoaded(GlMaterial material) {
        // TODO
    }

    @Override
    public void onTextureLoaded(GlTexture texture) {
        // TODO
    }

    @Override
    public void onWidgetLoaded(WidgetCreateInfo wci) throws IOException {
        if (wci._id.equals(MAIN) && wci._type.equals("MainWidget")) {
            _main = new Widget(wci, new MainWidget(_widgetManager, this));
        }
        else if (wci._id.equals(CAMPAIGN) && wci._type.equals("CampaignWidget")) {
            _campaign = new Widget(wci, new CampaignWidget(_widgetManager, this));
        }
        else if (wci._id.equals(OPTIONS) && wci._type.equals("CampaignWidget")) {
            _options = new Widget(wci, new OptionsWidget(_widgetManager, this));
        }
    }

    @Override
    public void campaignGameButtonClicked() {
        _widgetManager.hideAll();
        _widgetManager.show(_campaign, WidgetManager.ShowAs.FIRST);
    }

    @Override
    public void raceGameButtonClicked() {
        _controller.viewRaceScoreboard(); // this changes the application's current state
    }

    @Override
    public void dogfightGameButtonClicked() {
        _controller.viewDogfightScoreboard(); // this changes the application's current state
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
