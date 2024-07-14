package Model.InGameObjects.Abilities.LeaderAbilities.Skellige;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Enum.CardStates;
import controller.Client;

import java.util.ArrayList;

public class CrachAnCraite extends CardAbility {

    @Override
    public void applyAbility() {

        ArrayList<Card> playerDiscardedCards = new ArrayList<>();
        playerDiscardedCards.addAll(getCard().getPlayer().getBoard().getFriendlyDiscardPileArea().getCardsOnSection());
        ArrayList<Card> opponentDiscardedCards = new ArrayList<>();
        opponentDiscardedCards.addAll(getCard().getPlayer().getBoard().getOpponentDiscardPileArea().getCardsOnSection());

        if (playerDiscardedCards.isEmpty() && opponentDiscardedCards.isEmpty()) {
            getCard().getPlayer().getGameController().failedLeaderAbility(getCard());
            return;
        }

        for (Card card : playerDiscardedCards) {
            card.createGraphicalFields();

            card.getCurrentPlaceInGame().removeFromCardsOnSection(card);
            card.getCurrentPlaceInGame().updateSection();

            card.setPreviousState(card.getCurrentState());
            card.setCurrentState(CardStates.ROW_IN_GAME_DECK);
            card.setCurrentPlaceInGame(getCard().getPlayer().getBoard().getFriendlyDeckArea());
            card.getCurrentPlaceInGame().addToCardsOnSection(card);
            card.getCurrentPlaceInGame().updateSection();

            Client.client.sendRemoveCardPackage(card, getCard().getPlayer().getBoard().getFriendlyDiscardPileArea(),
                            CardStates.ROW_IN_GAME_DECK, false);

            Client.client.sendAddCardPackage(card, getCard().getPlayer().getBoard().getFriendlyDeckArea(),
                    CardStates.ROW_IN_GAME_DECK, false);
        }

        for (Card card : opponentDiscardedCards) {
            card.createGraphicalFields();

            card.getCurrentPlaceInGame().removeFromCardsOnSection(card);
            card.getCurrentPlaceInGame().updateSection();

            card.setPreviousState(card.getCurrentState());
            card.setCurrentState(CardStates.ROW_IN_GAME_DECK);
            card.setCurrentPlaceInGame(getCard().getPlayer().getBoard().getOpponentDeckArea());
            card.getCurrentPlaceInGame().addToCardsOnSection(card);
            card.getCurrentPlaceInGame().updateSection();

            Client.client.sendRemoveCardPackage(card, getCard().getPlayer().getBoard().getOpponentDiscardPileArea(),
                    CardStates.ROW_IN_GAME_DECK, false);

            Client.client.sendAddCardPackage(card, getCard().getPlayer().getBoard().getOpponentDeckArea(),
                    CardStates.ROW_IN_GAME_DECK, false);
        }

        playSfx("redraw");

        Client.client.sendSfxAnimationPackage(getCard(), "redraw", "non");

        getCard().getPlayer().getGameController().finishLeaderAbility();
    }

    @Override
    public CardAbility deepCopy() {
        return new CrachAnCraite();
    }

    @Override
    public void writeDescription() {
        setDescription("Crach an Craite:\nShuffle all cards from each player's graveyard back into their decks.");
    }
}
