package Model.InGameObjects.Board;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.RowCard;
import Model.InGameObjects.Cards.UnitCard;
import Model.Game.Player;
import Enum.*;

public class DiscardPileArea extends BoardSection{
    private static final double prefWidthOfDiscardPileArea = Sizes.DISCARD_PILE_AREA.getWidth();
    private static final double prefHeightOfDiscardPileArea = Sizes.DISCARD_PILE_AREA.getHeight();
    private static final double layoutXOnBoardOfDiscardPileArea = 1541;
    private static final double layoutYOnBoardOfDiscardPileArea = 68;
    private static final double layoutYDiffer = 758;

    public DiscardPileArea(Boolean isFriendly, Player player, Player owner) {
        super(prefWidthOfDiscardPileArea, prefHeightOfDiscardPileArea, layoutXOnBoardOfDiscardPileArea,
                    layoutYOnBoardOfDiscardPileArea, player, owner);
        if (isFriendly) {
            super.setLayoutYOnBoard(layoutYOnBoardOfDiscardPileArea + layoutYDiffer);
            super.setCardPlace(CardPlace.FRIENDLY_DISCARD_PILE_AREA);
        }
        else {
            super.setCardPlace(CardPlace.OPPONENT_DISCARD_PILE_AREA);
        }

        setBackgroundImage("/Pics/BoardObjects/DiscardPileArea.png");
    }

    @Override
    public void updateSection() {
        if (getCardsOnSection().isEmpty())
            return;

        RowCard rowCard = getCardsOnSection().get(getCardsOnSection().size() - 1).getRowCard();

        if (this.getChildren().contains(rowCard))
            return;

        rowCard.setLayoutX(17);
        rowCard.setLayoutY(24);
        this.getChildren().add(rowCard);
    }

    @Override
    public void addToCardsOnSection(Card card) {
        getCardsOnSection().add(card);

        if (card instanceof UnitCard)
            ((UnitCard) card).setCurrentPower(((UnitCard) card).getBasePower());
    }

}
