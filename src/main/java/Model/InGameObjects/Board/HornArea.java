package Model.InGameObjects.Board;
import Enum.*;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Cards.RowCard;
import Model.InGameObjects.Abilities.SpellAbilities.SpellCommandersHornAbility;
import Model.InGameObjects.Abilities.SpellAbilities.SpellMardroemeAbility;

public class HornArea extends BoardSection{

    private static final double prefWidthOfHornArea = Sizes.HORN_AREA.getWidth();
    private static final double prefHeightOfHornArea = Sizes.HORN_AREA.getHeight();
    private Row hornRow;
    private boolean isEmpty;
    private boolean isHorned;
    private boolean isMardroemed;
    private Card spellCard;

    public HornArea(Row hornRow) {
        super(prefWidthOfHornArea, prefHeightOfHornArea, hornRow.getLayoutXOnBoard() - prefWidthOfHornArea,
                    hornRow.getLayoutYOnBoard(), hornRow.getPlayer(), hornRow.getOwner());

        setCardPlace(CardPlace.HORN_AREA);
        this.hornRow = hornRow;
        this.isEmpty = true;
        isHorned = false;
        isMardroemed = false;

        //setBackgroundImage("/Pics/BoardObjects/HornArea.png");
    }

    @Override
    public void cloneBoardSection(BoardSection boardSection) {
        HornArea hornArea = (HornArea) boardSection;

        hornArea.setHorned(isHorned);

        hornArea.setEmpty(isEmpty);

        hornArea.setMardroemed(isMardroemed);

        hornArea.setSpellCard(spellCard);
    }

    @Override
    public void resetBoardSection() {
        if (spellCard != null)
            getChildren().remove(spellCard.getRowCard());

        getCardsOnSection().removeAll(getCardsOnSection());
        spellCard = null;
        isEmpty = true;
    }

    public Row getHornRow() {
        return hornRow;
    }

    private void checkSpellPresence() {
        for (Card card : getCardsOnSection()) {
            isEmpty = false;
            spellCard = card;
            for (CardAbility cardAbility : card.getCardAbilities()) {
                if (cardAbility instanceof SpellCommandersHornAbility) {
                    isHorned = true;
                    break;
                }

                else if (cardAbility instanceof SpellMardroemeAbility) {
                    isMardroemed = true;
                    break;
                }
            }
        }
    }

    public boolean isEmpty() {
        checkSpellPresence();
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public Card getSpellCard() {
        return spellCard;
    }

    public void setSpellCard(Card spellCard) {
        this.spellCard = spellCard;
    }

    public boolean isHorned() {
        return isHorned;
    }

    public void setHorned(boolean horned) {
        isHorned = horned;
    }

    public boolean isMardroemed() {
        return isMardroemed;
    }

    public void setMardroemed(boolean mardroemed) {
        isMardroemed = mardroemed;
    }

    @Override
    public void updateSection() {
        checkSpellPresence();

        if (!isEmpty) {
            RowCard rowCard = spellCard.getRowCard();

            rowCard.setLayoutX(25);
            rowCard.setLayoutY(10);
            if (!this.getChildren().contains(rowCard))
                this.getChildren().add(rowCard);
        }
    }
}
