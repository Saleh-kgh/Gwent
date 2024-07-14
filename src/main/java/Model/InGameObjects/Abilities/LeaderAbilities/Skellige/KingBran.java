package Model.InGameObjects.Abilities.LeaderAbilities.Skellige;

import Model.InGameObjects.Cards.CardAbility;

public class KingBran extends CardAbility {

    @Override
    public void applyAbility() {

    }

    @Override
    public CardAbility deepCopy() {
        return new KingBran();
    }

    @Override
    public void writeDescription() {
        setDescription("King Bran:\nUnits only lose half their Strength in bad weather conditions.");
    }
}
