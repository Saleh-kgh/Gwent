package Model.InGameObjects.Cards;

import java.io.Serializable;
import java.util.ArrayList;
import Enum.CardPlace;
import Model.InGameObjects.Abilities.UnitAbilities.UnitBerserkerAbility;
import Model.InGameObjects.Abilities.UnitAbilities.UnitTransformerAbility;
import Model.InGameObjects.Board.BoardSection;
import Model.InGameObjects.Board.Row;
import Model.Game.Player;
import Model.User.User;
import Enum.*;

public abstract class Card implements Serializable {
    private CardNames name;
    private int id;
    private DescriptionCard descriptionCard; // for the image
    private RowCard rowCard; // for the pane and label and ability ...
    private CardExplanation cardExplanation;
    private CardPlace cardPlace;
    private CardStates previousState;
    private CardStates currentState;
    private BoardSection currentPlaceInGame;
    private boolean isTransitioning;
    private Player player;
    private Player owner;
    private ArrayList<CardAbility> cardAbilities;

    public Card(CardNames name, CardPlace cardPlace, ArrayList<CardAbility> cardAbilityArrayList) {
        this.name = name;
        this.cardPlace = cardPlace;
        this.cardAbilities = cardAbilityArrayList;
        this.isTransitioning = false;
        setCardForCardAbilities();
    }

    public void createGraphicalFields() {
        createCardExplanation();
        descriptionCard = new DescriptionCard(this);
        rowCard = new RowCard(this);
    }

    public void removeGraphicalFields() {
        cardExplanation = null;
        descriptionCard = null;
        rowCard = null;

        if (!cardAbilities.isEmpty() && cardAbilities.get(0) instanceof UnitTransformerAbility) {
            ((UnitTransformerAbility) cardAbilities.get(0)).getTransformedCard().removeGraphicalFields();
        }
        else if (!cardAbilities.isEmpty() && cardAbilities.get(0) instanceof UnitBerserkerAbility) {
            ((UnitBerserkerAbility) cardAbilities.get(0)).getMardroemedCard().removeGraphicalFields();
        }
    }

    private void setCardForCardAbilities() {
        for (CardAbility cardAbility : cardAbilities) {
            cardAbility.setCard(this);
        }
    }

    private void createCardExplanation() {
        StringBuilder description = new StringBuilder();
        boolean isAbilityLess = true;

        if (this instanceof UnitCard) {
            if (((UnitCard) this).isHero())
                description.append("Hero:\nNot affected by any Special Cards or abilities.\n");
        }

        for (CardAbility cardAbility : cardAbilities) {
            cardAbility.writeDescription();
            description.append(cardAbility.getDescription());
            description.append("\n");
            isAbilityLess = false;
        }

        if (isAbilityLess) {
            description.append("Regular card:\n No description.");
        }

        cardExplanation = new CardExplanation(description.toString());
    }

    abstract public Card deepCopy();

    public void setCurrentState(CardStates currentState) {
        this.currentState = currentState;
    }

    public DescriptionCard getDescriptionCard() {
        return descriptionCard;
    }

    public RowCard getRowCard() {
        return rowCard;
    }

    public CardExplanation getCardExplanation() {
        return cardExplanation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescriptionCardImagePath() {
        return getCardName().getDescriptionCardAddress();
    }

    public boolean isTransitioning() {
        return isTransitioning;
    }

    public void setTransiting(boolean transitioning) {
        isTransitioning = transitioning;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public CardNames getCardName() {
        return this.name;
    }

    public CardPlace getCardPlace() {
        return cardPlace;
    }

    public CardStates getPreviousState() {
        return previousState;
    }

    public void setPreviousState(CardStates previousState) {
        this.previousState = previousState;
    }

    public void setCardPlace(CardPlace cardPlace) {
        this.cardPlace = cardPlace;
    }

    abstract public void putInField();

    abstract public void removeFromField();

    public BoardSection getCurrentPlaceInGame() {
        return currentPlaceInGame;
    }

    public void setCurrentPlaceInGame(BoardSection currentPlaceInGame) {
        this.currentPlaceInGame = currentPlaceInGame;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public CardStates getCurrentState() {
        return currentState;
    }

    public ArrayList<CardAbility> getCardAbilities() {
        return cardAbilities;
    }

    public CardNames getName() {
        return name;
    }

    public void setName(CardNames name) {
        this.name = name;
    }

    public void setCardExplanation(CardExplanation cardExplanation) {
        this.cardExplanation = cardExplanation;
    }

    public void applyAbility() {
        if (this instanceof UnitCard) {
            ((UnitCard) this).setRow((Row) currentPlaceInGame);
        }

        for (CardAbility cardAbility : cardAbilities) {
            cardAbility.applyAbility();
        }
    }

    public CardNames getCardNameFromString(String cardNameStr) {
        for (CardNames cardName : CardNames.values()) {
            if (cardName.getCardNameString().equals(cardNameStr))
                return cardName;
        }

        return null;
    }

    public void printCardDetail() {
        System.out.println("card explanation: " + cardExplanation);
        System.out.println("description card: " + descriptionCard);
        System.out.println("row card: " + rowCard);
        System.out.println("player: " + player);
        System.out.println("owner: " + cardExplanation);
        System.out.println("card explanation: " + owner);
        System.out.println("current place: " + currentPlaceInGame);
    }
}
