package Model.InGameObjects.Abilities.LeaderAbilities.Francesca;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.CardFactory;
import Model.InGameObjects.Abilities.SpellAbilities.SpellBitingFrostAbility;
import Enum.CardNames;
import Enum.CardStates;
import controller.Client;

public class PureBloodElf extends CardAbility {

    @Override
    public void applyAbility() {
        for (Card card : getCard().getPlayer().getBoard().getWeatherCardArea().getCardsOnSection()) {
            if (card.getCardAbilities().get(0) instanceof SpellBitingFrostAbility) {
                getCard().getPlayer().getGameController().failedLeaderAbility(getCard());
                return;
            }
        }

        Card frostCard = null;
        for (Card card : getCard().getPlayer().getDeck()) {
            if (card.getCardName().equals(CardNames.FROST))
                frostCard = card;
        }

        if (frostCard == null) {
            getCard().getPlayer().getGameController().failedLeaderAbility(getCard());
            return;
        }

        frostCard.createGraphicalFields();
        playWeatherCard(frostCard);
        getCard().getPlayer().getGameController().finishLeaderAbility();
    }

    public void playWeatherCard(Card card) {

        getCard().getPlayer().getGameController().moveCardProtocol(card, card.getCurrentPlaceInGame(),
                getCard().getPlayer().getBoard().getWeatherCardArea(), CardStates.ROW_IN_GAME_PLAYED);

        Client.client.sendMoveCardPackage(card, card.getCurrentPlaceInGame(),
                getCard().getPlayer().getBoard().getWeatherCardArea(), CardStates.ROW_IN_GAME_PLAYED);

        card.applyAbility();

        getCard().getPlayer().getBoard().getFriendlyCloseArea().updateSection();
        getCard().getPlayer().getBoard().getOpponentCloseArea().updateSection();
    }

    @Override
    public CardAbility deepCopy() {
        return new PureBloodElf();
    }

    @Override
    public void writeDescription() {
        setDescription("Pureblood Elf:\nPick a Biting Frost card from your deck and play it instantly.");
    }
}
