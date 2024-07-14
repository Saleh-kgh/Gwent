package Model.InGameObjects.Abilities.LeaderAbilities.Emhyr;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Enum.PlayerState;
import java.util.ArrayList;
import java.util.Random;

public class EmperorOfNilfgaard extends CardAbility {

    @Override
    public void applyAbility() {

        ArrayList<Card> hand = new ArrayList<>();
        hand.addAll(getCard().getPlayer().getOpponent().getHand());

        if (hand.isEmpty()) {
            getCard().getPlayer().getGameController().failedLeaderAbility(getCard());
            return;
        }

        Random random = new Random();

        ArrayList<Card> threeRandomCards = new ArrayList<>();
        int i = 0;

        while(i < 3 && i < hand.size()) {
            int randomInt = random.nextInt(hand.size());
            Card randomCard = hand.get(randomInt);

            if (!threeRandomCards.contains(randomCard)) {
                threeRandomCards.add(randomCard);
                i++;
            }
        }

        getCard().getPlayer().setPlayerState(PlayerState.LEADER_LOOKING_CARDS);

        getCard().getPlayer().getGameController().showArrayOfCardsOfLeaderAbility(threeRandomCards);

        playSfx("horn");
    }

    @Override
    public CardAbility deepCopy() {
        return new EmperorOfNilfgaard();
    }

    @Override
    public void writeDescription() {
        setDescription("Emperor of Nilfgaard:\nLook at 3 random cards from your opponent's hand.");
    }
}
