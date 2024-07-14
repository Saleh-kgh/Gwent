package Model.Package;

import Model.Game.GameHistory;
import Model.User.User;
import Model.User.UserGameInfo;

import java.io.Serializable;
import java.util.ArrayList;

public class GameHistoryInformationPackage implements Serializable {
    //////////////////////////////// Game info
    private int score = 0;
    private int highestScore;
    private int victoryCount;
    private int defeatCount;
    private int drawCount;
    /////////////////////////////// GameHistory
    private boolean winner; // true for user1 false for user2
    private boolean isResultDraw;
    private boolean isGameFinished;
    private int roundsPlayed;
    private ArrayList<Integer> user1EveryRoundScore = new ArrayList<>();
    private ArrayList<Integer> user2EveryRoundScore = new ArrayList<>();

    public GameHistoryInformationPackage(GameHistory gameHistory, UserGameInfo userGameInfo) {
        this.score = userGameInfo.getScore();
        this.highestScore = userGameInfo.getHighestScore();
        this.victoryCount = userGameInfo.getVictoryCount();
        this.defeatCount = userGameInfo.getDefeatCount();
        this.drawCount = userGameInfo.getDrawCount();

        this.winner = gameHistory.getUser1().equals(gameHistory.getWinner());
        this.isResultDraw = gameHistory.isResultDraw();
        this.isGameFinished = gameHistory.isGameFinished();
        this.roundsPlayed = gameHistory.getRoundsPlayed();
        this.user1EveryRoundScore = gameHistory.getUser1EveryRoundScore();
        this.user2EveryRoundScore = gameHistory.getUser2EveryRoundScore();
    }

    public int getScore() {
        return score;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public int getVictoryCount() {
        return victoryCount;
    }

    public int getDefeatCount() {
        return defeatCount;
    }

    public int getDrawCount() {
        return drawCount;
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isResultDraw() {
        return isResultDraw;
    }

    public boolean isGameFinished() {
        return isGameFinished;
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    public ArrayList<Integer> getUser1EveryRoundScore() {
        return user1EveryRoundScore;
    }

    public ArrayList<Integer> getUser2EveryRoundScore() {
        return user2EveryRoundScore;
    }
}
