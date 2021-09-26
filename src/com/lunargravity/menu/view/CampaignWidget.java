package com.lunargravity.menu.view;

import com.lunargravity.engine.widgetsystem.*;

import java.io.IOException;

public class CampaignWidget extends WidgetObserver implements IButtonObserver, IListObserver {
    private static final String SINGLE_PLAYER_BUTTON = "singlePlayerButton";
    private static final String TWO_PLAYERS_BUTTON = "twoPlayersButton";
    private static final String SAVED_GAMES_LIST = "savedGamesList";
    private static final String LOAD_SAVED_GAME_BUTTON = "loadSavedGameButton";
    private static final String MAIN_MENU_BUTTON = "mainMenuButton";

    private final ICampaignWidgetObserver _observer;

    public CampaignWidget(WidgetManager widgetManager, ICampaignWidgetObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        WidgetCreateInfo child = wci.getChild(SINGLE_PLAYER_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(TWO_PLAYERS_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(SAVED_GAMES_LIST, "ListWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ListWidget(_widgetManager, this)));
        }
        child = wci.getChild(LOAD_SAVED_GAME_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
        child = wci.getChild(MAIN_MENU_BUTTON, "ButtonWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ButtonWidget(_widgetManager, this)));
        }
    }

    @Override
    public void buttonClicked(String widgetId) {
        switch (widgetId) {
            case SINGLE_PLAYER_BUTTON -> _observer.singlePlayerCampaignButtonClicked();
            case TWO_PLAYERS_BUTTON -> _observer.twoPlayersCampaignButtonClicked();
            case LOAD_SAVED_GAME_BUTTON -> {
                ListWidget list = (ListWidget)getWidget().getChild(SAVED_GAMES_LIST).getObserver();
                int index = list.getSelectedIndex();
                if (index >= 0) {
                    _observer.loadSavedCampaignButtonClicked(list.getItem(index));
                }
            }
            case MAIN_MENU_BUTTON -> _observer.mainMenuButtonClicked();
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

    @Override
    public void freeResources() {
        super.freeResources();
        // anything to do?
    }
}
