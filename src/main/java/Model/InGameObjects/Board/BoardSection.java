package Model.InGameObjects.Board;

import Model.InGameObjects.Cards.*;
import view.Controller.ClickEvaluator.BoardSectionClickEvaluator;
import Model.Game.Player;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Enum.*;

public class BoardSection extends Pane {

    private final double prefWidth;
    private final double prefHeight;
    private double layoutXOnBoard;
    private double layoutYOnBoard;
    private CardPlace cardPlace;
    private final Player player;
    private final Player owner;
    private ArrayList<Card> cardsOnSection = new ArrayList<>();

    public BoardSection(double prefWidth, double prefHeight, double layoutXOnBoard,
                        double layoutYOnBoard, Player player, Player owner) {

        this.prefWidth = prefWidth;
        this.prefHeight = prefHeight;
        setPrefSize(prefWidth, prefHeight);
        this.layoutXOnBoard = layoutXOnBoard;
        this.layoutYOnBoard = layoutYOnBoard;
        this.player = player;
        this.owner = owner;

        setOnMouseClicked(event -> {
            new BoardSectionClickEvaluator(player).manageClickCommand(this);
        });
    }

    public void cloneBoardSection(BoardSection boardSection) {
        boardSection.setCardsOnSection(getCardsOnSection());
    }

    public void resetBoardSection() {
        getCardsOnSection().removeAll(getCardsOnSection());
        updateSection();
    }

    public void updateSection() {
        sortCardsOnSection();

        int cardsCount = cardsOnSection.size();
        double distanceFromSectionEdges = 30;
        double cardsTotalWidth = cardsCount * Sizes.ROW_CARD.getWidth();

        double layoutXStart = calculateLayoutXStart(distanceFromSectionEdges, cardsTotalWidth, cardsCount);
        double cardsSpacing = calculateCardsSpacing(distanceFromSectionEdges, cardsTotalWidth, cardsCount);

        ArrayList<RowCard> rowCards = new ArrayList<>();

            for (Card card : cardsOnSection) {
                rowCards.add(card.getRowCard());
            }

            int index = 0;
            for (RowCard rowCard : rowCards) {
                if (!rowCard.getCard().isTransitioning()) {
                    if (this.getChildren().contains((rowCard))) {
                        this.getChildren().remove(rowCard);
                    }

                    rowCard.setLayoutX(layoutXStart + (Sizes.ROW_CARD.getWidth() * index) - (cardsSpacing * index++));

                    rowCard.setLayoutY(10);

                    if (this instanceof WeatherCardArea) {
                        rowCard.setLayoutY(15);
                        rowCard.setLayoutX(rowCard.getLayoutX() + 10);
                    }

                    this.getChildren().add(rowCard);
                    rowCard.toFront();
                }
            }

            if (this instanceof Row) {
                ((Row) this).getRowScoreArea().updateSection();
                ((Row) this).weatherEffectToFront();
            }
    }

    public double calculateLayoutXStart(double distanceFromSectionEdges, double cardsTotalWidth, int cardsCount) {
        double layoutXStart = (getPrefWidth() - distanceFromSectionEdges - cardsTotalWidth) / 2;

        if (cardsTotalWidth > (getPrefWidth() - distanceFromSectionEdges)) {
            layoutXStart = distanceFromSectionEdges / 2;
        }

        return layoutXStart;
    }

    public double calculateCardsSpacing(double distanceFromSectionEdges, double cardsTotalWidth, int cardsCount) {
        double cardsSpacing = -1;

        if (cardsTotalWidth > (getPrefWidth() - distanceFromSectionEdges)) {
            cardsSpacing = (cardsTotalWidth - (getPrefWidth() - distanceFromSectionEdges)) / (cardsCount - 1);
        }

        return cardsSpacing;
    }

