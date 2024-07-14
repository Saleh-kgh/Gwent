package Model.InGameObjects.Abilities.LeaderAbilities.Foltest;

import Model.InGameObjects.Abilities.SpellAbilities.SpellImpenetrableFogAbility;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.CardFactory;
import Enum.CardStates;
import Enum.CardNames;
import controller.Client;

public class KingOfTemeria extends CardAbility {

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
        return new KingOfTemeria();
    }

    @Override
    public void writeDescription() {
        setDescription("King Of Termeria:\nPick an Impenetrable Fog card from your deck and play it instantly.");
    }
}
