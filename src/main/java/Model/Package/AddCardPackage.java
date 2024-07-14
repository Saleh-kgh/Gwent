package Model.Package;

import Model.InGameObjects.Board.BoardSection;
import Model.InGameObjects.Cards.Card;
import Enum.CardStates;

import java.io.Serializable;

public class AddCardPackage implements Serializable {

    private int cardId;
    private String cardState;
    private String destination;
    private boolean isForPuttingNextRound;

    public AddCardPackage(Card card, BoardSection destination, CardStates cardState, boolean isForPuttingNextRound) {
        this.cardId = card.getId();
        this.cardState = cardState.getCardStateString();
        this.destination = destination.getCardPlace().getCardPlaceString();
        this.isForPuttingNextRound = isForPuttingNextRound;
    }

    public int getCardId() {
        return cardId;
    }

    public String getCardState() {
        return cardState;
    }

    public String getDestination() {
        return destination;
    }

    public boolean isForPuttingNextRound() {
        return isForPuttingNextRound;
    }
}
