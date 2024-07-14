package Model.InGameObjects.Board;

import Model.InGameObjects.Cards.Card;
import Model.Game.Player;
import javafx.scene.effect.DropShadow;

import java.util.ArrayList;
import Enum.Sizes;
import javafx.scene.paint.Color;

public class ScrollBox extends BoardSection{

    private static final double prefWidthOfScrollShowBoxArea = Sizes.SCROLL_SHOW_BOX.getWidth();
    private static final double prefHeightOfScrollShowBoxArea = Sizes.SCROLL_SHOW_BOX.getHeight();
    private static final double layoutXOnBoardOfScrollShowBoxArea = 0;
    private static final double layoutYOnBoardOfScrollShowBoxArea = 150;
    private int cardsToSelect;
    private int cardsSelected;
    private boolean optionalSelect;
    private boolean isRemoved;
    private Card leftCard2;
    private Card leftCard1;
    private Card centerCard;
    private Card rightCard2;
    private Card rightCard1;
    ArrayList<Card> selectedCards = new ArrayList<>();

    public ScrollBox(Player owner, ArrayList<Card> cards,
                     int cardsToSelect, boolean optionalSelect) {
        super(prefWidthOfScrollShowBoxArea, prefHeightOfScrollShowBoxArea, layoutXOnBoardOfScrollShowBoxArea,
                    layoutXOnBoardOfScrollShowBoxArea, owner, owner);

        setCardsOnSection(cards);
        this.cardsToSelect = cardsToSelect;
        this.cardsSelected = 0;
        this.optionalSelect = optionalSelect;
        this.isRemoved = false;

        if (!cards.isEmpty() &&
                cards.get(0) != null) {
            centerCard = cards.get(0);
        }

        if (cards.size() > 1 &&
                cards.get(1) != null) {
            rightCard1 = cards.get(1);
        }

        if (cards.size() > 2 &&
                cards.get(2) != null) {
            rightCard2 = cards.get(2);
        }
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    public int getCardsToSelect() {
        return cardsToSelect;
    }

    public void setCardsToSelect(int cardsToSelect) {
        this.cardsToSelect = cardsToSelect;
    }

    public int getCardsSelected() {
        return cardsSelected;
    }

    public void setCardsSelected(int cardsSelected) {
        this.cardsSelected = cardsSelected;
    }

    public boolean isOptionalSelect() {
        return optionalSelect;
    }

    public void setOptionalSelect(boolean optionalSelect) {
        this.optionalSelect = optionalSelect;
    }

    public void addToSelectedCards(Card card) {
        selectedCards.add(card);
    }

    public ArrayList<Card> getSelectedCards() {
        return selectedCards;
    }

    public void setSelectedCards(ArrayList<Card> selectedCards) {
        this.selectedCards = selectedCards;
    }

    public Card getLeftCard1() {
        return leftCard1;
    }

    public void setLeftCard1(Card leftCard1) {
        this.leftCard1 = leftCard1;
    }

    public Card getLeftCard2() {
        return leftCard2;
    }

    public void setLeftCard2(Card leftcard) {
        this.leftCard2 = leftcard;
    }

    public Card getCenterCard() {
        return centerCard;
    }

    public void setCenterCard(Card centerCard) {
        this.centerCard = centerCard;
    }

    public Card getRightCard1() {
        return rightCard1;
    }

    public void setRightCard1(Card rightCard1) {
        this.rightCard1 = rightCard1;
    }

    public Card getRightCard2() {
        return rightCard2;
    }

    public void setRightCard2(Card rightCard2) {
        this.rightCard2 = rightCard2;
    }

    public void shiftCardsRight() {
        if (getCardsOnSection().indexOf(centerCard) == 0) {
            return;
        }
        resetCardsEffect();
        int centerCardIndex = getCardsOnSection().indexOf(centerCard);
        if (centerCardIndex > 1) {
            setLeftCard2(getCardsOnSection().get(centerCardIndex - 2));
        }
        else {
            setLeftCard2(null);
        }
        if (centerCardIndex > 2) {
            setLeftCard1(getCardsOnSection().get(centerCardIndex - 3));
        }
        else {
            setLeftCard1(null);
        }
        if (centerCardIndex + 1 < getCardsOnSection().size()) {
            setRightCard2(getCardsOnSection().get(centerCardIndex + 1));
        }
        else {
            setRightCard2(null);
        }
        setCenterCard(getCardsOnSection().get(centerCardIndex - 1));
        setRightCard1(getCardsOnSection().get(centerCardIndex));
        setCardsEffect();
    }

    public void shiftCardsLeft() {
        if (getCardsOnSection().indexOf(centerCard) == getCardsOnSection().size() - 1) {
            return;
        }
        resetCardsEffect();
        int centerCardIndex = getCardsOnSection().indexOf(centerCard);
        if (centerCardIndex < getCardsOnSection().size() - 2) {
            setRightCard1(getCardsOnSection().get(centerCardIndex + 2));
        }
        else {
            setRightCard1(null);
        }
        if (centerCardIndex < getCardsOnSection().size() - 3) {
            setRightCard2(getCardsOnSection().get((centerCardIndex + 3)));
        }
        else {
            setRightCard2(null);
        }
        if (centerCardIndex > 0) {
            setLeftCard1(getCardsOnSection().get(centerCardIndex - 1));
        }
        else {
            setLeftCard1(null);
        }
        setCenterCard(getCardsOnSection().get(centerCardIndex + 1));
        setLeftCard2(getCardsOnSection().get(centerCardIndex));
        setCardsEffect();
    }

    public void setCardsEffect() {
        if (centerCard != null) {
            centerCard.getDescriptionCard().setScaleX(1.4);
            centerCard.getDescriptionCard().setScaleY(1.4);
            DropShadow glow = new DropShadow();
            glow.setColor(Color.GOLD);
            glow.setRadius(20);
            glow.setSpread(0.6);
            centerCard.getDescriptionCard().setEffect(glow);
        }

        if (leftCard2 != null) {
            leftCard2.getDescriptionCard().setScaleX(1.2);
            leftCard2.getDescriptionCard().setScaleY(1.2);
        }

        if (rightCard1 != null) {
            rightCard1.getDescriptionCard().setScaleX(1.2);
            rightCard1.getDescriptionCard().setScaleY(1.2);
        }
    }

    private void resetCardsEffect() {
        if (centerCard != null) {
            centerCard.getDescriptionCard().setScaleX(1);
            centerCard.getDescriptionCard().setScaleY(1);
            centerCard.getDescriptionCard().setEffect(null);
        }

        if (leftCard2 != null) {
            leftCard2.getDescriptionCard().setScaleX(1);
            leftCard2.getDescriptionCard().setScaleY(1);
        }

        if (rightCard1 != null) {
            rightCard1.getDescriptionCard().setScaleX(1);
            rightCard1.getDescriptionCard().setScaleY(1);
        }
    }
}
