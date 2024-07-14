package Model.InGameObjects.Board;

import Model.InGameObjects.Cards.Card;
import Model.Game.Player;
import Enum.*;

public class HandArea extends BoardSection {
    private static final double prefWidthOfHandArea = Sizes.HAND_AREA.getWidth();
    private static final double prefHeightOfHandArea = Sizes.HAND_AREA.getHeight();
    private static final double layoutXOnBoardOfHandArea = 572;
    private static final double layoutYOnBoardOfHandArea = 836;

    public HandArea(Player owner) {
        super(prefWidthOfHandArea, prefHeightOfHandArea, layoutXOnBoardOfHandArea,
                    layoutYOnBoardOfHandArea, owner, owner);

        setCardPlace(CardPlace.HAND_AREA);
        //setBackgroundImage("/Pics/BoardObjects/HandArea.png");
    }

    @Override
    public void addToCardsOnSection(Card card) {
        getCardsOnSection().add(card);
        getPlayer().getBoard().getPlayerInformationBar().updateInfoBar();
    }
}
