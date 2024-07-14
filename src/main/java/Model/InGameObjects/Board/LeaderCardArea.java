package Model.InGameObjects.Board;

import Model.InGameObjects.Cards.RowCard;
import Model.Game.Player;
import Enum.*;

public class LeaderCardArea extends BoardSection{
    private static final double prefWidthOfLeaderCardArea = Sizes.LEADER_CARD_AREA.getWidth();
    private static final double prefHeightOfLeaderCardArea = Sizes.LEADER_CARD_AREA.getHeight();
    private static final double layoutXOnBoardOfLeaderCardArea = 132;
    private static final double layoutYOnBoardOfLeaderCardArea = 74;
    private static final double layoutYDiffer = 753;

    public LeaderCardArea(Boolean isFriendly, Player player, Player owner) {
        super(prefWidthOfLeaderCardArea, prefHeightOfLeaderCardArea, layoutXOnBoardOfLeaderCardArea,
                    layoutYOnBoardOfLeaderCardArea, player, owner);
        if (isFriendly) {
            super.setLayoutYOnBoard(layoutYOnBoardOfLeaderCardArea + layoutYDiffer);
            super.setCardPlace(CardPlace.FRIENDLY_LEADER);
        }
        else {
            super.setCardPlace(CardPlace.OPPONENT_LEADER);
        }

        setBackgroundImage("/Pics/BoardObjects/LeaderCardArea.png");
    }

    @Override
    public void updateSection() {
        if (getCardsOnSection().isEmpty())
            return;

        RowCard rowCard = getCardsOnSection().get(0).getRowCard();

        if (this.getChildren().contains(rowCard))
            return;

        rowCard.setLayoutX(26);
        rowCard.setLayoutY(22);
        this.getChildren().add(rowCard);

        if (!getPlayer().getGameController().isLeaderPlayable(rowCard.getCard()))
            getPlayer().getGameObject().getGameViewController().giveDarkEffectToCard(rowCard.getCard());
    }
}
