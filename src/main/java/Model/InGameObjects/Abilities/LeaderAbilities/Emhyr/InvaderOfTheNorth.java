package Model.InGameObjects.Abilities.LeaderAbilities.Emhyr;

import Model.InGameObjects.Cards.CardAbility;

public class InvaderOfTheNorth extends CardAbility {

    @Override
    public void applyAbility() {

    }

    @Override
    public CardAbility deepCopy() {
        return new InvaderOfTheNorth();
    }

    @Override
    public void writeDescription() {
        setDescription("Invader of the North:\nAbilities that restore a unit to the battlefield restore a randomly-chosen unit. Affects both players.");
    }
}
