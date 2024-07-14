package view.Controller;

import Model.InGameObjects.Board.Board;
import Model.InGameObjects.Board.BoardSection;
import Model.InGameObjects.Board.ScrollBox;
import Model.Game.GameObject;
import Model.Game.Player;
import Model.InGameObjects.Cards.*;
import Model.User.User;
import controller.Client;
import controller.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import view.Animations.AbilityAnimation;
import view.Animations.FadingAnimation;
import view.AppView;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import Enum.*;
import view.Transitions.AddTransition;

public class GameViewController extends ViewController implements Initializable {

    private final int notificationDuration = 2000;

    private Rectangle cheatButton;
    private Rectangle exitButton;
    private Rectangle passButton;
    private Button submitButton;
    private Label waiting;
    private Line glowingUpperLine;
    private Line glowingLowerLine;
    private TextField textField;
    @FXML
    Pane pane;
    private Board currentBoard;
    private Player currentPlayer;
    private ScrollPane chatScrollPane;
    private VBox chatVBox;
    private TextArea chatTextArea;
    private Button chatSendButton;
    private static GameViewController currentGameViewController;
    private User userOfPlayer;
    private User userOfOpponent;
    private boolean amIFirst;

    public Board getCurrentBoard() {
        return currentBoard;
    }

    public void setCurrentBoard(Board currentBoard) {
        this.currentBoard = currentBoard;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.setPane(this.pane);
        setScaleOnInitialization();

        currentGameViewController = this;
        Client.client.startNewRandomGame();

        displayWaitingForOpponent();
    }

    private void displayWaitingForOpponent() {
        waiting = new Label("Random match making...");
        waiting.setStyle("-fx-text-fill: white; -fx-font-size: 24; -fx-background-color: black");

        waiting.setLayoutX(900);
        waiting.setLayoutY(520);
        pane.getChildren().add(waiting);
    }

    private void removeWaitingForOpponent() {
        pane.getChildren().remove(waiting);
    }

    public void startGame() {
        removeWaitingForOpponent();

        adjustInitialChat();

        ArrayList<Card> player1Deck = new ArrayList<>();
        player1Deck.addAll(userOfPlayer.getCardInventory().getCurrentFaction().getDeck());

        Player player1 = new Player(userOfPlayer, userOfPlayer.getCardInventory().getCurrentFaction().getFactionName(),
                player1Deck, (LeaderCard) userOfPlayer.getCardInventory().getCurrentFaction().getCurrentLeader());

        ArrayList<Card> player2Deck = new ArrayList<>();
        player2Deck.addAll(userOfOpponent.getCardInventory().getCurrentFaction().getDeck());

        Player player2 = new Player(userOfOpponent, userOfOpponent.getCardInventory().getCurrentFaction().getFactionName(),
                player2Deck, (LeaderCard) userOfOpponent.getCardInventory().getCurrentFaction().getCurrentLeader());

        amIFirst = Objects.equals(userOfPlayer.getUsername(), Client.client.getUser().getUsername());


        if (amIFirst) {
            setCurrentPlayer(player1);
        } else {
            setCurrentPlayer(player2);
        }

        player1.setOpponent(player2);
        player2.setOpponent(player1);

        player1.setBoard(new Board(player1));
        player2.setBoard(new Board(player2));

        for (Card card : player1Deck) {
            card.createGraphicalFields();
            card.setPlayer(currentPlayer);
            if (player1.equals(currentPlayer))
                card.setCurrentPlaceInGame(currentPlayer.getBoard().getFriendlyDeckArea());
            else
                card.setCurrentPlaceInGame(currentPlayer.getBoard().getOpponentDeckArea());
        }
        player1.getLeaderCard().createGraphicalFields();
        player1.getLeaderCard().setPlayer(currentPlayer);

        for (Card card : player2Deck) {
            card.createGraphicalFields();
            card.setPlayer(currentPlayer);
            if (player2.equals(currentPlayer))
                card.setCurrentPlaceInGame(currentPlayer.getBoard().getFriendlyDeckArea());
            else
                card.setCurrentPlaceInGame(currentPlayer.getBoard().getOpponentDeckArea());
        }
        player2.getLeaderCard().createGraphicalFields();
        player2.getLeaderCard().setPlayer(currentPlayer);

        currentBoard = getCurrentPlayer().getBoard();

        GameObject gameObject = new GameObject(player1, player2);
        GameController.currentGameObject = gameObject;

        gameObject.setCurrentPlayer(currentPlayer);

        currentPlayer.getUser().setOpponentUser(currentPlayer.getOpponent().getUser());

        gameObject.setGameViewController(this);

        getCurrentPlayer().assignGameObject();
        getCurrentPlayer().getOpponent().assignGameObject();

        getCurrentPlayer().getGameController().setUpPlayerHandCards();
        getCurrentPlayer().getGameController().setUpPlayerDeckCards();
        addLeaderCardsToBoard();

        getCurrentPlayer().setPlayerState(PlayerState.SWAPPING_INITIAL_HAND);

        getCurrentPlayer().getOpponent().setPlayerState(PlayerState.WAITING_FOR_OPPONENT);

        pane.getChildren().add(currentBoard);
        getCurrentPlayer().getBoard(). updateAllSections();
        getCurrentPlayer().setGamePane(pane);
        getCurrentPlayer().getOpponent().setGamePane(pane);
        createButtons();
        placePlayerHandCards();
        placePlayerDeckCards();

        if (amIFirst) giveGlowingTurnRectangleTo(true);
        else giveGlowingTurnRectangleTo(false);

        currentBoard.setScrollBox(new ScrollBox(getCurrentPlayer(),
                getCurrentPlayer().getBoard().getHandArea().getCardsOnSection(), 2, true));
        for (Card card : currentBoard.getHandArea().getCardsOnSection()) {
            card.setCurrentState(CardStates.DES_IN_GAME_SELECTABLE);
        }
        showScrollShowBox(currentBoard.getScrollBox());
    }
    public void exitButtonAction() {
        if (getCurrentPlayer().getPlayerState().equals(PlayerState.PARALYZED) || getCurrentPlayer().isParalyzedByAnimation())
            return;

        Runnable onConfirm = () -> {
            exitToMainMenu();
            giveExitingToMainMenuNotification();
            Client.client.endGame(getCurrentPlayer().getUser().getUsername());
        };
        throwConfirmAlert("Forfeiting Game", "Do you want to exit current game? it will be considered as defeat.", onConfirm);
    }

