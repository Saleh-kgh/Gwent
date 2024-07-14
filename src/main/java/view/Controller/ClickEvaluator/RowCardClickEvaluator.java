package view.Controller.ClickEvaluator;

import Model.InGameObjects.Cards.*;
import Model.Game.Player;
import javafx.scene.Node;
import Enum.CardStates;
import Enum.CardNames;

public class RowCardClickEvaluator extends ClickEvaluator {

    public RowCardClickEvaluator(Player player) {
        super(player);
    }

    @Override
    void manageOnChoosingFromHand(Node node) {
        Card card = ((RowCard) node).getCard();
        if (card.getCurrentState().equals(CardStates.ROW_IN_GAME_HAND)) {
            getPlayer().getGameController().selectCard(card);
        }
        else {
            //showCardsOnSection(card.getCurrentPlaceInGame());
            // i don't know what the hell is wrong with this piece of code, the scroll box happen to be created and
            // called for adding into pane but not appearing.
        }
    }

    @Override
    void manageOnChoosingRowToPut(Node node) {
        Card card = ((RowCard) node).getCard();
        if (card.getCurrentPlaceInGame().equals(getPlayer().getBoard().getHandArea())) {
            getPlayer().getGameController().selectCard(card);
        }
        else if(card instanceof LeaderCard) {
            if (getPlayer().getSelectedCard() instanceof LeaderCard)
                getPlayer().getGameController().playLeaderCard(card);
            else
                getPlayer().getGameController().selectCard(card);
        }
        else if(isCardOnAFriendlyRow(card)) {
            if (getPlayer().getSelectedCard().getCardName().equals(CardNames.DECOY)) {
                getPlayer().getGameController().replaceUnitCardWithDecoy(card);
            }
        }
        else if (card.getCurrentPlaceInGame().equals(getPlayer().getBoard().getWeatherCardArea())) {
            if (isCardAWeatherCard(card)) {
                getPlayer().getGameController().playACardFromHand(card.getCurrentPlaceInGame());
            }
        }
    }

    @Override
    void manageOnLookingAtCards(Node node) {
        getPlayer().getGameController().stopPlayerFromLookingAtCards();
    }

    @Override
    void manageOnChoosingScrollBoxCard(Node node) {
        return;
    }

    @Override
    void manageOnSwappingInitialHand(Node node) {
        getPlayer().getGameController().stopSwappingInitialHand();
    }

    @Override
    void manageOnLeaderChoosingCards(Node node) {
        return;
    }

    @Override
    void manageOnLeaderLookingCards(Node node) {
        getPlayer().getGameController().stopShowingScrollBoxOfLeaderAbility();
    }

    private boolean isCardOnAFriendlyRow(Card card) {
        return card.getCurrentPlaceInGame().equals(getPlayer().getBoard().getFriendlyCloseArea()) ||
                card.getCurrentPlaceInGame().equals(getPlayer().getBoard().getFriendlyRangedArea()) ||
                card.getCurrentPlaceInGame().equals(getPlayer().getBoard().getFriendlySiegeArea());
    }

    private boolean isCardAWeatherCard(Card card) {
        return card.getCardName().equals(CardNames.FROST) ||
               card.getCardName().equals(CardNames.FOG) ||
               card.getCardName().equals(CardNames.RAIN) ||
               card.getCardName().equals(CardNames.STORM) ||
               card.getCardName().equals(CardNames.CLEAR_WEATHER);
    }
}
