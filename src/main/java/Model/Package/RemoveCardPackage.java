package Model.Package;

import Model.InGameObjects.Board.BoardSection;
import Model.InGameObjects.Cards.Card;
import Enum.CardStates;

import java.io.Serializable;

public class RemoveCardPackage implements Serializable {

    private int cardId;
    private String cardState;
    private String destination;
    private boolean isForPuttingNextRound;

    public RemoveCardPackage(Card card, BoardSection destination, CardStates cardState, boolean isForPuttingNextRound) {
        this.cardId = card.getId();
        this.cardState = cardState.getCardStateString();
        this.destination = destination.getCardPlace().getCardPlaceString();
        this.isForPuttingNextRound = isForPuttingNextRound;
    }

    public int getCardId() {
        return cardId;
    }

    public String getDestination() {
        return destination;
    }

    public String getCardState() {
        return cardState;
    }

    public boolean isForPuttingNextRound() {
        return isForPuttingNextRound;
    }
}
