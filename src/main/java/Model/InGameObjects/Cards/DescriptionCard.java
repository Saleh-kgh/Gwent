package Model.InGameObjects.Cards;

import view.Controller.ClickEvaluator.DescriptionCardClickEvaluator;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import Enum.*;

public class DescriptionCard extends Rectangle {

    private final Card card;

    public DescriptionCard(Card card) {
        super(Sizes.DESCRIPTIVE_CARD.getWidth(), Sizes.DESCRIPTIVE_CARD.getHeight());
        this.card = card;
        this.setFill(new ImagePattern(new Image(getClass().getResource
                    (card.getCardName().getDescriptionCardAddress()).toExternalForm())));

        setOnMouseClicked(event -> {
           new DescriptionCardClickEvaluator(card.getPlayer()).manageClickCommand(this);
        });
    }

    public Card getCard() {
        return card;
    }
}
