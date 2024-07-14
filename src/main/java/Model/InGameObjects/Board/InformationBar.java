package Model.InGameObjects.Board;

import Model.Game.Player;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class InformationBar {

    private Player player;
    private Player owner;
    private boolean isOpponent;
    private Pane board;
    private Circle playerProfile;
    private Rectangle playerFactionShield;
    private Rectangle cardsIcon;
    private Rectangle passedIcon;
    private Circle gem1;
    private Circle gem2;
    private Label handCardsCount;
    private Label factionName;
    private Label playerName;

    public InformationBar(Board board, Player player, Player owner) {
        this.player = player;
        this.owner = owner;
        this.board = board;
        isOpponent = !owner.equals(player);

        setUpNameText();
        setUpFactionText();
        setUpPlayerProfile();
        setUpFactionShield();
        setUpCardsIcon();
        setUpCardsCount();
        setUpGems();
        setUpPassedIcon();
        addComponentsToPane();
    }

    public void updateInfoBar() {
        changeCardsCount();
    }

    private void addComponentsToPane() {
        board.getChildren().addAll(playerProfile, playerFactionShield,
                    cardsIcon, handCardsCount, playerName, factionName, gem1, gem2);
    }

    private void setUpNameText() {
        playerName = new Label(owner.getUser().getNickname());
        playerName.setStyle("-fx-text-fill: #af8c2e; -fx-font-size: 15");
        playerName.setLayoutX(240);
        playerName.setLayoutY(745);
        if (isOpponent)
            playerName.setLayoutY(285);
    }

    private void setUpFactionText() {
        factionName = new Label(owner.getPlayersFaction().toString());
        factionName.setStyle("-fx-text-fill: #e1d5b0; -fx-font-size: 10");

        factionName.setLayoutX(240);
        factionName.setLayoutY(771);
        if (isOpponent)
            factionName.setLayoutY(313);
    }

    private void setUpPlayerProfile() {
        playerProfile = new Circle(55);
        playerProfile.setFill(new ImagePattern(new Image(getClass().getResource
                ("/Pics/Icons/InGameStatics/profile.png").toExternalForm())));
        playerProfile.setStroke(Color.web("#92afee"));
        playerProfile.setStrokeWidth(4);

        playerProfile.setLayoutX(177);
        playerProfile.setLayoutY(733);
        if (isOpponent)
            playerProfile.setLayoutY(340);
    }

    private void setUpFactionShield() {
        playerFactionShield = new Rectangle(45, 50);
        playerFactionShield.setFill(new ImagePattern(new Image(getClass().getResource
                (owner.getPlayersFaction().giveFactionShieldAddress()).toExternalForm())));

        playerFactionShield.setLayoutX(100);
        playerFactionShield.setLayoutY(750);
        if (isOpponent)
            playerFactionShield.setLayoutY(280);
    }

    private void setUpCardsIcon() {
        cardsIcon = new Rectangle(30, 40);
        cardsIcon.setFill(new ImagePattern(new Image(getClass().getResource
                ("/Pics/Icons/PreGameIcons/icon_card_count.png").toExternalForm())));

        cardsIcon.setLayoutX(240);
        cardsIcon.setLayoutY(690);
        if (isOpponent)
            cardsIcon.setLayoutY(355);
    }

    private void setUpCardsCount() {
        String countStr = String.format("%d", owner.getHand().size());
        handCardsCount = new Label(countStr);
        handCardsCount.setStyle("-fx-text-fill: #8c6f24; -fx-font-size: 20");

        handCardsCount.setLayoutX(275);
        handCardsCount.setLayoutY(690);
        if (isOpponent)
            handCardsCount.setLayoutY(355);
    }

    private void setUpGems() {
        gem1 = new Circle(20);
        gem1.setFill(new ImagePattern(new Image(getClass().getResource
                ("/Pics/Icons/InGameStatics/icon_gem_on.png").toExternalForm())));
        gem1.setLayoutX(320);
        gem1.setLayoutY(710);
        if (isOpponent)
            gem1.setLayoutY(370);

        gem2 = new Circle(20);
        gem2.setFill(new ImagePattern(new Image(getClass().getResource
                ("/Pics/Icons/InGameStatics/icon_gem_on.png").toExternalForm())));
        gem2.setLayoutX(360);
        gem2.setLayoutY(710);
        if (isOpponent)
            gem2.setLayoutY(370);
    }

    public void changeCardsCount() {
        String countStr = String.format("%d", owner.getHand().size());
        handCardsCount.setText(countStr);
    }

    public void breakGem() {
        if (owner.getRemainingGems() == 2) {
            return;
        } else if (owner.getRemainingGems() == 1) {
            gem2.setFill(new ImagePattern(new Image(getClass().getResource
                    ("/Pics/Icons/InGameStatics/icon_gem_off.png").toExternalForm())));
        } else {
            gem1.setFill(new ImagePattern(new Image(getClass().getResource
                    ("/Pics/Icons/InGameStatics/icon_gem_off.png").toExternalForm())));
        }
    }

    public void recoverGem() {
        gem2.setFill(new ImagePattern(new Image(getClass().getResource
                ("/Pics/Icons/InGameStatics/icon_gem_on.png").toExternalForm())));
    }

    private void setUpPassedIcon() {
        passedIcon = new Rectangle(103, 33);
        passedIcon.setFill(new ImagePattern(new Image(getClass().getResource(
                    "/Pics/Icons/InGameStatics/PassedIcon.png").toExternalForm())));

        passedIcon.setLayoutX(410);
        passedIcon.setLayoutY(670);

        if (isOpponent)
            passedIcon.setLayoutY(270);
    }

    public void addPassedIconToPane() {
        board.getChildren().add(passedIcon);
    }

    public void removePassedIconFromPane() {
        board.getChildren().remove(passedIcon);
    }
}