    public void exitToMainMenu() {
        getCurrentPlayer().getGameController().closeGame();
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(notificationDuration));
        pauseTransition.setOnFinished(event -> {
            AppView.setRootForMenu(Menus.Main.getValue());
        });
        pauseTransition.play();
    }

    public void giveExitingToMainMenuNotification() {
        displayNotifBar("Exiting Current Game!");
        displayNotifIcon("/Pics/Icons/NotificationIcons/notif_round_passed.png");
    }

    public void showEndGameResult(boolean hasPlayerWon, boolean isResultDraw) {
        Rectangle rectangle = new Rectangle(1920, 1080);
        rectangle.setFill(Color.rgb(0, 0, 0, 0.7));
        rectangle.setLayoutX(0);
        rectangle.setLayoutY(0);
        pane.getChildren().add(rectangle);

        Label playerName = new Label(getCurrentPlayer().getUser().getNickname());
        playerName.setStyle("-fx-font-size: 30; -fx-text-fill: GOLD");
        Label opponentName = new Label(getCurrentPlayer().getOpponent().getUser().getNickname());
        opponentName.setStyle("-fx-font-size: 30; -fx-text-fill: GOLD");

        playerName.setLayoutX(500);
        opponentName.setLayoutX(500);
        playerName.setLayoutY(600);
        opponentName.setLayoutY(700);
        pane.getChildren().addAll(playerName, opponentName);

        for (int i = 0; i < getCurrentPlayer().getGameObject().getGameHistory().getRoundsPlayed(); i++) {
            Label roundLabel = new Label("Round " + (i + 1));
            roundLabel.setLayoutX(750 + i * 250);
            roundLabel.setLayoutY(500);
            roundLabel.setStyle("-fx-font-size: 30; -fx-text-fill: GOLD");
            pane.getChildren().add(roundLabel);

            String playerScoreStr;
            String opponentScoreStr;
            if (getCurrentPlayer().getUser().equals(getCurrentPlayer().getGameObject().getGameHistory().getUser1())) {
                playerScoreStr = String.format("%d", getCurrentPlayer().getGameObject().getGameHistory().getUser1EveryRoundScore().get(i));

                opponentScoreStr = String.format("%d", getCurrentPlayer().getGameObject().getGameHistory().getUser2EveryRoundScore().get(i));
            }
            else {
                playerScoreStr = String.format("%d", getCurrentPlayer().getGameObject().getGameHistory().getUser2EveryRoundScore().get(i));

                opponentScoreStr = String.format("%d", getCurrentPlayer().getGameObject().getGameHistory().getUser1EveryRoundScore().get(i));
            }

            Label playerScore = new Label(playerScoreStr);
            playerScore.setLayoutX(770 + i * 250);
            playerScore.setLayoutY(600);
            playerScore.setStyle("-fx-font-size: 30; -fx-text-fill: white");
            pane.getChildren().add(playerScore);

            Label opponentScore = new Label(opponentScoreStr);
            opponentScore.setLayoutX(770 + i * 250);
            opponentScore.setLayoutY(700);
            opponentScore.setStyle("-fx-font-size: 30; -fx-text-fill: white");
            pane.getChildren().add(opponentScore);
        }

        Button quitButton = new Button("Quit");
        quitButton.setLayoutX(940);
        quitButton.setLayoutY(900);
        pane.getChildren().add(quitButton);

        quitButton.setOnMouseClicked(event -> {
            exitToMainMenu();
        });

        if (isResultDraw) announceGameDraw();
        else if (hasPlayerWon) announceGameWon();
        else announceGameLost();
    }

    public void addLeaderCardsToBoard() {
        getCurrentPlayer().getLeaderCard().createGraphicalFields();
        getCurrentPlayer().getLeaderCard().setCurrentState(CardStates.ROW_IN_GAME_HAND);
        getCurrentPlayer().getLeaderCard().setPlayer(currentPlayer);
        getCurrentPlayer().getLeaderCard().setCurrentPlaceInGame(
                    getCurrentPlayer().getBoard().getFriendlyLeaderCardArea());
        getCurrentPlayer().getOpponent().getLeaderCard().createGraphicalFields();
        getCurrentPlayer().getOpponent().getLeaderCard().setPlayer(getCurrentPlayer());
        getCurrentPlayer().getOpponent().getLeaderCard().setCurrentState(CardStates.ROW_IN_GAME_HAND);
        getCurrentPlayer().getOpponent().getLeaderCard().setCurrentPlaceInGame(
                    getCurrentPlayer().getBoard().getOpponentLeaderCardArea());

        getCurrentPlayer().getBoard().getFriendlyLeaderCardArea().addToCardsOnSection(
                getCurrentPlayer().getLeaderCard()
        );
        getCurrentPlayer().getBoard().getOpponentLeaderCardArea().addToCardsOnSection(
                getCurrentPlayer().getOpponent().getLeaderCard()
        );

        getCurrentPlayer().getOpponent().getBoard().getFriendlyLeaderCardArea().addToCardsOnSection(
                getCurrentPlayer().getOpponent().getLeaderCard()
        );
        getCurrentPlayer().getOpponent().getBoard().getOpponentLeaderCardArea().addToCardsOnSection(
                getCurrentPlayer().getLeaderCard()
        );
    }

    public void giveDarkEffectToCard(Card card) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        card.getRowCard().setEffect(colorAdjust);
    }

    public void removeDarkEffectFromCard(Card card) {
        card.getRowCard().setEffect(null);
    }

    public void showScrollShowBox(ScrollBox scrollBox) {

        showScrollCards(scrollBox);
        scrollBox.setCardsEffect();

        pane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (!scrollBox.isRemoved()) {
                double deltaX = event.getDeltaY();
                if (deltaX < -5) {
                    removeScrollCardsFromPane(scrollBox);
                    scrollBox.shiftCardsLeft();
                    try {
                        showScrollCards(scrollBox);
                    } catch (Exception e) {}
                } else if (deltaX > 5) {
                    removeScrollCardsFromPane(scrollBox);
                    scrollBox.shiftCardsRight();
                    try {
                        showScrollCards(scrollBox);
                    } catch (Exception e) {}
                }
            }
        });

    }

    public void removeScrollBox(ScrollBox scrollBox) {
        removeScrollCardsFromPane(scrollBox);
        scrollBox.setRemoved(true);

        scrollBox.getPlayer().getBoard().getChildren().remove(scrollBox);

        scrollBox = null;
    }

    private void removeScrollCardsFromPane(ScrollBox scrollBox) {
        if (scrollBox.getCenterCard() != null &&
                pane.getChildren().contains(scrollBox.getCenterCard().getDescriptionCard())) {
            pane.getChildren().remove(scrollBox.getCenterCard().getDescriptionCard());
        }
        if (scrollBox.getLeftCard1() != null &&
                pane.getChildren().contains(scrollBox.getLeftCard1().getDescriptionCard())) {
            pane.getChildren().remove(scrollBox.getLeftCard1().getDescriptionCard());
        }
        if (scrollBox.getLeftCard2() != null &&
                pane.getChildren().contains(scrollBox.getLeftCard2().getDescriptionCard())) {
            pane.getChildren().remove(scrollBox.getLeftCard2().getDescriptionCard());
        }
        if (scrollBox.getRightCard1() != null &&
                pane.getChildren().contains(scrollBox.getRightCard1().getDescriptionCard())) {
            pane.getChildren().remove(scrollBox.getRightCard1().getDescriptionCard());
        }
        if (scrollBox.getRightCard2() != null &&
                pane.getChildren().contains(scrollBox.getRightCard2().getDescriptionCard())) {
            pane.getChildren().remove(scrollBox.getRightCard2().getDescriptionCard());
        }
    }

    private void showScrollCards(ScrollBox scrollBox) {
        double leftCard1X = 200;
        double leftCard1Y = 300;
        double leftCard2X = 500;
        double leftCard2Y = 300;
        double centerCardX = 815;
        double centerCardY = 300;
        double rightCard1X = 1145;
        double rightCard1Y = 300;
        double rightCard2X = 1470;
        double rightCard2Y = 300;

        if (scrollBox.getLeftCard1() != null) {
            pane.getChildren().add(scrollBox.getLeftCard1().getDescriptionCard());
            scrollBox.getLeftCard1().getDescriptionCard().setLayoutX(leftCard1X);
            scrollBox.getLeftCard1().getDescriptionCard().setLayoutY(leftCard1Y);
        }
        if (scrollBox.getLeftCard2() != null) {
            pane.getChildren().add(scrollBox.getLeftCard2().getDescriptionCard());
            scrollBox.getLeftCard2().getDescriptionCard().setLayoutX(leftCard2X);
            scrollBox.getLeftCard2().getDescriptionCard().setLayoutY(leftCard2Y);
        }
        if (scrollBox.getCenterCard() != null) {
            pane.getChildren().add(scrollBox.getCenterCard().getDescriptionCard());
            scrollBox.getCenterCard().getDescriptionCard().setLayoutX(centerCardX);
            scrollBox.getCenterCard().getDescriptionCard().setLayoutY(centerCardY);
        }
        if (scrollBox.getRightCard1() != null) {
            pane.getChildren().add(scrollBox.getRightCard1().getDescriptionCard());
            scrollBox.getRightCard1().getDescriptionCard().setLayoutX(rightCard1X);
            scrollBox.getRightCard1().getDescriptionCard().setLayoutY(rightCard1Y);
        }
        if (scrollBox.getRightCard2() != null) {
            pane.getChildren().add(scrollBox.getRightCard2().getDescriptionCard());
            scrollBox.getRightCard2().getDescriptionCard().setLayoutX(rightCard2X);
            scrollBox.getRightCard2().getDescriptionCard().setLayoutY(rightCard2Y);
        }
    }

    public void placePlayerHandCards() {
        getCurrentPlayer().getBoard().getHandArea().setCardsOnSection(getCurrentPlayer().getHand());
        getCurrentPlayer().getBoard().getHandArea().updateSection();
    }

    public void placePlayerDeckCards() {
        getCurrentPlayer().getBoard().getFriendlyDeckArea().setCardsOnSection(getCurrentPlayer().getDeck());
        getCurrentPlayer().getBoard().getFriendlyDeckArea().updateSection();
    }

    public void displaySelectedCardDescriptionExplanation(Card card) {

        DescriptionCard descriptionCard = card.getDescriptionCard();

        CardExplanation cardExplanation = card.getCardExplanation();

        descriptionCard.setLayoutX(1550);
        descriptionCard.setLayoutY(300);
        pane.getChildren().add(descriptionCard);

        cardExplanation.setLayoutX(1500);
        cardExplanation.setLayoutY(700);
        cardExplanation.showDescriptionInScene(pane);
    }

    public void removeSelectedCardDescriptionAndExplanation(Card card) {
        pane.getChildren().remove(card.getDescriptionCard());
        card.getCardExplanation().removeDescriptionFromScene(pane);
    }

    public void giveGlowingTurnRectangleTo(boolean player) {
        if (glowingUpperLine == null) {
            glowingUpperLine = new Line(0, 665, 450, 665);
            glowingLowerLine = new Line(0, 805, 450, 805);

            glowingUpperLine.setStroke(Color.GOLD);
            glowingUpperLine.setStrokeWidth(3);
            glowingLowerLine.setStroke(Color.GOLD);
            glowingLowerLine.setStrokeWidth(3);

            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.GOLD);
            dropShadow.setRadius(20);
            dropShadow.setSpread(0.5);

            glowingUpperLine.setEffect(dropShadow);
            glowingLowerLine.setEffect(dropShadow);
        }

        if (player) {
            glowingUpperLine.setStartY(665);
            glowingUpperLine.setEndY(665);

            glowingLowerLine.setStartY(805);
            glowingLowerLine.setEndY(805);
        }
        else {
            glowingUpperLine.setStartY(265);
            glowingUpperLine.setEndY(265);

            glowingLowerLine.setStartY(405);
            glowingLowerLine.setEndY(405);
        }

        if (!pane.getChildren().contains(glowingUpperLine)) {
            pane.getChildren().add(glowingUpperLine);
            pane.getChildren().add(glowingLowerLine);
        }
    }

    public void playCardTransition(Card card, BoardSection destination) {
        AddTransition addTransition = new AddTransition(card, destination);
        addTransition.play();
    }

    public void playSfxFromString(String mediaName) {
        AppView.playSfxAudio(mediaName);
    }

    public void playAnimationOnCard(Card card, String animName) {
        if (animName.startsWith("anim_")) {
            new FadingAnimation(card.getRowCard(), animName).play();
        }
        else {
            new AbilityAnimation(card.getRowCard(), "/Pics/Icons/InGameEffects/anim_"
                    + animName + ".png").play();
        }

    }

    public void playRowOverlayAnimation(String animationName) {

        if (animationName.equals("overlay_frost") && getCurrentPlayer().getBoard().getFriendlyCloseArea().getWeatherEffect() == null) {
            new FadingAnimation(getCurrentPlayer().getBoard().getFriendlyCloseArea(), "overlay_frost").play();
            new FadingAnimation(getCurrentPlayer().getBoard().getOpponentCloseArea(), "overlay_frost").play();
            getCurrentPlayer().getGameObject().getGameViewController().playSfxFromString("cold");
        }
        else if (animationName.equals("overlay_fog") && getCurrentPlayer().getBoard().getFriendlyRangedArea().getWeatherEffect() == null) {
            new FadingAnimation(getCurrentPlayer().getBoard().getFriendlyRangedArea(), "overlay_fog").play();
            new FadingAnimation(getCurrentPlayer().getBoard().getOpponentRangedArea(), "overlay_fog").play();
            getCurrentPlayer().getGameObject().getGameViewController().playSfxFromString("fog");
        }
        else if (animationName.equals("overlay_rain") && getCurrentPlayer().getBoard().getFriendlySiegeArea().getWeatherEffect() == null) {
            new FadingAnimation(getCurrentPlayer().getBoard().getFriendlySiegeArea(), "overlay_rain").play();
            new FadingAnimation(getCurrentPlayer().getBoard().getOpponentSiegeArea(), "overlay_rain").play();
            getCurrentPlayer().getGameObject().getGameViewController().playSfxFromString("rain");
        }
    }

    public void moveButtonsToFront() {
        exitButton.toFront();
        passButton.toFront();
        cheatButton.toFront();
    }

    private void createButtons() {
        createExitButton();
        createPassButton();
        createCheatButton();
    }

    private void createExitButton() {
        exitButton = new Rectangle(101, 42);
        exitButton.setFill(new ImagePattern(new Image(getClass().getResource
                ("/Pics/Icons/InGameStatics/ExitButton.png").toExternalForm())));

        exitButton.setLayoutX(15);
        exitButton.setLayoutY(495);
        pane.getChildren().add(exitButton);

        exitButton.setOnMouseClicked(mouseEvent -> {
            exitButtonAction();
        });

        exitButton.setOnMouseMoved(event -> giveButtonStroke(exitButton));
        exitButton.setOnMouseExited(event -> removeButtonStroke(exitButton));
    }

    private void createPassButton() {
        passButton = new Rectangle(101, 42);
        passButton.setFill(new ImagePattern(new Image(getClass().getResource
                ("/Pics/Icons/InGameStatics/PassButton.png").toExternalForm())));

        passButton.setLayoutX(320);
        passButton.setLayoutY(880);
        pane.getChildren().add(passButton);

        passButton.setOnMouseClicked(mouseEvent -> {
            if (currentPlayer.getPlayerState().equals(PlayerState.PASSED) ||
                    currentPlayer.getPlayerState().equals(PlayerState.WAITING_FOR_OPPONENT) ||
                    currentPlayer.isParalyzedByAnimation())
                return;

            passButton.setDisable(true);

            getCurrentPlayer().getGameController().passRound();
            getCurrentPlayer().getGameController().giveTurnWithDelay();

            PauseTransition pause = new PauseTransition(Duration.millis(3000));
            pause.setOnFinished(event -> {
                passButton.setDisable(false);
            });
            pause.play();
        });

        passButton.setOnMouseMoved(event -> giveButtonStroke(passButton));
        passButton.setOnMouseExited(event -> removeButtonStroke(passButton));
    }

    private void createCheatButton() {
        cheatButton = new Rectangle(101, 42);
        cheatButton.setFill(new ImagePattern(new Image(getClass().getResource
                ("/Pics/Icons/InGameStatics/CheatButton.png").toExternalForm())));

        cheatButton.setLayoutX(320);
        cheatButton.setLayoutY(820);
        pane.getChildren().add(cheatButton);

        cheatButton.setOnMouseClicked(mouseEvent -> {
            if (currentPlayer.getPlayerState().equals(PlayerState.PASSED) ||
                    currentPlayer.getPlayerState().equals(PlayerState.WAITING_FOR_OPPONENT) ||
                    currentPlayer.isParalyzedByAnimation())
                return;

            showCheatTextField();
        });

        cheatButton.setOnMouseMoved(event -> giveButtonStroke(cheatButton));
        cheatButton.setOnMouseExited(event -> removeButtonStroke(cheatButton));
    }

    private void giveButtonStroke(Rectangle button) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.web("#cc9933"));
        dropShadow.setRadius(10);
        dropShadow.setSpread(0.3);

        Glow glow = new Glow();
        glow.setLevel(0.6);

        button.setEffect(dropShadow);
        dropShadow.setInput(glow);
    }

    private void removeButtonStroke(Rectangle button) {
        button.setEffect(null);
    }

    public void announceGameWon() {
        displayEndGameIcon("/Pics/Icons/NotificationIcons/end_win.png");
        AppView.playSfxAudio("game_win");
    }

    public void announceGameLost() {
        displayEndGameIcon("/Pics/Icons/NotificationIcons/end_lose.png");
        AppView.playSfxAudio("game_lose");
    }

    public void announceGameDraw() {
        displayEndGameIcon("/Pics/Icons/NotificationIcons/end_draw.png");
        AppView.playSfxAudio("game_lose");
    }

    public void announceRoundWon() {
        displayNotifBar("Round Won!");
        displayNotifIcon("/Pics/Icons/NotificationIcons/notif_win_round.png");
        AppView.playSfxAudio("round_win");
    }

    public void announceRoundLost() {
        displayNotifBar("Round Lost!");
        displayNotifIcon("/Pics/Icons/NotificationIcons/notif_lose_round.png");
        AppView.playSfxAudio("round_lose");
    }

    public void announceRoundDraw() {
        displayNotifBar("Round Draw!");
        displayNotifIcon("/Pics/Icons/NotificationIcons/notif_draw_round.png");
        AppView.playSfxAudio("resilience");
    }

    public void announceRoundPassed() {
        getCurrentPlayer().getBoard().getPlayerInformationBar().addPassedIconToPane();
        displayNotifBar("Passed!");
        displayNotifIcon("/Pics/Icons/NotificationIcons/notif_round_passed.png");
    }

    public void announceOpponentTurn() {
        displayNotifBar("Opponent's Turn!");
        displayNotifIcon("/Pics/Icons/NotificationIcons/notif_op_turn.png");
        AppView.playSfxAudio("turn_op");
    }

    public void announcePlayerTurn() {
        displayNotifBar("Your Turn!");
        displayNotifIcon("/Pics/Icons/NotificationIcons/notif_me_turn.png");
        AppView.playSfxAudio("turn_me");
    }

    public void announcePlayerGoesFirst() {
        displayNotifBar("You Go First!");
        displayNotifIcon("/Pics/Icons/NotificationIcons/notif_me_coin.png");
        AppView.playSfxAudio("turn_me");
    }

    public void announceOpponentGoesFirst() {
        displayNotifBar("Opponent Goes First!");
        displayNotifIcon("/Pics/Icons/NotificationIcons/notif_op_coin.png");
        AppView.playSfxAudio("turn_op");
    }

    public void announceRoundBegins() {
        displayNotifBar("Round Begins!");
        displayNotifIcon("/Pics/Icons/NotificationIcons/notif_round_start.png");
        AppView.playSfxAudio("turn_me");
    }

    public void displayNotifIcon(String imageAddress) {
        getCurrentPlayer().setParalyzedByAnimation(true);

        double scaleFactor = Sizes.getUserScreenScale();

        Rectangle notifIcon = new Rectangle(420 * scaleFactor, 300 * scaleFactor);
        notifIcon.setFill(new ImagePattern(new Image(getClass().getResource(
                imageAddress).toExternalForm())));
        notifIcon.setLayoutX(380 * scaleFactor);
        notifIcon.setLayoutY(355 * scaleFactor);

        pane.getChildren().add(notifIcon);

        PauseTransition pauseTransition = new PauseTransition(Duration.millis(notificationDuration - 100));
        pauseTransition.setOnFinished(event -> {
            pane.getChildren().remove(notifIcon);
            getCurrentPlayer().setParalyzedByAnimation(false);
        });
        pauseTransition.play();
    }

    public void displayNotifBar(String message) {
        getCurrentPlayer().setParalyzedByAnimation(true);

        double scaleFactor = Sizes.getUserScreenScale();

        Rectangle blackBar = new Rectangle(1920 * scaleFactor, 150 * scaleFactor, Color.BLACK);
        blackBar.setLayoutX(0);
        blackBar.setLayoutY(430 * scaleFactor);

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-text-fill: #af8c2e; -fx-font-size: 30");
        messageLabel.setLayoutX((1920 / 2 - messageLabel.getWidth() / 2) * scaleFactor);
        messageLabel.setLayoutY(490 * scaleFactor);

        pane.getChildren().addAll(blackBar, messageLabel);

        PauseTransition pauseTransition = new PauseTransition(Duration.millis(notificationDuration));
        pauseTransition.setOnFinished(event -> {
            pane.getChildren().removeAll(blackBar, messageLabel);
            getCurrentPlayer().setParalyzedByAnimation(false);
        });
        pauseTransition.play();
    }

    public void displayEndGameIcon(String imageAddress) {
        double scaleFactor = Sizes.getUserScreenScale();

        Rectangle notifIcon = new Rectangle(656 * scaleFactor, 416 * scaleFactor);
        notifIcon.setFill(new ImagePattern(new Image(getClass().getResource(
                imageAddress).toExternalForm())));
        notifIcon.setLayoutX((1920 / 2 - notifIcon.getWidth() / 2) * scaleFactor);
        notifIcon.setLayoutY(50 * scaleFactor);

        pane.getChildren().add(notifIcon);
    }

    public void showCheatTextField() {
        PlayerState initialPlayerState = getCurrentPlayer().getPlayerState();
        getCurrentPlayer().setPlayerState(PlayerState.PARALYZED);

        textField = new TextField();
        textField.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        textField.setLayoutX(1920 / 2 - 40);
        textField.setLayoutY(1080 /2);
        pane.getChildren().add(textField);

        submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            executeCheat(textField.getText(), initialPlayerState);
        });
        submitButton.setLayoutX(1920 / 2 + 10);
        submitButton.setLayoutY(1080 /2 + 50);
        pane.getChildren().add(submitButton);
    }

    public void removeCheatTextField() {
        textField.clear();
        pane.getChildren().removeAll(textField, submitButton);
    }

    private void executeCheat(String cheat, PlayerState initialState) {
        if (cheat.equals("give homelander")) {
            getCurrentPlayer().getGameController().cheatGiveHomeLander(true);
        }
        else if (cheat.equals("give butcher")) {
            getCurrentPlayer().getGameController().cheatGiveButcher(true);
        }
        else if (cheat.equals("add to hand")) {
            getCurrentPlayer().getGameController().cheatAddToHand();
        }
        else if (cheat.equals("recover crystal")) {
            getCurrentPlayer().getGameController().cheatRecoverCrystal();
        }
        else if (cheat.equals("give thaler")) {
            getCurrentPlayer().getGameController().cheatGiveThaler(true);
        }
        else if (cheat.equals("win round")) {
            getCurrentPlayer().getGameController().cheatWinRound();
        }
        else if (cheat.equals("lose round")) {
            getCurrentPlayer().getGameController().cheatLoseRound();
        }
        else {
            throwAlert("No such cheat code", "Try again something else if you are an admin.");
        }

        getCurrentPlayer().setPlayerState(initialState);
        removeCheatTextField();
    }
    private void adjustInitialChat() {

        pane.getStylesheets().add(getClass().getResource("/StyleSheets/GameChat.css").toExternalForm());
        chatScrollPane = new ScrollPane();
        chatVBox = new VBox();
        chatTextArea = new TextArea();
        chatSendButton = new Button();


        chatScrollPane.setContent(chatVBox);
        chatScrollPane.setPrefSize(300,400);
        chatScrollPane.setLayoutX(1600);
        chatScrollPane.setLayoutY(400);

        chatVBox.getChildren().add(chatTextArea);
        chatTextArea.setPrefSize(20, 100);
        chatTextArea.setFont(new Font(25));

        chatSendButton.setText("send");
        chatVBox.getChildren().add(chatSendButton);

        chatSendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LocalTime currentTime = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String formattedTime = currentTime.format(formatter);
                Text text = new Text("(" + formattedTime + ")" + " " +chatTextArea.getText());
                text.setFont(new Font(20));
                text.setFill(Color.GREEN);
                chatVBox.getChildren().add(text);
                Client.client.sendMessage(chatTextArea.getText());
                chatTextArea.clear();
                chatVBox.getChildren().remove(chatTextArea);
                chatVBox.getChildren().remove(chatSendButton);
                chatVBox.getChildren().add(chatTextArea);
                chatVBox.getChildren().add(chatSendButton);
            }
        });

        AppView.getCurrentScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.T) {
                if (pane.getChildren().contains(chatScrollPane)) {
                    pane.getChildren().remove(chatScrollPane);
                } else {
                    pane.getChildren().add(chatScrollPane);
                }
            } else if (e.getCode() == KeyCode.DIGIT1) {
                updateGUIWhenSendEmoji(1);
                Client.client.sendEmoji(1);
            } else if (e.getCode() == KeyCode.DIGIT2) {
                updateGUIWhenSendEmoji(2);
                Client.client.sendEmoji(2);
            } else if (e.getCode() == KeyCode.DIGIT3) {
                updateGUIWhenSendEmoji(3);
                Client.client.sendEmoji(3);
            } else if (e.getCode() == KeyCode.DIGIT4) {
                updateGUIWhenSendEmoji(4);
                Client.client.sendEmoji(4);
            } else if (e.getCode() == KeyCode.DIGIT5) {
                updateGUIWhenSendReactionMessage(5);
                Client.client.sendReactionMessage(5);
            } else if (e.getCode() == KeyCode.DIGIT6) {
                updateGUIWhenSendReactionMessage(6);
                Client.client.sendReactionMessage(6);
            } else if (e.getCode() == KeyCode.DIGIT7) {
                updateGUIWhenSendReactionMessage(7);
                Client.client.sendReactionMessage(7);
            }
        });
    }

    public void updateGUIWhenGetReactionMessage(int integer) {
        Label label = new Label();
        label.setLayoutX(Emojis.OPPONENT_EMOJI_X.getValue());
        label.setLayoutY(Emojis.OPPONENT_EMOJI_Y.getValue());
        label.setPrefSize(Emojis.LABEL_MESSAGE_WIDTH.getValue(), Emojis.LABEL_MESSAGE_HEIGHT.getValue());
        label.setFont(new Font(Emojis.LABEL_MESSAGE_FONT_SIZE.getValue()));
        label.setTextFill(Color.YELLOW);

        switch (integer) {
            case 5:
                label.setText(Emojis.LABEL_MESSAGE1.getStringValue());
                break;
            case 6:
                label.setText(Emojis.LABEL_MESSAGE2.getStringValue());
                break;
            case 7:
                label.setText(Emojis.LABEL_MESSAGE3.getStringValue());
                break;
        }
        pane.getChildren().add(label);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(Emojis.EMOJI_TIME.getValue()),
                event -> pane.getChildren().remove(label)
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }
    private void updateGUIWhenSendReactionMessage(int whichMessage) {
        Label label = new Label();
        label.setLayoutX(Emojis.MY_EMOJI_X.getValue());
        label.setLayoutY(Emojis.MY_EMOJI_Y.getValue());
        label.setPrefSize(Emojis.LABEL_MESSAGE_WIDTH.getValue(), Emojis.LABEL_MESSAGE_HEIGHT.getValue());
        label.setFont(new Font(Emojis.LABEL_MESSAGE_FONT_SIZE.getValue()));
        label.setTextFill(Color.YELLOW);

        switch (whichMessage) {
            case 5:
                label.setText(Emojis.LABEL_MESSAGE1.getStringValue());
                break;
            case 6:
                label.setText(Emojis.LABEL_MESSAGE2.getStringValue());
                break;
            case 7:
                label.setText(Emojis.LABEL_MESSAGE3.getStringValue());
                break;
        }
        pane.getChildren().add(label);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(Emojis.EMOJI_TIME.getValue()),
                event -> pane.getChildren().remove(label)
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }
    private void updateGUIWhenSendEmoji(int whichEmoji) {
        Rectangle rectangle = new Rectangle();
        rectangle.setX(Emojis.MY_EMOJI_X.getValue());
        rectangle.setY(Emojis.MY_EMOJI_Y.getValue());
        rectangle.setWidth(Emojis.EMOJI_WIDTH.getValue());
        rectangle.setHeight(Emojis.EMOJI_HEIGHT.getValue());

        switch (whichEmoji) {
            case 1:
                rectangle.setFill(new ImagePattern(new Image(Emojis.ADDRESS1.getStringValue())));
                break;
            case 2:
                rectangle.setFill(new ImagePattern(new Image(Emojis.ADDRESS2.getStringValue())));
                break;
            case 3:
                rectangle.setFill(new ImagePattern(new Image(Emojis.ADDRESS3.getStringValue())));
                break;
            case 4:
                rectangle.setFill(new ImagePattern(new Image(Emojis.ADDRESS4.getStringValue())));
                break;
        }
        pane.getChildren().add(rectangle);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(Emojis.EMOJI_TIME.getValue()),
                event -> pane.getChildren().remove(rectangle)
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }
    public void updateGUIWhenGetEmoji(Integer integer) {

        Rectangle rectangle = new Rectangle();
        rectangle.setX(Emojis.OPPONENT_EMOJI_X.getValue());
        rectangle.setY(Emojis.OPPONENT_EMOJI_Y.getValue());
        rectangle.setWidth(Emojis.EMOJI_WIDTH.getValue());
        rectangle.setHeight(Emojis.EMOJI_HEIGHT.getValue());


        switch (integer) {
            case 1:
                rectangle.setFill(new ImagePattern(new Image(Emojis.ADDRESS1.getStringValue())));
                break;
            case 2:
                rectangle.setFill(new ImagePattern(new Image(Emojis.ADDRESS2.getStringValue())));
                break;
            case 3:
                rectangle.setFill(new ImagePattern(new Image(Emojis.ADDRESS3.getStringValue())));
                break;
            case 4:
                rectangle.setFill(new ImagePattern(new Image(Emojis.ADDRESS4.getStringValue())));
                break;
        }
        pane.getChildren().add(rectangle);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(Emojis.EMOJI_TIME.getValue()),
                event -> pane.getChildren().remove(rectangle)
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }
    public void updateGUIWhenGetMessage(String receivedMessage) {
        Text text = new Text(receivedMessage);
        text.setFont(new Font(20));
        text.setFill(Color.WHITE);
        chatVBox.getChildren().add(text);
        chatVBox.getChildren().remove(chatTextArea);
        chatVBox.getChildren().remove(chatSendButton);
        chatVBox.getChildren().add(chatTextArea);
        chatVBox.getChildren().add(chatSendButton);
    }
    public void setUserOfPlayer(User userOfPlayer) {
        this.userOfPlayer = userOfPlayer;
    }

    public void setUserOfOpponent(User userOfOpponent) {
        this.userOfOpponent = userOfOpponent;
    }

    public void setAmIFirst(boolean amIFirst) {
        this.amIFirst = amIFirst;
    }

    public static GameViewController getCurrentGameViewController() {
        return currentGameViewController;
    }

    public boolean isAmIFirst() {
        return amIFirst;
    }
}
