package Model.InGameObjects.Cards;
import Enum.CardPlace;
import java.util.ArrayList;
import Enum.*;

public class SpellCard extends Card{

    public SpellCard(CardNames name,CardPlace cardPlace, ArrayList<CardAbility> cardAbilities) {
        super(name, cardPlace, cardAbilities);
    }

    @Override
    public Card deepCopy() {
        ArrayList<CardAbility> copiedCardAbilities = new ArrayList<>();
        for (CardAbility cardAbility : getCardAbilities()) {
            copiedCardAbilities.add(cardAbility.deepCopy());
        }

        SpellCard copiedSpellCard = new SpellCard(getCardName(), getCardPlace(), copiedCardAbilities);

        copiedSpellCard.setCardPlace(getCardPlace());
        copiedSpellCard.setCurrentState(getCurrentState());
        copiedSpellCard.setPreviousState(getPreviousState());

        if (getCurrentPlaceInGame() != null)
            copiedSpellCard.setCurrentPlaceInGame(getCurrentPlaceInGame());
        if (getOwner() != null)
            copiedSpellCard.setOwner(getOwner());

        return copiedSpellCard;
    }

    public void putInField() {}

    public void removeFromField() {}
}
