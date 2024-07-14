package Model.InGameObjects.Board;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardAbility;
import Model.InGameObjects.Abilities.UnitAbilities.UnitCommandersHornAbility;
import Model.InGameObjects.Abilities.UnitAbilities.UnitMoraleBoostAbility;
import Model.InGameObjects.Abilities.UnitAbilities.UnitSpyAbility;
import Model.InGameObjects.Abilities.UnitAbilities.UnitTightBondAbility;
import Model.InGameObjects.Cards.UnitCard;
import Model.Game.Player;

import java.util.ArrayList;
import Enum.CardStates;
import Enum.CardNames;
import javafx.scene.shape.Rectangle;

public abstract class Row extends BoardSection{
    private RowScoreArea rowScoreArea;
    private HornArea hornArea;
    private boolean isHornAreaFull;
    private boolean isHorned;
    private boolean hasMardroeme;
    private boolean isWeatherBad;
    private int rowMoralBoost;
    private Rectangle weatherEffect;
    private ArrayList<UnitCard> unitCardsToPutNextRound = new ArrayList<>();

    public Row(double prefWidth, double prefHeight, double layoutXOnBoard,
               double layoutYOnBoard, Player player, Player owner) {

        super(prefWidth, prefHeight, layoutXOnBoard, layoutYOnBoard, player, owner);
    }

    @Override
    public void cloneBoardSection(BoardSection boardSection) {
        Row row = (Row) boardSection;

        row.getUnitCardsToPutNextRound().removeAll(row.getUnitCardsToPutNextRound());
        row.getUnitCardsToPutNextRound().addAll(unitCardsToPutNextRound);

        row.setHornAreaFull(isHornAreaFull);

        row.setHorned(isHorned);

        row.setHasMardroeme(hasMardroeme);

        row.setWeatherBad(isWeatherBad);

        row.setRowMoralBoost(rowMoralBoost);

        row.setCardsOnSection(getCardsOnSection());

        getHornArea().cloneBoardSection(row.getHornArea());
    }

    @Override
    public void resetBoardSection() {
        getCardsOnSection().removeAll(getCardsOnSection());
        hornArea.resetBoardSection();

        if (weatherEffect != null) {
            getChildren().remove(weatherEffect);
            weatherEffect = null;
        }
    }

    public void setHornAndRowScore() {
        this.hornArea = new HornArea(this);
        this.rowScoreArea = new RowScoreArea(this);
    }

    public RowScoreArea getRowScoreArea() {
        return rowScoreArea;
    }

    public boolean isHornAreaFull() {
        return isHornAreaFull;
    }

    public void setHornAreaFull(boolean hornAreaFull) {
        isHornAreaFull = hornAreaFull;
    }

    public HornArea getHornArea() {
        return hornArea;
    }

    public boolean isHorned() {
        return isHorned;
    }

    public Rectangle getWeatherEffect() {
        return weatherEffect;
    }

    public void setWeatherEffect(Rectangle weatherEffect) {
        this.weatherEffect = weatherEffect;
    }

    public void weatherEffectToFront() {
        if (weatherEffect != null)
            weatherEffect.toFront();
    }

    public void setHorned(boolean horned) {
        isHorned = horned;
    }

    public boolean doesHaveMardroeme() {
        return hasMardroeme;
    }

    public void setHasMardroeme(boolean hasMardroeme) {
        this.hasMardroeme = hasMardroeme;
    }

    public boolean isWeatherBad() {
        return isWeatherBad;
    }

    public void setWeatherBad(boolean weatherBad) {
        isWeatherBad = weatherBad;
    }

    public int getRowMoralBoost() {
        return rowMoralBoost;
    }

    public void setRowMoralBoost(int rowMoralBoost) {
        this.rowMoralBoost = rowMoralBoost;
    }

    public ArrayList<UnitCard> getUnitCardsToPutNextRound() {
        return unitCardsToPutNextRound;
    }

    public void addToUnitCardsToPutNextRound(UnitCard unitCard) {
        unitCardsToPutNextRound.add(unitCard);
    }

    public void removeToUnitCardsToPutNextRound(UnitCard unitCard) {
        unitCardsToPutNextRound.remove(unitCard);
    }

    public int calculateRowTotalPower() {
        int totalPower = 0;
        for (Card card : getCardsOnSection()) {
            if (card instanceof UnitCard) {
                totalPower += calculateCardPowerInRow((UnitCard) card);
            }
        }

        return totalPower;
    }

