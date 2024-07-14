package Model.InGameObjects.Abilities.UnitAbilities;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.UnitCard;
import java.util.ArrayList;
import Enum.CardTightBTypes;
import controller.Client;
import view.Animations.AbilityAnimation;

public class UnitTightBondAbility extends CardAbility {

    private CardTightBTypes cardTightBType;

    public UnitTightBondAbility(CardTightBTypes cardTightBType) {
        this.cardTightBType = cardTightBType;
    }

    public void setMultipliedPowerForRowBond() {
        UnitCard unitCard = (UnitCard) getCard();
        int sameBondInRowCount = 0;
        ArrayList<UnitCard> bondedCards = new ArrayList<>();
        bondedCards.add(unitCard);

        for (Card card : unitCard.getCurrentPlaceInGame().getCardsOnSection()) {
            for (CardAbility cardAbility : card.getCardAbilities()) {
                if (cardAbility instanceof UnitTightBondAbility) {
                    if (((UnitTightBondAbility) cardAbility).getCardTightBType().equals(cardTightBType)) {
                        sameBondInRowCount++;
                        bondedCards.add((UnitCard) card);
                    }
                }
            }
        }

        for (UnitCard bondedCard : bondedCards) {
            bondedCard.setMultipliedPower(sameBondInRowCount);
        }
    }

    @Override
    public void applyAbility() {
        UnitCard unitCard = (UnitCard) getCard();
        int sameBondInRowCount = 0;
        ArrayList<UnitCard> bondedCards = new ArrayList<>();
        bondedCards.add(unitCard);

        for (Card card : unitCard.getCurrentPlaceInGame().getCardsOnSection()) {
            for (CardAbility cardAbility : card.getCardAbilities()) {
                if (cardAbility instanceof UnitTightBondAbility) {
                    if (((UnitTightBondAbility) cardAbility).getCardTightBType().equals(cardTightBType)) {
                        sameBondInRowCount++;
                        bondedCards.add((UnitCard) card);
                    }
                }
            }
        }

        for (UnitCard bondedCard : bondedCards) {
            bondedCard.setMultipliedPower(sameBondInRowCount);

            if (bondedCards.size() > 2) {
                new AbilityAnimation(bondedCard.getRowCard(), "/Pics/Icons/InGameEffects/anim_bond.png").play();
                playSfx("moral");

                Client.client.sendSfxAnimationPackage(bondedCard, "moral", "bond");
            }
        }

    }

    @Override
    public CardAbility deepCopy() {
        return new UnitTightBondAbility(cardTightBType);
    }

    @Override
    public void writeDescription() {
        setDescription("Tight Bond:\nPlace next to a card with the same name to multiply the strength of both cards.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/UnitCardAbilities/card_ability_bond.png";
    }

    public CardTightBTypes getCardTightBType() {
        return cardTightBType;
    }
}
