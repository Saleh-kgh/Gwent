package Model.Package;

import java.io.Serializable;

public class PlayerTurnPackage implements Serializable {

    private int cardsInHandCount;
    private int cardsInDeckCount;
    private boolean hasPassed;

    public PlayerTurnPackage(int cardsInHandCount, int cardsInDeckCount, boolean hasPassed) {
        this.cardsInHandCount = cardsInHandCount;
        this.cardsInDeckCount = cardsInDeckCount;
        this.hasPassed = hasPassed;
    }

    public int getCardsInHandCount() {
        return cardsInHandCount;
    }

    public int getCardsInDeckCount() {
        return cardsInDeckCount;
    }

    public boolean isHasPassed() {
        return hasPassed;
    }
}
