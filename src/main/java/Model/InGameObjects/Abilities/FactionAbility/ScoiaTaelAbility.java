package Model.InGameObjects.Abilities.FactionAbility;

import Model.Game.Player;

public class ScoiaTaelAbility extends FactionAbility {

    public ScoiaTaelAbility(Player player, Player owner) {
        super(player, owner);
    }

    @Override
    public boolean canAbilityBeTriggered() {
        return false;
    }

    @Override
    public void applyAbility(int duration) {
    }
}
