package Model.InGameObjects.Abilities.SpellAbilities;

import Model.InGameObjects.Cards.CardAbility;

public class SpellCommandersHornAbility extends CardAbility {


    @Override
    public void applyAbility() {
        playSfx("horn");
    }

    @Override
    public CardAbility deepCopy() {
        return new SpellCommandersHornAbility();
    }

    @Override
    public void writeDescription() {
        setDescription("Commander's Horn:\nDoubles the strength of all unit cards in that row. Limited to 1 per row.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/SpecialCardAbilities/card_special_horn.png";
    }
}
