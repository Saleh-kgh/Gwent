package Model.InGameObjects.Abilities.UnitAbilities;

import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.UnitCard;
import controller.Client;

public class UnitCommandersHornAbility extends CardAbility {

    @Override
    public void applyAbility() {
        UnitCard unitCard = (UnitCard) getCard();
        if (!unitCard.getRow().isHorned())
            unitCard.getRow().setHorned(true);

        playSfx("horn");
        playAnimation("horn");

        Client.client.sendSfxAnimationPackage(getCard(), "horn", "horn");
    }

    @Override
    public CardAbility deepCopy() {
        return new UnitCommandersHornAbility();
    }

    @Override
    public void writeDescription() {
        setDescription("Commander's Horn:\nDoubles the strength of all unit cards in that row. Limited to 1 per row.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/UnitCardAbilities/card_ability_horn.png";
    }
}
