package Model.InGameObjects.Cards;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import Enum.*;
import javafx.scene.shape.Rectangle;

public class BackOfCard extends Rectangle {

    private final Card card;

    public BackOfCard(Card card) {
        super(Sizes.ROW_CARD.getWidth(), Sizes.ROW_CARD.getHeight());
        this.card = card;
        String backImageAddress = card.getCardName().getFactionBackAddressByName(card.getCardName().getFactionNameAsEnum());
        this.setFill(new ImagePattern(new Image(getClass().getResource
                (backImageAddress).toExternalForm())));
    }

    public Card getCard() {
        return card;
    }
}
