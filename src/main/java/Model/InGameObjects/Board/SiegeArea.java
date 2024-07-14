package Model.InGameObjects.Board;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Abilities.SpellAbilities.SpellSkelligeStormAbility;
import Model.InGameObjects.Abilities.SpellAbilities.SpellTorrentialRainAbility;
import Model.Game.Player;
import Enum.*;

public class SiegeArea extends Row{

    private static final double prefWidthOfSiegeArea = Sizes.SIEGE_AREA.getWidth();
    private static final double prefHeightOfSiegeArea = Sizes.SIEGE_AREA.getHeight();
    private static final double layoutXOnBoardOfCloseArea = 705;
    private static final double layoutYOnBoardOfCloseArea = 18;
    private static final double layoutYDiffer = 690;

    public SiegeArea(Boolean isFriendly, Player player, Player owner) {
        super(prefWidthOfSiegeArea, prefHeightOfSiegeArea, layoutXOnBoardOfCloseArea,
                layoutYOnBoardOfCloseArea, player, owner);
        if (isFriendly) {
            super.setLayoutYOnBoard(layoutYOnBoardOfCloseArea + layoutYDiffer);
            super.setCardPlace(CardPlace.FRIENDLY_SIEGE);
        }
        else {
            super.setCardPlace(CardPlace.OPPONENT_SIEGE);
        }
        setHornAndRowScore();

        //setBackgroundImage("/Pics/BoardObjects/SiegeArea.png");
    }

    @Override
    void checkForWeather() {
        setWeatherBad(false);
        outer:
        for (Card card : getPlayer().getBoard().getWeatherCardArea().getCardsOnSection()) {
            for (CardAbility cardAbility : card.getCardAbilities()) {
                if (cardAbility instanceof SpellTorrentialRainAbility || cardAbility instanceof SpellSkelligeStormAbility) {
                    setWeatherBad(true);
                    break outer;
                }
            }
        }
    }
}