    public void sortCardsOnSection() {
        List<Card> cards = getCardsOnSection();
        Collections.sort(cards, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                if (c1 instanceof LeaderCard && !(c2 instanceof LeaderCard)) {
                    return -1;
                } else if (!(c1 instanceof LeaderCard) && c2 instanceof LeaderCard) {
                    return 1;
                }

                if (c1 instanceof SpellCard && !(c2 instanceof SpellCard)) {
                    return -1;
                } else if (!(c1 instanceof SpellCard) && c2 instanceof SpellCard) {
                    return 1;
                }

                if (c1 instanceof UnitCard && c2 instanceof UnitCard) {
                    return Integer.compare(((UnitCard) c1).getBasePower(), ((UnitCard) c2).getBasePower());
                }

                return 0;
            }
        });
    }

    public void setBackgroundImage(String imagePath) {
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
        setBackground(new Background(backgroundImage));
    }

    public Player getPlayer() {
        return player;
    }

    public Player getOwner() {
        return owner;
    }

    public CardPlace getCardPlace() {
        return cardPlace;
    }

    public void setCardPlace(CardPlace cardPlace) {
        this.cardPlace = cardPlace;
    }

    public double getLayoutXOnBoard() {
        return layoutXOnBoard;
    }

    public double getLayoutYOnBoard() {
        return layoutYOnBoard;
    }

    public void setLayoutXOnBoard(double layoutXOnBoard) {
        this.layoutXOnBoard = layoutXOnBoard;
    }

    public void setLayoutYOnBoard(double layoutYOnBoard) {
        this.layoutYOnBoard = layoutYOnBoard;
    }

    public ArrayList<Card> getCardsOnSection() {
        return cardsOnSection;
    }

    public void setCardsOnSection(ArrayList<Card> cardsOnSection) {
        this.cardsOnSection = cardsOnSection;
    }

    public void addToCardsOnSection(Card card) {
        this.cardsOnSection.add(card);
    }

    public void removeFromCardsOnSection(Card card) {
        this.cardsOnSection.remove(card);

        if (this.getChildren().contains(card.getRowCard()))
            this.getChildren().remove(card.getRowCard());
    }

    public double getCardLayoutXAfterTransition(Card addedCard) {
        if (!getCardsOnSection().contains(addedCard))
            getCardsOnSection().add(addedCard);

        sortCardsOnSection();

        double layoutX = 0;

        int cardsCount = cardsOnSection.size();
        double distanceFromSectionEdges = 30;
        double cardsTotalWidth = cardsCount * Sizes.ROW_CARD.getWidth();

        double layoutXStart = calculateLayoutXStart(distanceFromSectionEdges, cardsTotalWidth, cardsCount);
        double cardsSpacing = calculateCardsSpacing(distanceFromSectionEdges, cardsTotalWidth, cardsCount);

        ArrayList<RowCard> rowCards = new ArrayList<>();

        for (Card card : cardsOnSection) {
            rowCards.add(card.getRowCard());
        }

        int index = 0;
        for (RowCard rowCard : rowCards) {
            double rowCardLayoutX = layoutXStart + (Sizes.ROW_CARD.getWidth() * index) - (cardsSpacing * index++);

            if (rowCard.getCard().equals(addedCard))
                layoutX =  rowCardLayoutX + getLayoutX();
        }

        if (getCardsOnSection().contains(addedCard))
            getCardsOnSection().remove(addedCard);

        sortCardsOnSection();

        return layoutX;
    }

    public double getCardLayoutYAfterTransition(Card card) {
        return 10 + getLayoutY();
    }

    public double translateRowCardAbsoluteLayoutX(Card card) {
        return card.getRowCard().getLayoutX() + getLayoutX();
    }

    public double translateRowCardAbsoluteLayoutY(Card card) {
        return card.getRowCard().getLayoutY() + getLayoutY();
    }

    public double translateRowCardComparativeLayoutX(Card card) {
        return card.getCurrentPlaceInGame().translateRowCardAbsoluteLayoutX(card) - getLayoutX();
    }

    public double translateRowCardComparativeLayoutY(Card card) {
        return card.getCurrentPlaceInGame().translateRowCardAbsoluteLayoutY(card) - getLayoutY();
    }
}
