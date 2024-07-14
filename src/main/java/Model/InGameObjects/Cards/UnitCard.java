package Model.InGameObjects.Cards;
import Enum.CardPlace;
import java.util.ArrayList;
import Enum.*;
import Model.InGameObjects.Board.Row;

public class UnitCard extends Card{

    private int basePower;
    private int moralBoost;
    private int multipliedPower;
    private int currentPower;
    private boolean isHero;
    private Row row;

    public UnitCard(CardNames name, CardPlace cardPlace, int basePower, boolean isHero, ArrayList<CardAbility> cardAbilities) {
        super(name, cardPlace, cardAbilities);
        this.basePower = basePower;
        this.currentPower = basePower;
        this.isHero = isHero;
        this.multipliedPower = 1;
    }

    @Override
    public Card deepCopy() {
        ArrayList<CardAbility> copiedCardAbilities = new ArrayList<>();
        for (CardAbility cardAbility : getCardAbilities()) {
            copiedCardAbilities.add(cardAbility.deepCopy());
        }

        UnitCard copiedUnitCard = new UnitCard(getCardName(), getCardPlace(), basePower,
                        isHero(), copiedCardAbilities);

        copiedUnitCard.setCardPlace(getCardPlace());
        copiedUnitCard.setCurrentState(getCurrentState());
        copiedUnitCard.setPreviousState(getPreviousState());
        copiedUnitCard.setCurrentPower(getCurrentPower());

        if (getCurrentPlaceInGame() != null)
            copiedUnitCard.setCurrentPlaceInGame(getCurrentPlaceInGame());
        if (getOwner() != null)
            copiedUnitCard.setOwner(getOwner());

        return copiedUnitCard;
    }

    public void putInField() {}

    public void removeFromField() {}

    public int calculateStrength() {
        return (currentPower * multipliedPower) + moralBoost;
    }

    public void setMoralBoost(int moralBoost) {
        this.moralBoost = moralBoost;
    }

    public void setMultipliedPower(int multipliedPower) {
        this.multipliedPower = multipliedPower;
    }

    public void setCurrentPower(int currentPower) {
        this.currentPower = currentPower;
    }

    public void setHero(boolean hero) {
        isHero = hero;
    }

    public int getBasePower() {
        return basePower;
    }

    public int getMoralBoost() {
        return moralBoost;
    }

    public int getMultipliedPower() {
        return multipliedPower;
    }

    public int getCurrentPower() {
        return currentPower;
    }

    public boolean isHero() {
        return isHero;
    }

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }
}
