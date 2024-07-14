package Model.InGameObjects.Abilities.LeaderAbilities.Eredin;

import Model.InGameObjects.Cards.CardAbility;

public class TheTreacherous extends CardAbility {

    @Override
    public void applyAbility() {

    }

    @Override
    public CardAbility deepCopy() {
        return new TheTreacherous();
    }

    @Override
    public void writeDescription() {
        setDescription("The Treacherous:\nDoubles the strength of all spy cards (affects both players).");
    }
}
