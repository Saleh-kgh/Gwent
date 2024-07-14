package Model.InGameObjects.Board;
import Enum.CardPlace;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Abilities.SpellAbilities.SpellBitingFrostAbility;
import Model.Game.Player;
import Enum.*;

public class CloseArea extends Row{

    private static final double prefWidthOfCloseArea = Sizes.CLOSE_AREA.getWidth();
    private static final double prefHeightOfCloseArea = Sizes.CLOSE_AREA.getHeight();
    private static final double layoutXOnBoardOfCloseArea = 705;
    private static final double layoutYOnBoardOfCloseArea = 288.5;
    private static final double layoutYDiffer = 147;

    public CloseArea(Boolean isFriendly, Player player, Player owner) {
        super(prefWidthOfCloseArea, prefHeightOfCloseArea, layoutXOnBoardOfCloseArea,
                    layoutYOnBoardOfCloseArea, player, owner);
        if (isFriendly) {
            super.setLayoutYOnBoard(layoutYOnBoardOfCloseArea + layoutYDiffer);
            super.setCardPlace(CardPlace.FRIENDLY_CLOSE);
        }
        else {
            super.setCardPlace(CardPlace.OPPONENT_CLOSE);
        }
        setHornAndRowScore();

        //setBackgroundImage("/Pics/BoardObjects/CloseArea.png");
    }

    @Override
    void checkForWeather() {
        setWeatherBad(false);
        outer:
        for (Card card : getPlayer().getBoard().getWeatherCardArea().getCardsOnSection()) {
            for (CardAbility cardAbility : card.getCardAbilities()) {
                if (cardAbility instanceof SpellBitingFrostAbility) {
                    setWeatherBad(true);
                    break outer;
                }
            }
        }
    }
}
