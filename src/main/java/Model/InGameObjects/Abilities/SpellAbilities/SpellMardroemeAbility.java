package Model.InGameObjects.Abilities.SpellAbilities;

import Model.InGameObjects.Board.HornArea;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Abilities.UnitAbilities.UnitBerserkerAbility;
import controller.Client;

import java.util.ArrayList;

public class SpellMardroemeAbility extends CardAbility {

    @Override
    public CardAbility deepCopy() {
        return new SpellMardroemeAbility();
    }

    @Override
    public void writeDescription() {
        setDescription("Mardroeme:\nTriggers transformation of all Berserker cards on the same row.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/SpecialCardAbilities/card_special_mardroeme.png";
    }

    @Override
    public void applyAbility() {
         HornArea hornArea = (HornArea) getCard().getCurrentPlaceInGame();
         hornArea.getHornRow().setHasMardroeme(true);

        ArrayList<Card> berserkerCards = new ArrayList<>();

        for (Card card : hornArea.getHornRow().getCardsOnSection()) {
            for (CardAbility cardAbility : card.getCardAbilities()) {
                if (cardAbility instanceof UnitBerserkerAbility) {
                    berserkerCards.add(card);
                }
            }
        }

        for (Card card : berserkerCards) {
            card.getPlayer().getGameController().applyCardAbilityWithDelay(card, card.getCurrentPlaceInGame());
        }

        playSfx("resilience");

        Client.client.sendSfxAnimationPackage(getCard(), "resilience", "non");
    }
}
