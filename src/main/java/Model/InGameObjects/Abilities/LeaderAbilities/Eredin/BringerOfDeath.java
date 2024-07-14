package Model.InGameObjects.Abilities.LeaderAbilities.Eredin;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;

import java.util.ArrayList;
import java.util.Random;
import Enum.CardStates;
import controller.Client;

public class BringerOfDeath extends CardAbility {

    @Override
    public void applyAbility() {

        ArrayList<Card> discardPileCards = new ArrayList<>();
        discardPileCards.addAll(getCard().getPlayer().getBoard().getFriendlyDiscardPileArea().getCardsOnSection());

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

        playSfx("med");

        Client.client.sendSfxAnimationPackage(card, "med", "non");
    }

    @Override
    public CardAbility deepCopy() {
        return new BringerOfDeath();
    }

    @Override
    public void writeDescription() {
        setDescription("Bringer Of Death:\nRestore a card from your discard pile to your hand.");
    }
}
