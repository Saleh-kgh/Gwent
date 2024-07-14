package Model.InGameObjects.Cards;

import view.Controller.ClickEvaluator.RowCardClickEvaluator;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import Enum.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import view.AppView;

public class RowCard extends Pane {

    private Card card;
    private Rectangle backGround;
    private Label powerLabel;
    private Rectangle powerBackground;
    private Rectangle cardPlaceImage;
    private Rectangle abilityImage;

    public RowCard(Card card) {
        this.card = card;
        setWidth(Sizes.ROW_CARD.getWidth());
        setHeight(Sizes.ROW_CARD.getHeight());

        createBackGround();
        if (card instanceof UnitCard) {
            createPowerBackground();
            createPowerLabel();
            createCardPlaceImage();
        }
        if (!(card instanceof LeaderCard)) {
            if (!card.getCardAbilities().isEmpty())
                createAbilityImage();
        }

        setOnMouseClicked(event -> {
            event.consume();
            new RowCardClickEvaluator(card.getPlayer()).manageClickCommand(this);
        });

        setOnMouseEntered(event -> {
            if (card.getCurrentState() != null) {
                if (card.getCurrentState().equals(CardStates.ROW_IN_GAME_HAND)) {
                    giveDragEffects();
                }
            }
        });

        setOnMouseExited(event -> {
            if (card.getCurrentState() != null) {
                if (card.getCurrentState().equals(CardStates.ROW_IN_GAME_HAND)) {
                    removeDragEffects();
                }
            }
        });
    }

    private void giveDragEffects() {
        AppView.playSfxAudio("card");

        DropShadow glow = new DropShadow();
        glow.setColor(Color.GOLD);
        glow.setRadius(10);
        glow.setSpread(0.3);
        this.setEffect(glow);
        this.setLayoutY(this.getLayoutY() - 10);
    }

    private void removeDragEffects() {
        this.setEffect(null);
        this.setLayoutY(this.getLayoutY() + 10);
    }

    private void createBackGround() {
        backGround = new Rectangle(Sizes.ROW_CARD.getWidth(), Sizes.ROW_CARD.getHeight());
        try {
            backGround.setFill(new ImagePattern(new Image(getClass().getResource
                    (card.getCardName().getRowCardAddress()).toExternalForm())));
        } catch (Exception e) {
            System.out.println(card.getCardName());
        }

        this.getChildren().add(backGround);
        backGround.setLayoutX(0);
        backGround.setLayoutY(0);
    }

    private void createPowerBackground() {
        if (!(card instanceof UnitCard))
            return;

        powerBackground = new Rectangle(Sizes.ROW_CARD_POWER_ICON.getWidth(), Sizes.ROW_CARD_POWER_ICON.getHeight());

        if (!((UnitCard) card).isHero()) {
            powerBackground.setFill(new ImagePattern(new Image(getClass().getResource
                    ("/Pics/Icons/UnitCardAbilities/power_normal.png").toExternalForm())));
        }
        else {
            powerBackground.setFill(new ImagePattern(new Image(getClass().getResource
                    ("/Pics/Icons/UnitCardAbilities/power_hero.png").toExternalForm())));
        }

        this.getChildren().add(powerBackground);
        powerBackground.setLayoutX(-2);
        powerBackground.setLayoutY(-2);
    }

    private void createPowerLabel() {
        String basePower = String.format("%d", ((UnitCard) card).getBasePower());
        powerLabel = new Label(basePower);
        powerLabel.setPrefSize(30, 30);
        powerLabel.setAlignment(Pos.CENTER);
        if (!((UnitCard) card).isHero())
            powerLabel.setStyle("-fx-text-fill: black;");
        else
            powerLabel.setStyle("-fx-text-fill: white;");

        this.getChildren().add(powerLabel);
        powerLabel.setLayoutX(0);
        powerLabel.setLayoutY(0);
    }

    private void createCardPlaceImage() {
        String rowType = "";
        switch (card.getCardPlace()) {
            case FRIENDLY_CLOSE, OPPONENT_CLOSE:
                rowType = "card_row_close.png";
                break;
            case FRIENDLY_RANGED, OPPONENT_RANGED:
                rowType = "card_row_ranged.png";
                break;
            case FRIENDLY_SIEGE, OPPONENT_SIEGE:
                rowType = "card_row_siege.png";
                break;
            case FRIENDLY_CLOSE_RANGED, OPPONENT_CLOSE_RANGED:
                rowType = "card_row_agile.png";
                break;
        }

        cardPlaceImage = new Rectangle(Sizes.ROW_CARD_ROW_ICON.getWidth(), Sizes.ROW_CARD_ROW_ICON.getWidth());
        cardPlaceImage.setFill(new ImagePattern(new Image(getClass().getResource
                        ("/Pics/Icons/RowIcons/" + rowType).toExternalForm())));

        this.getChildren().add(cardPlaceImage);
        cardPlaceImage.setLayoutX(Sizes.ROW_CARD.getWidth() - Sizes.ROW_CARD_ROW_ICON.getWidth() - 2);
        cardPlaceImage.setLayoutY(Sizes.ROW_CARD.getHeight() - Sizes.ROW_CARD_ROW_ICON.getHeight() - 2);
    }

    private void createAbilityImage() {
        abilityImage = new Rectangle(Sizes.ROW_CARD_ABILITY_ICON.getWidth(), Sizes.ROW_CARD_ABILITY_ICON.getHeight());
        abilityImage.setFill(new ImagePattern(new Image(getClass().getResource
                    (card.getCardAbilities().get(0).getAbilityIcon()).toExternalForm())));

        int isUnitCard = card instanceof UnitCard ? 2 : 1;

        this.getChildren().add(abilityImage);
        abilityImage.setLayoutX(Sizes.ROW_CARD.getWidth() - isUnitCard * (Sizes.ROW_CARD_ABILITY_ICON.getWidth() + 2));
        abilityImage.setLayoutY(Sizes.ROW_CARD.getHeight() - Sizes.ROW_CARD_ABILITY_ICON.getHeight() - 2);
    }

    public void setPowerLabelText() {
        UnitCard unitCard = (UnitCard) card;

        int currentPower = unitCard.getCurrentPower();
        String currentPowerStr = String.format("%d", currentPower);
        powerLabel.setText(currentPowerStr);

        if (currentPower < unitCard.getBasePower()) {
            powerLabel.setStyle("-fx-text-fill: #e82323");
        }
        else if (currentPower > unitCard.getBasePower()) {
            powerLabel.setStyle("-fx-text-fill: #39a239");
        }
        else {
            if (!((UnitCard) card).isHero())
                powerLabel.setStyle("-fx-text-fill: black");
            else
                powerLabel.setStyle("-fx-text-fill: white");
        }
        powerLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
    }

    public Card getCard() {
        return card;
    }

    public Rectangle getBackGround() {
        return backGround;
    }

    public Label getPowerLabel() {
        return powerLabel;
    }

    public Rectangle getPowerBackground() {
        return powerBackground;
    }

    public Rectangle getCardPlaceImage() {
        return cardPlaceImage;
    }

    public Rectangle getAbilityImage() {
        return abilityImage;
    }
}
