package Model.InGameObjects.Abilities.UnitAbilities;

import Model.InGameObjects.Cards.CardAbility;

public class UnitAgileAbility extends CardAbility {

    @Override
    public void applyAbility() {
        return;
    }

    @Override
    public CardAbility deepCopy() {
        return new UnitAgileAbility();
    }

    @Override
    public void writeDescription() {
        setDescription("Agile:\nCan be placed in either the Close Combat or the Ranged Combat row. Cannot be moved once placed.");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/UnitCardAbilities/card_ability_agile.png";
    }
}
