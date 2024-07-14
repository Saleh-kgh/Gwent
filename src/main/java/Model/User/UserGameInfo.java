package Model.User;

import Model.Game.GameHistory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UserGameInfo implements Serializable {

    private User owner;
    private int score = 0;
    private int highestScore;
    private int victoryCount;
    private int defeatCount;
    private int drawCount;

    public UserGameInfo(User user) {
        this.owner = user;
    }

    public User getOwner() {
        return owner;
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

    public void calculateHighestScore() {
        int highest = 0;
         for (GameHistory gameHistory : getOwner().getUserGameHistories()) {
             if (getOwner().equals(gameHistory.getUser1())) {
                 for (int score : gameHistory.getUser1EveryRoundScore())
                     highest = Math.max(highest, score);
             }
             else {
                 for (int score : gameHistory.getUser2EveryRoundScore())
                     highest = Math.max(highest, score);
             }
         }
         highestScore = highest;
    }

    public void addVictoryCount(int count) {
        victoryCount += count;
    }

    public void addDefeatCount(int count) {
        defeatCount += count;
    }

    public void addDrawCount(int count) {
        drawCount += count;
    }

    public int getUserRank() {
        ArrayList<User> toBeSortedList = User.getAllUsers();
        Collections.sort(toBeSortedList, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                // positive or negative to be checked TODO
                if (o1.getGameInfo().score > o2.getGameInfo().score)
                    return 1;
                if (o1.getGameInfo().score < o2.getGameInfo().score)
                    return -1;
                return 0;
            }
        });
        for (int i = 0; i < toBeSortedList.size(); i++) {
            if (toBeSortedList.get(i).equals(owner))
                return i;
        }
        return 0;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void calculateScore() {
        int totalScore = 0;
        for (GameHistory gameHistory : getOwner().getUserGameHistories()) {
            if (getOwner().equals(gameHistory.getUser1())) {
                for (int score : gameHistory.getUser1EveryRoundScore())
                    totalScore += score;
            }
            else {
                for (int score : gameHistory.getUser2EveryRoundScore())
                    totalScore += score;
            }
        }
        score = totalScore;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public void setVictoryCount(int victoryCount) {
        this.victoryCount = victoryCount;
    }

    public void setDefeatCount(int defeatCount) {
        this.defeatCount = defeatCount;
    }

    public void setDrawCount(int drawCount) {
        this.drawCount = drawCount;
    }
}
