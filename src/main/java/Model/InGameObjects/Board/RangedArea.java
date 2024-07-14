package Model.InGameObjects.Board;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Abilities.SpellAbilities.SpellImpenetrableFogAbility;
import Model.InGameObjects.Abilities.SpellAbilities.SpellSkelligeStormAbility;
import Model.Game.Player;
import Enum.*;

public class RangedArea extends Row{

    private static final double prefWidthOfRangedArea = Sizes.RANGED_AREA.getWidth();
    private static final double prefHeightOfRangedArea = Sizes.RANGED_AREA.getHeight();
    private static final double layoutXOnBoardOfCloseArea = 704;
    private static final double layoutYOnBoardOfCloseArea = 148;
    private static final double layoutYDiffer = 422.5;

    public RangedArea(Boolean isFriendly, Player player, Player owner) {
        super(prefWidthOfRangedArea, prefHeightOfRangedArea, layoutXOnBoardOfCloseArea,
                    layoutYOnBoardOfCloseArea, player, owner);
        if (isFriendly) {
            super.setLayoutYOnBoard(layoutYOnBoardOfCloseArea + layoutYDiffer);
            super.setCardPlace(CardPlace.FRIENDLY_RANGED);
        }
        else {
            super.setCardPlace(CardPlace.OPPONENT_RANGED);
        }
        setHornAndRowScore();

        //setBackgroundImage("/Pics/BoardObjects/RangedArea.png");
    }

    @Override
    void checkForWeather() {
        setWeatherBad(false);
        outer:
        for (Card card : getPlayer().getBoard().getWeatherCardArea().getCardsOnSection()) {
            for (CardAbility cardAbility : card.getCardAbilities()) {
                if (cardAbility instanceof SpellImpenetrableFogAbility || cardAbility instanceof SpellSkelligeStormAbility) {
                    setWeatherBad(true);
                    break outer;
                }
            }
        }
    }
}
