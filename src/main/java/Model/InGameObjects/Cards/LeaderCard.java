package Model.InGameObjects.Cards;
import Enum.CardPlace;
import java.util.ArrayList;
import Enum.*;

public class LeaderCard extends Card{

    private boolean isLeadersAbilityPassive;

    private boolean isLeaderPlayed;

    public LeaderCard(CardNames name, CardPlace cardPlace, boolean isLeadersAbilityPassive, ArrayList<CardAbility> cardAbilityArrayList) {
        super(name, cardPlace, cardAbilityArrayList);
        this.isLeadersAbilityPassive = isLeadersAbilityPassive;
        this.isLeaderPlayed = false;
    }

    public boolean isLeadersAbilityPassive() {
        return isLeadersAbilityPassive;
    }

    public void setLeadersAbilityPassive(boolean leadersAbilityPassive) {
        isLeadersAbilityPassive = leadersAbilityPassive;
    }

    public boolean isLeaderPlayed() {
        return isLeaderPlayed;
    }

    public void setLeaderPlayed(boolean leaderPlayed) {
        isLeaderPlayed = leaderPlayed;
    }

    @Override
    public Card deepCopy() {
        ArrayList<CardAbility> copiedCardAbilities = new ArrayList<>();
        for (CardAbility cardAbility : getCardAbilities()) {
            copiedCardAbilities.add(cardAbility.deepCopy());
        }

        LeaderCard copiedLeaderCard = new LeaderCard(getCardName(), getCardPlace(), isLeadersAbilityPassive, copiedCardAbilities);

        copiedLeaderCard.setCardPlace(getCardPlace());
        copiedLeaderCard.setCurrentState(getCurrentState());
        copiedLeaderCard.setPreviousState(getPreviousState());

        if (getCurrentPlaceInGame() != null)
            copiedLeaderCard.setCurrentPlaceInGame(getCurrentPlaceInGame());
        if (getOwner() != null)
            copiedLeaderCard.setOwner(getOwner());

        return copiedLeaderCard;
    }

    @Override
    public void putInField() {

    }

    @Override
    public void removeFromField() {

    }
}
