package Model.InGameObjects.Abilities.LeaderAbilities.Emhyr;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.LeaderCard;

import java.util.ArrayList;
import java.util.Random;
import Enum.*;
import controller.Client;

public class TheRelentless extends CardAbility {

    @Override
    public void applyAbility() {

        ArrayList<Card> discardPileCards = new ArrayList<>();
        discardPileCards.addAll(getCard().getPlayer().getBoard().getOpponentDiscardPileArea().getCardsOnSection());

        if (discardPileCards.isEmpty()) {
            getCard().getPlayer().getGameController().failedLeaderAbility(getCard());
            return;
        }

        Random random = new Random();
        Card card = discardPileCards.get(random.nextInt(discardPileCards.size()));

        getCard().getPlayer().getGameController().moveCardProtocol(card, card.getCurrentPlaceInGame(),
                getCard().getPlayer().getBoard().getHandArea(), CardStates.ROW_IN_GAME_HAND);

        Client.client.sendMoveCardPackage(card, card.getCurrentPlaceInGame(),
                getCard().getPlayer().getBoard().getHandArea(), CardStates.ROW_IN_GAME_HAND);

        getCard().getPlayer().getGameController().finishLeaderAbility();

        playSfx("horn");

        Client.client.sendSfxAnimationPackage(card, "horn", "non");
    }

    @Override
    public CardAbility deepCopy() {
        return new TheRelentless();
    }

    @Override
    public void writeDescription() {
        setDescription("The Relentless:\nDraw a card from your opponent's discard pile.");
    }
}
