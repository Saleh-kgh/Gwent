package Model.InGameObjects.Board;

import Model.Game.Player;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import Enum.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class DeckArea extends BoardSection{
    private static final double prefWidthOfDeckArea = Sizes.DECK_AREA.getWidth();
    private static final double prefHeightOfDeckArea = Sizes.DECK_AREA.getHeight();
    private static final double layoutXOnBoardOfDeckArea = 1723;
    private static final double layoutYOnBoardOfDeckArea = 68;
    private static final double layoutYDiffer = 758;

    private Label deckCardsCount;
    private Rectangle factionBack;

    public DeckArea(Boolean isFriendly, Player player, Player owner) {
        super(prefWidthOfDeckArea, prefHeightOfDeckArea, layoutXOnBoardOfDeckArea,
                    layoutYOnBoardOfDeckArea, player, owner);
        if (isFriendly) {
            super.setLayoutYOnBoard(layoutYOnBoardOfDeckArea + layoutYDiffer);
            super.setCardPlace(CardPlace.FRIENDLY_DECK_AREA);
        }
        else {
            super.setCardPlace(CardPlace.OPPONENT_DECK_AREA);
        }

        setBackgroundImage("/Pics/BoardObjects/DeckArea.png");
        setUpFactionBack();
        setUpCardCountLabel();
    }

    private void setUpCardCountLabel() {
        String countStr = String.format("%d", getOwner().getDeck().size());
        deckCardsCount = new Label(countStr);

        deckCardsCount.setStyle("-fx-font-size: 20; -fx-text-fill: White;");
        deckCardsCount.setBackground(new Background(new BackgroundFill(Color.gray(0.6), CornerRadii.EMPTY, Insets.EMPTY)));

        deckCardsCount.setLayoutX(50);
        deckCardsCount.setLayoutY(100);

        this.getChildren().add(deckCardsCount);
    }

    private void setUpFactionBack() {
        factionBack = new Rectangle(100, 135);
        factionBack.setFill(new ImagePattern(new Image(getClass().getResource(
                    getOwner().getPlayersFaction().giveFactionBackAddress()).toExternalForm())));

        factionBack.setLayoutX(6);
        factionBack.setLayoutY(6);

        this.getChildren().add(factionBack);
    }

    private void updateCardCountLabel() {
        String countStr = String.format("%d", getOwner().getDeck().size());
        deckCardsCount.setText(countStr);
    }

    private void updateFactionBack() {
        if (getOwner().getDeck().isEmpty()) {
            this.getChildren().remove(factionBack);
        }
        else if (!this.getChildren().contains(factionBack)) {
            this.getChildren().add(factionBack);
        }
    }

    @Override
    public void updateSection() {
        updateCardCountLabel();
        updateFactionBack();
    }
}
