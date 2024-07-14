package Model.InGameObjects.Abilities.LeaderAbilities.Emhyr;

import Model.InGameObjects.Cards.CardAbility;

public class TheWhiteFlame extends CardAbility {

    @Override
    public void applyAbility() {

    }

    @Override
    public CardAbility deepCopy() {
        return new TheWhiteFlame();
    }

    @Override
    public void writeDescription() {
        setDescription("The White Flame:\nCancel your opponent's Leader Ability.");
    }
}
