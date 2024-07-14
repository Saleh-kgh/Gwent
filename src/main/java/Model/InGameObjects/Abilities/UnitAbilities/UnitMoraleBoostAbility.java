package Model.InGameObjects.Abilities.UnitAbilities;

import Model.InGameObjects.Cards.CardAbility;
import controller.Client;

public class UnitMoraleBoostAbility extends CardAbility {

    @Override
    public void applyAbility() {
        playSfx("moral");
        playAnimation("morale");

        Client.client.sendSfxAnimationPackage(getCard(), "moral", "morale");
    }

    @Override
    public CardAbility deepCopy() {
        return new UnitMoraleBoostAbility();
    }

    @Override
    public void writeDescription() {
        setDescription("Morale Boost:\nAdds +1 to all units in the row (excluding itself).");
    }

    @Override
    public String getAbilityIcon() {
        return "/Pics/Icons/UnitCardAbilities/card_ability_morale.png";
    }
}
