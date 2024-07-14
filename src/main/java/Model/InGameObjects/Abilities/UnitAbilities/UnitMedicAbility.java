package Model.InGameObjects.Abilities.UnitAbilities;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.LeaderCard;
import Model.InGameObjects.Cards.UnitCard;
import Enum.*;
import java.util.ArrayList;
import java.util.Random;

public class UnitMedicAbility extends CardAbility {

    @Override
    public void applyAbility() {
        UnitCard medicCard = (UnitCard) getCard();

        if (!isLeaderEmhyrInvaderOfNorth()) {
            ArrayList<Card> unitCards = new ArrayList<>();

            for (Card card : medicCard.getPlayer().getBoard().getFriendlyDiscardPileArea().getCardsOnSection()) {
                if (card instanceof UnitCard) {
                    if (!((UnitCard) card).isHero()) {
                        card.setCurrentState(CardStates.DES_IN_GAME_SELECTABLE);
                        unitCards.add((UnitCard) card);
                    }
                }
            }

            if (unitCards.isEmpty()) {
                return;
            }

            getCard().getPlayer().setPlayerState(PlayerState.CHOOSING_SCROLL_BOX_CARD);

            getCard().getPlayer().getGameController().showChooseScrollBox(unitCards);
        }
        else {
            ArrayList<Card> discardPileCards = new ArrayList<>();
            discardPileCards.addAll(getCard().getPlayer().getBoard().getFriendlyDiscardPileArea().getCardsOnSection());

            Random random = new Random();
            Card card = discardPileCards.get(random.nextInt(discardPileCards.size()));
            getCard().getPlayer().getGameController().reviveCardFromDiscPile(card);
        }
    }

    private boolean isLeaderEmhyrInvaderOfNorth() {
        LeaderCard playerLeader = getCard().getPlayer().getLeaderCard();
        LeaderCard opponentLeader = getCard().getPlayer().getOpponent().getLeaderCard();

        return playerLeader.getCardName().equals(CardNames.EMHYR_INVADER_OF_THE_NORTH) ||
                opponentLeader.getCardName().equals(CardNames.EMHYR_INVADER_OF_THE_NORTH);
    }

    @Override
    public CardAbility deepCopy() {
        return new UnitMedicAbility();
    }


    @Override
    public void writeDescription() {
        setDescription("Medic:\nChoose one card from your discard pile and play it instantly (no Heroes or Special Cards).");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/UnitCardAbilities/card_ability_medic.png";
    }
}
