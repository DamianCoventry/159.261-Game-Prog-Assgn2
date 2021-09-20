package com.lunargravity.menu.view;

import com.lunargravity.engine.widgetsystem.*;

public class CampaignWidget extends WidgetObserver implements IButtonObserver, IListObserver {
    private static final String BACKGROUND_IMAGE = "backgroundImage";
    private static final String SINGLE_PLAYER_BUTTON = "singlePlayerButton";
    private static final String TWO_PLAYERS_BUTTON = "twoPlayersButton";
    private static final String SAVED_GAMES_LIST = "savedGamesList";
    private static final String LOAD_SAVED_GAME_BUTTON = "loadSavedGameButton";
    private static final String MAIN_MENU_BUTTON = "mainMenuButton";

    private final ICampaignWidgetObserver _observer;

    private Widget _backgroundImage;
    private Widget _singlePlayerButton;
    private Widget _twoPlayersButton;
    private Widget _savedGamesList;
    private Widget _loadSavedGameButton;
    private Widget _mainMenuButton;

    public CampaignWidget(WidgetManager widgetManager, ICampaignWidgetObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void createChildWidgets(WidgetCreateInfo wci) {
        // TODO: need to examine the wci structure and pass the correct info to each of these ctor calls
        _backgroundImage = new Widget(wci, new ImageWidget(_widgetManager));
        _singlePlayerButton = new Widget(wci, new ButtonWidget(_widgetManager, this));
        _twoPlayersButton = new Widget(wci, new ButtonWidget(_widgetManager, this));
        _savedGamesList = new Widget(wci, new ListWidget(_widgetManager, this));
        _loadSavedGameButton = new Widget(wci, new ButtonWidget(_widgetManager, this));
        _mainMenuButton = new Widget(wci, new ButtonWidget(_widgetManager, this));
    }

    @Override
    public void buttonClicked(String widgetId) {
        if (widgetId.equals(SINGLE_PLAYER_BUTTON)) {
            _observer.singlePlayerCampaignButtonClicked();
        }
        else if (widgetId.equals(TWO_PLAYERS_BUTTON)) {
            _observer.twoPlayersCampaignButtonClicked();
        }
        else if (widgetId.equals(LOAD_SAVED_GAME_BUTTON)) {
            ListWidget list = (ListWidget)_savedGamesList.getObserver();
            int index = list.getSelectedIndex();
            if (index >= 0) {
                _observer.loadSavedCampaignButtonClicked(list.getItem(index));
            }
        }
        else if (widgetId.equals(MAIN_MENU_BUTTON)) {
            _observer.mainMenuButtonClicked();
        }
    }

    @Override
    public void selectedListItemChanged(String widgetId, int oldItem, int newItem) {
        // TODO
    }

    @Override
    public void listItemAdded(String widgetId, int itemIndex) {
        // TODO
    }

    @Override
    public void listItemRemoved(String widgetId, int itemIndex) {
        // TODO
    }

    @Override
    public void listItemsCleared(String widgetId) {
        // TODO
    }
}
