package controller;

import Model.Result;
import Model.User.User;

public class MainController {

    public Result createGameWithPlayer(User user) {
        Result result = new Result ("no", user.isPlayingGame());
        return null;
    }

    public void loader() {}
}
