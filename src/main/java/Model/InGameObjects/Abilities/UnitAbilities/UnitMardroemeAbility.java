package Model.InGameObjects.Abilities.UnitAbilities;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.UnitCard;
import controller.Client;

import java.util.ArrayList;

public class UnitMardroemeAbility extends CardAbility {

    @Override
    public void applyAbility() {
        UnitCard unitCard = (UnitCard) getCard();

        unitCard.getRow().setHasMardroeme(true);

        ArrayList<Card> berserkerCards = new ArrayList<>();

        for (Card card : unitCard.getRow().getCardsOnSection()) {
            for (CardAbility cardAbility : card.getCardAbilities()) {
                if (cardAbility instanceof UnitBerserkerAbility) {
                    berserkerCards.add(card);
                }
            }
        }

        for (Card card : berserkerCards) {
            unitCard.getPlayer().getGameController().applyCardAbilityWithDelay(card, card.getCurrentPlaceInGame());
        }

        playSfx("resilience");

        Client.client.sendSfxAnimationPackage(getCard(), "resilience", "non");
    }

    @Override
    public CardAbility deepCopy() {
        return new UnitMardroemeAbility();
    }

    @Override
    public void writeDescription() {
        setDescription("Mardoreme:\nTriggers transformation of all Berserker cards on the same row.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/UnitCardAbilities/card_ability_mardroeme.png";
    }
}
