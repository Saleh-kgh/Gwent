package Model.InGameObjects.Abilities.LeaderAbilities.SevenKingdoms;

import Model.InGameObjects.Abilities.SpellAbilities.SpellImpenetrableFogAbility;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import controller.Client;
import Enum.CardNames;
import Enum.CardStates;

public class KingJoffrey extends CardAbility {

    @Override
    public void applyAbility() {
        for (Card card : getCard().getPlayer().getBoard().getWeatherCardArea().getCardsOnSection()) {
            if (card.getCardAbilities().get(0) instanceof SpellImpenetrableFogAbility) {
                getCard().getPlayer().getGameController().failedLeaderAbility(getCard());
                return;
            }
        }

        Card fogCard = null;
        for (Card card : getCard().getPlayer().getDeck()) {
            if (card.getCardName().equals(CardNames.FOG))
                fogCard = card;
        }

        if (fogCard == null) {
            getCard().getPlayer().getGameController().failedLeaderAbility(getCard());
            return;
        }

        fogCard.createGraphicalFields();
        playWeatherCard(fogCard);
        getCard().getPlayer().getGameController().finishLeaderAbility();
    }

    public void playWeatherCard(Card card) {

        getCard().getPlayer().getGameController().moveCardProtocol(card, card.getCurrentPlaceInGame(),
                getCard().getPlayer().getBoard().getWeatherCardArea(), CardStates.ROW_IN_GAME_PLAYED);

        Client.client.sendMoveCardPackage(card, card.getCurrentPlaceInGame(),
                getCard().getPlayer().getBoard().getWeatherCardArea(), CardStates.ROW_IN_GAME_PLAYED);

        card.applyAbility();

        getCard().getPlayer().getBoard().getFriendlyRangedArea().updateSection();
        getCard().getPlayer().getBoard().getOpponentRangedArea().updateSection();
    }

    @Override
    public CardAbility deepCopy() {
        return new KingJoffrey();
    }

    @Override
    public void writeDescription() {
        setDescription("King Joffrey:\nPlays an impenetrable fog.");
    }
}
