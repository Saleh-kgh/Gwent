package Model.Package;

import Model.InGameObjects.Board.BoardSection;
import Model.InGameObjects.Cards.Card;
import Enum.CardStates;

import java.io.Serializable;

public class MoveCardPackage implements Serializable {

    private int cardId;
    private String beginning;
    private String destination;
    private String cardState;

    public MoveCardPackage(Card card, BoardSection beginning, BoardSection destination, CardStates cardState) {
        this.cardId = card.getId();

        this.beginning = beginning.getCardPlace().getCardPlaceString();

        this.destination = destination.getCardPlace().getCardPlaceString();

        this.cardState = cardState.getCardStateString();
    }

    public int getCardId() {
        return cardId;
    }

    public String getBeginning() {
        return beginning;
    }

    public String getDestination() {
        return destination;
    }

    public String getCardState() {
        return cardState;
    }
}
