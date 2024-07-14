package controller;

import Model.User.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LeaderBoardController {
    public ArrayList<String> getLeaderBoardReport() {
        ArrayList<User> users = User.getAllUsers();

        Comparator<User> byScore = Comparator.comparingInt(u -> u.getGameInfo().getScore());
        users.sort(byScore.reversed());

        ArrayList<String> leaderBoardReport = new ArrayList<>();
        int rank = 1;
        for (User user : users) {
            leaderBoardReport.add(rank + ". " + user.getUsername() + " - Score: " + user.getGameInfo().getScore() + " " + user.isOnline());
            rank++;
        }
        return leaderBoardReport;
    }
}