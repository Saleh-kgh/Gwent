package Model.InGameObjects.Abilities.LeaderAbilities.Emhyr;

import Model.InGameObjects.Abilities.SpellAbilities.SpellTorrentialRainAbility;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Enum.CardStates;
import Model.InGameObjects.Cards.CardFactory;
import Enum.CardNames;
import controller.Client;

public class HisImperialMajesty extends CardAbility {

    @Override
    public void applyAbility() {
        for (Card card : getCard().getPlayer().getBoard().getWeatherCardArea().getCardsOnSection()) {
            if (card.getCardAbilities().get(0) instanceof SpellTorrentialRainAbility) {
                getCard().getPlayer().getGameController().failedLeaderAbility(getCard());
                return;
            }
        }

        Card rainCard = null;
        for (Card card : getCard().getPlayer().getDeck()) {
            if (card.getCardName().equals(CardNames.RAIN))
                rainCard = card;
        }

        if (rainCard == null) {
            getCard().getPlayer().getGameController().failedLeaderAbility(getCard());
            return;
        }

        rainCard.createGraphicalFields();
        playWeatherCard(rainCard);
        getCard().getPlayer().getGameController().finishLeaderAbility();
    }

    public void playWeatherCard(Card card) {

        getCard().getPlayer().getGameController().moveCardProtocol(card, card.getCurrentPlaceInGame(),
                    getCard().getPlayer().getBoard().getWeatherCardArea(), CardStates.ROW_IN_GAME_PLAYED);

        Client.client.sendMoveCardPackage(card, card.getCurrentPlaceInGame(),
                getCard().getPlayer().getBoard().getWeatherCardArea(), CardStates.ROW_IN_GAME_PLAYED);

        card.applyAbility();

        getCard().getPlayer().getBoard().getFriendlySiegeArea().updateSection();
        getCard().getPlayer().getBoard().getOpponentSiegeArea().updateSection();
    }

    @Override
    public CardAbility deepCopy() {
        return new HisImperialMajesty();
    }

    @Override
    public void writeDescription() {
        setDescription("His Imperial Majesty:\nPick a Torrential Rain card from your deck and play it instantly.");
    }
}
