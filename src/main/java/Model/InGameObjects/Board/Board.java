package Model.InGameObjects.Board;

import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.UnitCard;
import Model.Game.Player;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Board extends Pane {

    private Player player;
    private DeckArea friendlyDeckArea;
    private DeckArea opponentDeckArea;
    private DiscardPileArea friendlyDiscardPileArea;
    private DiscardPileArea opponentDiscardPileArea;
    private TotalScoreArea friendlyTotalScoreArea;
    private TotalScoreArea opponentTotalScoreArea;
    private LeaderCardArea friendlyLeaderCardArea;
    private LeaderCardArea opponentLeaderCardArea;
    private CloseArea friendlyCloseArea;
    private CloseArea opponentCloseArea;
    private RangedArea friendlyRangedArea;
    private RangedArea opponentRangedArea;
    private SiegeArea friendlySiegeArea;
    private SiegeArea opponentSiegeArea;
    private WeatherCardArea weatherCardArea;
    private HandArea handArea;
    private OpponentHand opponentHand;
    private ScrollBox scrollBox;
    private InformationBar playerInformationBar;
    private InformationBar opponentInformationbar;
    private ArrayList<BoardSection> boardSections = new ArrayList<>();

    public Board(Player player) {
        this.player = player;

        friendlyDeckArea = new DeckArea(true, player ,player);
        opponentDeckArea = new DeckArea(false, player ,player.getOpponent());

        friendlyDiscardPileArea = new DiscardPileArea(true, player ,player);
        opponentDiscardPileArea = new DiscardPileArea(false, player ,player.getOpponent());

        playerInformationBar = new InformationBar(this, player, player);
        opponentInformationbar = new InformationBar(this, player, player.getOpponent());

        friendlyTotalScoreArea = new TotalScoreArea(true, player ,player);
        opponentTotalScoreArea = new TotalScoreArea(false, player ,player.getOpponent());

        friendlyLeaderCardArea = new LeaderCardArea(true, player ,player);
        opponentLeaderCardArea = new LeaderCardArea(false, player ,player.getOpponent());

        friendlyCloseArea = new CloseArea(true, player ,player);
        opponentCloseArea = new CloseArea(false, player ,player.getOpponent());

        friendlyRangedArea = new RangedArea(true,player ,player);
        opponentRangedArea = new RangedArea(false, player ,player.getOpponent());

        friendlySiegeArea = new SiegeArea(true, player ,player);
        opponentSiegeArea = new SiegeArea(false, player ,player.getOpponent());

        weatherCardArea = new WeatherCardArea(player ,player);
        handArea = new HandArea(player);
        opponentHand = new OpponentHand(0,0,0,0, player, player.getOpponent());
        addSectionsToBoardPane();
    }

    private void addSectionsToBoardPane() {
        addSectionsToBoardList();

        for (BoardSection boardSection : boardSections) {
            getChildren().add(boardSection);
            boardSection.setLayoutX(boardSection.getLayoutXOnBoard());
            boardSection.setLayoutY(boardSection.getLayoutYOnBoard());

            if (boardSection instanceof Row) {
                Row row = (Row) boardSection;
                HornArea hornArea = row.getHornArea();
                RowScoreArea rowScoreArea = row.getRowScoreArea();

                getChildren().add(hornArea);
                hornArea.setLayoutX(hornArea.getLayoutXOnBoard());
                hornArea.setLayoutY(hornArea.getLayoutYOnBoard());

                getChildren().add(rowScoreArea);
                rowScoreArea.setLayoutX(rowScoreArea.getLayoutXOnBoard());
                rowScoreArea.setLayoutY(rowScoreArea.getLayoutYOnBoard());
            }
        }
    }

    private void addSectionsToBoardList() {

        boardSections.add(friendlyDeckArea);
        boardSections.add(friendlyDiscardPileArea);
        boardSections.add(friendlyLeaderCardArea);
        boardSections.add(friendlyTotalScoreArea);
        boardSections.add(friendlyCloseArea);
        boardSections.add(friendlyRangedArea);
        boardSections.add(friendlySiegeArea);

        boardSections.add(opponentDeckArea);
        boardSections.add(opponentDiscardPileArea);
        boardSections.add(opponentLeaderCardArea);
        boardSections.add(opponentTotalScoreArea);
        boardSections.add(opponentCloseArea);
        boardSections.add(opponentRangedArea);
        boardSections.add(opponentSiegeArea);

        boardSections.add(weatherCardArea);
        boardSections.add(handArea);
    }

    public void updateAllSections() {
        for (BoardSection boardSection : boardSections) {
            boardSection.updateSection();
        }

        opponentInformationbar.updateInfoBar();
        playerInformationBar.updateInfoBar();

        updateHigherScoreIcon();
    }

    private void updateHigherScoreIcon() {
        if (opponentTotalScoreArea.getTotalScore() > friendlyTotalScoreArea.getTotalScore()) {
            friendlyTotalScoreArea.removeHigherScoreIcon();
            opponentTotalScoreArea.addHigherScoreIcon();
        }
        else {
            opponentTotalScoreArea.removeHigherScoreIcon();
            friendlyTotalScoreArea.addHigherScoreIcon();
        }
    }

    public void putNextRoundCardsOfRows() {
        for (BoardSection boardSection : boardSections) {
            if (boardSection instanceof Row)
                ((Row) boardSection).placeUnitCardsToPutNextRound();
        }
    }

    public void removeCardsGraphics() {
        for(BoardSection boardSection : boardSections) {
            for (Card card : boardSection.getCardsOnSection()) {
                card.removeGraphicalFields();
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public DeckArea getFriendlyDeckArea() {
        return friendlyDeckArea;
    }

    public DeckArea getOpponentDeckArea() {
        return opponentDeckArea;
    }

    public DiscardPileArea getFriendlyDiscardPileArea() {
        return friendlyDiscardPileArea;
    }

    public DiscardPileArea getOpponentDiscardPileArea() {
        return opponentDiscardPileArea;
    }

    public TotalScoreArea getFriendlyTotalScoreArea() {
        return friendlyTotalScoreArea;
    }

    public InformationBar getPlayerInformationBar() {
        return playerInformationBar;
    }

    public InformationBar getOpponentInformationBar() {
        return opponentInformationbar;
    }

    public TotalScoreArea getOpponentTotalScoreArea() {
        return opponentTotalScoreArea;
    }

    public LeaderCardArea getFriendlyLeaderCardArea() {
        return friendlyLeaderCardArea;
    }

    public LeaderCardArea getOpponentLeaderCardArea() {
        return opponentLeaderCardArea;
    }

    public OpponentHand getOpponentHand() {
        return opponentHand;
    }

    public CloseArea getFriendlyCloseArea() {
        return friendlyCloseArea;
    }

    public CloseArea getOpponentCloseArea() {
        return opponentCloseArea;
    }

    public RangedArea getFriendlyRangedArea() {
        return friendlyRangedArea;
    }

    public RangedArea getOpponentRangedArea() {
        return opponentRangedArea;
    }

    public SiegeArea getFriendlySiegeArea() {
        return friendlySiegeArea;
    }

    public SiegeArea getOpponentSiegeArea() {
        return opponentSiegeArea;
    }

    public WeatherCardArea getWeatherCardArea() {
        return weatherCardArea;
    }

    public HandArea getHandArea() {
        return handArea;
    }

    public ScrollBox getScrollBox() {
        return scrollBox;
    }

    public void setScrollBox(ScrollBox scrollBox) {
        this.scrollBox = scrollBox;
    }

    public ArrayList<BoardSection> getBoardSections() {
        return boardSections;
    }

    public void setBoardSections(ArrayList<BoardSection> boardSections) {
        this.boardSections = boardSections;
    }

    public int getBoardHighestCardPower() {
        int highestCardPower = -1;

        for (BoardSection boardSection : boardSections) {
            if (boardSection instanceof Row) {
                highestCardPower = Math.max(highestCardPower, ((Row) boardSection).getHighestCardPowerInRow());
            }
        }

        return highestCardPower;
    }

    public int getFrCloseAreaHighestCardPower() {
        int highestCardPower = -1;

        for (Card card : friendlyCloseArea.getCardsOnSection()) {
            if (card instanceof UnitCard) {
                highestCardPower = Math.max(highestCardPower, ((UnitCard) card).getCurrentPower());
            }
        }

        return highestCardPower;
    }

    public int getOppCloseAreaHighestCardPower() {
        int highestCardPower = -1;

        for (Card card : opponentCloseArea.getCardsOnSection()) {
            if (card instanceof UnitCard) {
                highestCardPower = Math.max(highestCardPower, ((UnitCard) card).getCurrentPower());
            }
        }

        return highestCardPower;
    }

    public int getFrRangedAreaHighestCardPower() {
        int highestCardPower = -1;

        for (Card card : friendlyRangedArea.getCardsOnSection()) {
            if (card instanceof UnitCard) {
                highestCardPower = Math.max(highestCardPower, ((UnitCard) card).getCurrentPower());
            }
        }

        return highestCardPower;
    }

    public int getOppRangedAreaHighestCardPower() {
        int highestCardPower = -1;

        for (Card card : friendlyRangedArea.getCardsOnSection()) {
            if (card instanceof UnitCard) {
                highestCardPower = Math.max(highestCardPower, ((UnitCard) card).getCurrentPower());
            }
        }

        return highestCardPower;
    }

    public int getFrSiegeAreaHighestCardPower() {
        int highestCardPower = -1;

        for (Card card : friendlySiegeArea.getCardsOnSection()) {
            if (card instanceof UnitCard) {
                highestCardPower = Math.max(highestCardPower, ((UnitCard) card).getCurrentPower());
            }
        }

        return highestCardPower;
    }

    public int getOppSiegeAreaHighestCardPower() {
        int highestCardPower = -1;

        for (Card card : opponentSiegeArea.getCardsOnSection()) {
            if (card instanceof UnitCard) {
                highestCardPower = Math.max(highestCardPower, ((UnitCard) card).getCurrentPower());
            }
        }

        return highestCardPower;
    }
}
