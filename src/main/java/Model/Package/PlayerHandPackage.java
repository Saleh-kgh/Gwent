package Model.Package;

import Model.InGameObjects.Cards.Card;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerHandPackage implements Serializable {

    private int [] cardIds;

    public PlayerHandPackage(ArrayList<Card> handCards) {
        getCardNamesFromHandCards(handCards);
    }

    private void getCardNamesFromHandCards(ArrayList<Card> handCards) {
        cardIds = new int[handCards.size()];

        for (int i = 0; i < handCards.size(); i++) {
            cardIds[i] = handCards.get(i).getId();
        }
    }

    public int[] getCardIds() {
        return cardIds;
    }
}
