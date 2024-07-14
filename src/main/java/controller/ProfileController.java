package controller;

import Model.DataSaver;
import Model.Game.GameHistory;
import Model.Result;
import Model.User.User;
import view.AppView;
import Enum.LoginRegex;

public class ProfileController {


    public Result changeUsername(String username, String oldUsername) {
        if (username.equals(oldUsername))
            return new Result("username problem", "new username must be different from old one", false);
        if (User.doesUserExist(username)) return new Result("username taken", false);
        if (!LoginRegex.VALID_USERNAME.matches(username)) return new Result("username invalid", false);

        User.getUserByUsername(oldUsername).setUsername(username);
        DataSaver.saveUsers(User.getAllUsers());

        return new Result("changed", "username changed successfully", true);
    }

    public Result changeNickname(String nickname, String oldUsername) {
        System.out.println(oldUsername);
        if (nickname.equals(User.getUserByUsername(oldUsername).getNickname()))
            return new Result("Nickname", "new Nickname must be different from old one", false);

        User.getUserByUsername(oldUsername).setNickname(nickname);
        DataSaver.saveUsers(User.getAllUsers());
        return new Result("changed", "nickname changed successfully", true);
    }

    public Result changeEmail(String email, String oldUsername) {
        if (email.equals(User.getUserByUsername(oldUsername).getEmail()))
            return new Result("email", "new email must be different from old one", false);
        if (!LoginRegex.VALID_EMAIL.matches(email)) return new Result("email invalid", false);

        User.getUserByUsername(oldUsername).setEmail(email);
        DataSaver.saveUsers(User.getAllUsers());
        return new Result("changed", "email changed successfully", true);
    }

    public Result changePassword(String password, String oldUsername) {
        if (!LoginRegex.VALID_PASSWORD.matches(password)) return new Result("password invalid", false);
        if (!LoginRegex.STRONG_PASSWORD.matches(password)) return new Result("password weak", false);

        User.getUserByUsername(oldUsername).setPassword(password);
        DataSaver.saveUsers(User.getAllUsers());
        return new Result("changed", "password changed successfully", true);
    }

    public Result showUserInfo(String username) {
        User user = User.getUserByUsername(username);
        StringBuilder info = new StringBuilder();
        info.append("username: ").append(user.getUsername()).append("\n");
        info.append("nickname: ").append(user.getNickname()).append("\n");
        info.append("rank: ").append(user.getGameInfo().getUserRank()).append("\n");
        int totalGamesNumber = user.getGameInfo().getVictoryCount() +
                user.getGameInfo().getDrawCount() +
                user.getGameInfo().getDefeatCount();
        info.append("total games: ").append(totalGamesNumber).append("\n");
        info.append("wins: ").append(user.getGameInfo().getVictoryCount()).append("\n");
        info.append("defeats: ").append(user.getGameInfo().getDefeatCount()).append("\n");
        info.append("draws: ").append(user.getGameInfo().getDrawCount()).append("\n");

        return new Result("INFO:", info.toString(), true);
    }

    public Result showGameHistory(int n, String username) {
        User user = User.getUserByUsername(username);
        if (n <= 0)
            return new Result("invalid number", "enter a number greater than 1", false);
        assert user != null;
        if (user.getUserGameHistories().isEmpty())
            return new Result("game history", "empty history", false);
        if (user.getUserGameHistories().size() < n)
            return new Result("game history", "you don't have enough games as you entered", false);
        StringBuilder history = new StringBuilder();
        for (GameHistory game : user.getUserGameHistories()) {
            history.append(game.getUser2()).append("\n");
            history.append("against: ").append(game.getGameDate()).append("\n");
            history.append("you score:").append(game.getUserEveryRoundScore(game.getUser1())).append("\n");
            history.append("you total score:").append(game.getUser1TotalScore()).append("\n");
            history.append("opponent score").append(game.getUserEveryRoundScore(game.getUser2())).append("\n");
            history.append("opponent total score").append(game.getUser1TotalScore()).append("\n");
            history.append("winner: ").append(game.getWinner().getUsername());
            history.append("=====Gwent is awesome=====Gwent is awesome=====Gwent is awesome=====");
        }
        return new Result("History", history.toString(), true);
    }
}
