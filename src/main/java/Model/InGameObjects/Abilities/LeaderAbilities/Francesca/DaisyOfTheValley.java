package Model.InGameObjects.Abilities.LeaderAbilities.Francesca;

import Model.InGameObjects.Cards.CardAbility;

public class DaisyOfTheValley extends CardAbility {

    @Override
    public void applyAbility() {

    }

    @Override
    public CardAbility deepCopy() {
        return new DaisyOfTheValley();
    }

    @Override
    public void writeDescription() {
        setDescription("Daisy of the Valley:\nDraw an extra card at the beginning of the battle.");
    }
}
