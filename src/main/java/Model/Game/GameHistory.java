package Model.Game;

import Model.User.User;

import java.util.ArrayList;
import java.util.Date;

public class GameHistory {

    private User user1;
    private User user2;
    private Date gameDate;
    private User winner;
    private boolean isResultDraw;
    private boolean isGameFinished;
    private int roundsPlayed;
    private ArrayList<Integer> user1EveryRoundScore = new ArrayList<>();
    private ArrayList<Integer> user2EveryRoundScore = new ArrayList<>();

    public GameHistory(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.gameDate = new Date();
    }

    public int getUser1TotalScore() {
        return 0;
    }

    public int getUser2TotalScore() {
        return 0;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public User getWinner() {
        return winner;
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

    public ArrayList<Integer> getUserEveryRoundScore(User user) {
        if (user.equals(user1))
            return user1EveryRoundScore;
        else
            return user2EveryRoundScore;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }

    public void addUserRoundScore(int score, User user) {
        if (user.equals(user1))
            user1EveryRoundScore.add(score);
        else
            user2EveryRoundScore.add(score);
    }

    public void setUser1EveryRoundScore(ArrayList<Integer> user1EveryRoundScore) {
        this.user1EveryRoundScore = user1EveryRoundScore;
    }

    public void setUser2EveryRoundScore(ArrayList<Integer> user2EveryRoundScore) {
        this.user2EveryRoundScore = user2EveryRoundScore;
    }

    public boolean isGameFinished() {
        return isGameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        isGameFinished = gameFinished;
    }

    public boolean isResultDraw() {
        return isResultDraw;
    }

    public void setResultDraw(boolean resultDraw) {
        isResultDraw = resultDraw;
    }

    public void end() {
        // TODO this should be completed when working on game
        user1.getGameInfo().addScore(getUser1TotalScore());
        user2.getGameInfo().addScore(getUser2TotalScore());
    }
}
