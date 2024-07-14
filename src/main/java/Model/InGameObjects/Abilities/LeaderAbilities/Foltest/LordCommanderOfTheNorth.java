package Model.InGameObjects.Abilities.LeaderAbilities.Foltest;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.CardFactory;
import Enum.CardNames;
import Enum.CardStates;
import controller.Client;

public class LordCommanderOfTheNorth extends CardAbility {

    @Override
    public void applyAbility() {
        if (getCard().getPlayer().getBoard().getWeatherCardArea().getCardsOnSection().isEmpty()) {
            getCard().getPlayer().getGameController().failedLeaderAbility(getCard());
            return;
        }

        Card clearWeatherCard = CardFactory.getCopiedCardByName(CardNames.CLEAR_WEATHER);
        clearWeatherCard.createGraphicalFields();
        playWeatherCard(clearWeatherCard);
        getCard().getPlayer().getGameController().finishLeaderAbility();
    }

    public void playWeatherCard(Card card) {
        card.setOwner(getCard().getOwner());
        card.setPlayer(getCard().getPlayer());
        card.setPreviousState(CardStates.ROW_HIDDEN);

        card.setCurrentPlaceInGame(getCard().getPlayer().getBoard().getFriendlyDeckArea());
        card.getRowCard().setLayoutX(getCard().getPlayer().getBoard().getFriendlyDeckArea().getLayoutX());
        card.getRowCard().setLayoutY(getCard().getPlayer().getBoard().getFriendlyDeckArea().getLayoutY());

        getCard().getPlayer().getGameController().moveCardProtocol(card, card.getCurrentPlaceInGame(),
                getCard().getPlayer().getBoard().getWeatherCardArea(), CardStates.ROW_IN_GAME_PLAYED);

        card.setId(7777);

        Client.client.sendMoveCardPackage(card, card.getCurrentPlaceInGame(),
                getCard().getPlayer().getBoard().getWeatherCardArea(), CardStates.ROW_IN_GAME_PLAYED);

        card.applyAbility();

        getCard().getCurrentPlaceInGame().updateSection();
    }

    @Override
    public CardAbility deepCopy() {
        return new LordCommanderOfTheNorth();
    }

    @Override
    public void writeDescription() {
        setDescription("Lord Commander Of The North:\nClear any weather effects " +
                    "(resulting from Biting Frost, Torrential Rain or Impenetrable Fog cards) in play.");
    }
}