    public int calculateCardPowerInRow(UnitCard unitCard) {
        int cardPower = unitCard.getBasePower();
        if (!unitCard.isHero()) {
            checkForWeather();
            int conditionalPower = unitCard.getBasePower() == 0 ? 0 : 1;
            conditionalPower = isCardLeaderKingBran(unitCard) ? cardPower / 2 : conditionalPower;
            cardPower = isWeatherBad ? conditionalPower : cardPower;

            checkForTightBond(unitCard);

            cardPower *= unitCard.getMultipliedPower();

            boolean isCommanderHornSpell = isRowCommanderHorned();
            boolean isCommanderHornUnit = isCommanderHornUnitOnRow();
            boolean isCardCommanderHornUnit = false;

            for (CardAbility cardAbility : unitCard.getCardAbilities()) {
                if (cardAbility instanceof UnitCommandersHornAbility) {
                    isCardCommanderHornUnit = true;
                    break;
                }
            }

            if (isCardCommanderHornUnit) {
                if (isCommanderHornSpell) {
                    cardPower *= 2;
                }
            }
            else {
                if (isCommanderHornUnit || isCommanderHornSpell) {
                    cardPower *= 2;
                }
                else if (isCardSpy(unitCard)) {
                    if (isEredinTreacherousPresent())
                        cardPower *= 2;
                }
            }

            setMoraleBoost();
            cardPower += rowMoralBoost;
            if (isCardMoraleBooster(unitCard)) cardPower--;
        }
        unitCard.setCurrentPower(cardPower);
        unitCard.getRowCard().setPowerLabelText();
        return cardPower;
    }

    private boolean isCardMoraleBooster(Card card) {
        for (CardAbility cardAbility : card.getCardAbilities()) {
            if (cardAbility instanceof UnitMoraleBoostAbility)
                return true;
        }
        return false;
    }

    private boolean isCardSpy(Card card) {
        for (CardAbility cardAbility : card.getCardAbilities()) {
            if (cardAbility instanceof UnitSpyAbility)
                return true;
        }
        return false;
    }

    private boolean isCardLeaderKingBran(Card card) {
        return card.getOwner().getLeaderCard().getCardName().equals(CardNames.KING_BRAN);
    }

    private boolean isEredinTreacherousPresent() {
        if (getPlayer().getLeaderCard().getCardName().equals(CardNames.EREDIN_TREACHEROUS))
            return true;

        if (getPlayer().getOpponent().getLeaderCard().getCardName().equals(CardNames.EREDIN_TREACHEROUS))
            return true;

        return false;
    }

    private void checkForTightBond(Card card) {
        for (CardAbility cardAbility : card.getCardAbilities()) {
            if (cardAbility instanceof UnitTightBondAbility) {
                ((UnitTightBondAbility) cardAbility).setMultipliedPowerForRowBond();
                return;
            }
        }
    }

    public boolean isRowCommanderHorned() {
        boolean isHorned = false;
        getHornArea().updateSection();

        if (!getHornArea().isEmpty()) {
            isHorned = getHornArea().isHorned();
        }

        return isHorned;
    }

    public boolean isCommanderHornUnitOnRow() {
        boolean isHorned = false;

        for (Card card : getCardsOnSection()) {
            if (card instanceof UnitCard) {
                for (CardAbility cardAbility : card.getCardAbilities()) {
                    if (cardAbility instanceof UnitCommandersHornAbility) {
                        isHorned = true;
                    }
                }
            }
        }

        return isHorned;
    }

    public void setMoraleBoost() {
        int boost = 0;
        for (Card card : getCardsOnSection()) {
            if (card instanceof UnitCard) {
                for (CardAbility cardAbility : card.getCardAbilities()) {
                    if (cardAbility instanceof UnitMoraleBoostAbility) {
                        boost++;
                    }
                }
            }
        }

        rowMoralBoost = boost;
    }

    public int getMoraleBoost() {
        return rowMoralBoost;
    }

    public int getHighestCardPowerInRow() {
        int highestCardPower = -1;

        for (Card card : getCardsOnSection()) {
            if (card instanceof UnitCard) {
                if (!((UnitCard) card).isHero())
                    highestCardPower = Math.max(highestCardPower , calculateCardPowerInRow((UnitCard) card));
            }
        }

        return highestCardPower;
    }

    public Row getOpponentSameKindRow() {
        switch (this.getCardPlace()) {
            case FRIENDLY_CLOSE:
                return this.getPlayer().getBoard().getOpponentCloseArea();
            case FRIENDLY_RANGED:
                return this.getPlayer().getBoard().getOpponentRangedArea();
            case FRIENDLY_SIEGE:
                return this.getPlayer().getBoard().getOpponentSiegeArea();
            default:
                return null;
        }
    }

    public void placeUnitCardsToPutNextRound() {
        getCardsOnSection().addAll(unitCardsToPutNextRound);
        for (Card card : unitCardsToPutNextRound) {
            card.setPreviousState(CardStates.ROW_HIDDEN);
            card.setCurrentState(CardStates.ROW_IN_GAME_PLAYED);
            card.setCurrentPlaceInGame(this);
        }
        updateSection();

        ArrayList<UnitCard> newArray = new ArrayList<>();
        unitCardsToPutNextRound = newArray;
    }

    abstract void checkForWeather();
}
