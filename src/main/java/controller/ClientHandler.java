package controller;


import Model.Game.GameHistory;
import Model.Package.GameHistoryInformationPackage;
import Model.Package.ServerPackage;
import Model.User.User;
import Model.DataSaver;
import Model.FriendRequest;
import Model.Result;

import Enum.LoginRegex;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class ClientHandler implements Runnable {

    private boolean isQuitGame = false;
    private boolean isInPrivateGame;
    private int randomCode;
    private String friendThatIsWaiting = null;
    private static int gameCode = 0;
    private static ArrayList<ArrayList<ClientHandler>> usersThatAreBusyInGame = new ArrayList<>();// playing or watching
    private static HashMap<Integer, ArrayList<ClientHandler>> HistoryOfALlUsersBusyInGame = new HashMap();

    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private static ArrayList<ClientHandler> clientHandlersThatWantToPlay = new ArrayList<>();

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private User clientUser;
    private Server server;
    private final RegisterController registerController = new RegisterController();
    private final LoginController loginController = new LoginController();
    private final MainController mainController = new MainController();
    private final ProfileController profileController = new ProfileController();
    private final LeaderBoardController leaderBoardController = new LeaderBoardController();
    private final static DataSaver dataSaver = new DataSaver();

    public ClientHandler(Socket socket, Server server) {

        try {
            this.socket = socket;
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            clientHandlers.add(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {

        while (socket.isConnected()) {

            try {

                ServerPackage obj = (ServerPackage) objectInputStream.readObject();
                if (Objects.equals(obj.command, "login")) {
                    //clientUser = ((User)(obj.object));
                    //System.out.println(clientUser.getUsername() + " logged in!");

                    loginServer(obj.object);
                } else if (Objects.equals(obj.command, "joinGameAsObserver")) {
                    sendJoinResult((String) obj.object);
                } else if (Objects.equals(obj.command, "sendMessage")) {
                    String message = ((String) (obj.object));
                    broadCastMessage(message);
                } else if (Objects.equals(obj.command, "getAllGames")) {
                    sendAllGamesList();
                } else if (Objects.equals(obj.command, "getCurrentGames")) {
                    sendCurrentGamesList();
                } else if (Objects.equals(obj.command, "register")) {
                    sendRegisterResult(obj.object);
                } else if (Objects.equals(obj.command, "registerSec")) {
                    updateSec(obj.object);
                } else if (Objects.equals(obj.command, "securityQuestion")) {
                    giveQuestion(obj.object);
                } else if (Objects.equals(obj.command, "securityAnswer")) {
                    loginWithSec(obj.object);
                } else if (Objects.equals(obj.command, "leaderBoard")) {
                    sendLeaderBoard();
                } else if (Objects.equals(obj.command, "usernameChange")) {
                    assert clientUser != null;
                    Result result = profileController.changeUsername((String) obj.object, clientUser.getUsername());
                    objectOutputStream.reset();
                    sendChangeResult(result, "changeUsername");
                } else if (Objects.equals(obj.command, "emailChange")) {
                    assert clientUser != null;
                    Result result = profileController.changeEmail((String) obj.object, clientUser.getUsername());
                    objectOutputStream.reset();
                    sendChangeResult(result, "changeEmail");
                } else if (Objects.equals(obj.command, "nicknameChange")) {
                    objectOutputStream.reset();
                    Result result = profileController.changeNickname((String) obj.object, clientUser.getUsername());
                    sendChangeResult(result, "changeNickName");
                } else if (Objects.equals(obj.command, "passCh")) {
                    objectOutputStream.reset();
                    Result result = profileController.changePassword((String) obj.object, clientUser.getUsername());
                    sendChangePassResult(result);
                } else if (Objects.equals(obj.command, "info")) {
                    assert clientUser != null;
                    Result result = profileController.showUserInfo(clientUser.getUsername());
                    objectOutputStream.reset();
                    objectOutputStream.writeObject(new ServerPackage("getInfo", result));
                } else if (Objects.equals(obj.command, "getProfile")) {
                    assert clientUser != null;
                    Result result;
                    if (User.getUserByUsername((String) obj.object) == null)
                        result = new Result("no such user", false);
                    else if (!clientUser.isYourFriend(User.getUserByUsername((String) obj.object))) {
                        result = new Result("oops", "he is not your friend", false);
                    } else
                        result = profileController.showUserInfo(User.getUserByUsername((String) obj.object).getUsername());
                    objectOutputStream.reset();
                    objectOutputStream.writeObject(new ServerPackage("getInfo", result));
                } else if (Objects.equals(obj.command, "gameHistory")) {
                    assert clientUser != null;
                    Result result = profileController.showGameHistory(Integer.parseInt((String) obj.object), clientUser.getUsername());
                    objectOutputStream.reset();
                    objectOutputStream.writeObject(new ServerPackage("gameHistory", result));
                } else if (Objects.equals(obj.command, "endClient")) {
                    clientUser.online = false;
                    clientUser = null;
                } else if (Objects.equals(obj.command, "getGameReqs")) {
                    sendGameReq();
                } else if (Objects.equals(obj.command, "getFriendRequests")) {
                    sendHistoryOfRequests();
                } else if (Objects.equals(obj.command, "search")) {
                    searchFor((String) obj.object);
                } else if (Objects.equals(obj.command, "friendReq")) {
                    sendFriendReq((String) obj.object);

                } else if (Objects.equals(obj.command, "getNewUserForDeck")) {
                    clientUser.setCardInventory(((User) obj.object).getCardInventory());
                    DataSaver.saveUsers(User.getAllUsers());
                } else if (Objects.equals(obj.command, "getNewUserForGameHistory")) {
                    updateUserGameHistoryInformation(obj);
                    DataSaver.saveUsers(User.getAllUsers());
                } else if (Objects.equals(obj.command, "accept")) {
                    Result result;
                    if (FriendRequest.getRequest((String) obj.object, clientUser.getUsername()) == null) {
                        result = new Result("no such request or user", false);
                        try {
                            objectOutputStream.writeObject(
                                    new ServerPackage("accR", result));
                        } catch (IOException e) {
                            System.out.println(" failed r");
                        }
                    } else if (clientUser.isYourFriend(User.getUserByUsername((String) obj.object))) {
                        result = new Result("oh!", "you are already friends ", false);
                        try {
                            objectOutputStream.writeObject(
                                    new ServerPackage("accR", result));
                        } catch (IOException e) {
                            System.out.println(" failed r");
                        }
                    } else {
                        Objects.requireNonNull(FriendRequest.getRequest((String) obj.object, clientUser.getUsername())).accept();
                        result = new Result("nice!", "you are friends now ", true);
                        for (User user : User.getAllUsers()) {
                            user.getFriendsNames().clear();
                            for (User friend : user.getFriends()) {
                                user.getFriendsNames().add(friend.getUsername());
                            }
                        }
                        DataSaver.saveUsers(User.getAllUsers());
                        try {
                            objectOutputStream.writeObject(
                                    new ServerPackage("accR", result));
                        } catch (IOException e) {
                            System.out.println(" failed r");
                        }
                    }
                } else if (Objects.equals(obj.command, "reject")) {
                    Result result;
                    if (FriendRequest.getRequest((String) obj.object, clientUser.getUsername()) == null) {
                        result = new Result("no such request or user", false);
                        try {
                            objectOutputStream.writeObject(
                                    new ServerPackage("rejR", result));
                        } catch (IOException e) {
                            System.out.println(" failed r");
                        }
                    } else {
                        Objects.requireNonNull(FriendRequest.getRequest((String) obj.object, clientUser.getUsername())).reject();
                        result = new Result("ok", "his/her friendship request rejected :(", true);
                        DataSaver.saveUsers(User.getAllUsers());
                        try {
                            objectOutputStream.writeObject(
                                    new ServerPackage("rejR", result));
                        } catch (IOException e) {
                            System.out.println(" failed r");
                        }
                    }
                } else if (Objects.equals(obj.command, "getFriends")) {
                    objectOutputStream.reset();
                    sendFriends();
                } else if (Objects.equals(obj.command, "startNewRandomGame")) {

                    clientUser = (User) (obj.object);
                    findGame(this);
                } else if (Objects.equals(obj.command, "moveCard") ||
                        Objects.equals(obj.command, "addCard") ||
                        Objects.equals(obj.command, "removeCard") ||
                        Objects.equals(obj.command, "sfxAnimation") ||
                        Objects.equals(obj.command, "playerTurn") ||
                        Objects.equals(obj.command, "playerHand") ||
                        Objects.equals(obj.command, "playerDeck") ||
                        Objects.equals(obj.command, "cheatPackage") ||
                        Objects.equals(obj.command, "weatherEffect")) {

                    sendGamePackageToOpponent(obj);
                } else if (Objects.equals(obj.command, "updateUser")) {
                    clientUser = (User) obj.object;
                } else if (Objects.equals(obj.command, "requestForNewGameWithFriend")) {
                    requestForNewGameWithFriend((String) obj.object);
                } else if (Objects.equals(obj.command, "broDoesn'tWantToPlay")) {
                    broDoesntWantToPlay((ArrayList<String>) obj.object);
                } else if (Objects.equals(obj.command, "broWantsToPlay")) {
                    broWantsToPlay((ArrayList<String>) obj.object);
                } else if (Objects.equals(obj.command, "sendEmoji")) {
                    sendEmoji((Integer) obj.object);
                } else if (Objects.equals(obj.command, "giveNumberForAuthorization")) {
                    giveNumberForAuthorization((String) obj.object);
                } else if (Objects.equals(obj.command, "checkCode")) {
                    objectOutputStream.writeObject(new ServerPackage("checkCode", randomCode == Integer.parseInt((String) obj.object)));
                    randomCode = -1;
                } else if (Objects.equals(obj.command, "addUser")) {
                    addUser((ArrayList<String>) obj.object);

                } else if (Objects.equals(obj.command, "screenShot")) {
                    saveImage((byte[]) obj.object);
                } else if (Objects.equals(obj.command, "sendReactionMessage")) {
                    sendReactionMessage((Integer) obj.object);
                } else if (Objects.equals(obj.command, "endGame")) {
                    endGame((String) obj.object);
                }
            } catch (IOException e) {
                if (clientUser != null) {
                    System.out.println(this + " with this user name " + clientUser.getUsername() + " disconnected ");
                } else {
                    System.out.println(this + " disconnected ");
                }
                clientHandlers.remove(this);
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private synchronized static void endGame(String username) {

        for (ArrayList<ClientHandler> list : usersThatAreBusyInGame) {

            if (list.size() >= 2) {
                if (Objects.equals(list.get(0).clientUser.getUsername(), username)) {

                    list.get(0).isQuitGame = true;

                    if (list.get(0).isQuitGame && list.get(1).isQuitGame) {
                        usersThatAreBusyInGame.remove(list);
                        list.get(0).isQuitGame = false;
                        list.get(1).isQuitGame = false;
                    }
                    break;
                } else if (Objects.equals(list.get(1).clientUser.getUsername(), username)) {

                    list.get(1).isQuitGame = true;
                    if (list.get(0).isQuitGame && list.get(1).isQuitGame) {
                        usersThatAreBusyInGame.remove(list);
                        list.get(0).isQuitGame = false;
                        list.get(1).isQuitGame = false;
                    }
                    break;
                }
            }


        }
    }
    private void addUser(ArrayList<String> user) {
        String username = user.get(0);
        String password = user.get(1);
        String nickname = user.get(2);
        String email = user.get(3);
        new User(username, password, nickname, email, true);
        DataSaver.saveUsers(User.getAllUsers());
    }

    private int getRandomSixDigits() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        return random.nextInt((max - min) + 1) + min;
    }

    private void sendGameReq() {
        try {
            objectOutputStream.writeObject(new ServerPackage("getFameReq", clientUser.getGameRequestsLog()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendJoinResult(String name) {
        Result result;
        for (ArrayList<ClientHandler> clientHandlerArrayList : usersThatAreBusyInGame) {
            if (clientHandlerArrayList.get(0).clientUser.getUsername().equals(name) ||
                    clientHandlerArrayList.get(1).clientUser.getUsername().equals(name)) {
                clientHandlerArrayList.add(this);
                result = new Result("joined", "let's go!", true);
                try {
                    objectOutputStream.writeObject(new ServerPackage("joinObserver", result));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
        result = new Result("no such user in room", "", false);
        try {
            objectOutputStream.writeObject(new ServerPackage("joinObserver", result));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void giveNumberForAuthorization(String email) {
        randomCode = getRandomSixDigits();
        boolean check = SendEmail.run(email, "Authorization", String.valueOf(randomCode));
        if (!check) {
            randomCode = -1;
        }
    }

    private void sendReactionMessage(Integer integer) {
        for (ArrayList<ClientHandler> list : usersThatAreBusyInGame) {
            if (list.size() >= 2) {

                if (Objects.equals(list.get(0).clientUser.getUsername(), clientUser.getUsername())) {
                    try {
                        list.get(1).objectOutputStream.writeObject(new ServerPackage("receiveReactionMessage", integer));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (Objects.equals(list.get(1).clientUser.getUsername(), clientUser.getUsername())) {
                    try {
                        list.get(0).objectOutputStream.writeObject(new ServerPackage("receiveReactionMessage", integer));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            }
        }
    }

    private void sendEmoji(Integer integer) {
        for (ArrayList<ClientHandler> list : usersThatAreBusyInGame) {
            if (list.size() >= 2) {

                if (Objects.equals(list.get(0).clientUser.getUsername(), clientUser.getUsername())) {
                    try {
                        list.get(1).objectOutputStream.writeObject(new ServerPackage("receiveEmoji", integer));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (Objects.equals(list.get(1).clientUser.getUsername(), clientUser.getUsername())) {
                    try {
                        list.get(0).objectOutputStream.writeObject(new ServerPackage("receiveEmoji", integer));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            }
        }
    }

    private void broWantsToPlay(ArrayList<String> input) {
        String username = input.get(0);
        User.getUserByUsername(input.get(0)).getGameRequestsLog().add(input.get(0) + " accepted to play with " + input.get(1));
        User.getUserByUsername(input.get(1)).getGameRequestsLog().add(input.get(0) + " accepted to play with " + input.get(1));
        DataSaver.saveUsers(User.getAllUsers());
        for (ClientHandler clientHandler : clientHandlers) {
            if (Objects.equals(clientHandler.clientUser.getUsername(), username)) {
                try {
                    friendThatIsWaiting = username;
                    clientHandler.friendThatIsWaiting = clientUser.getUsername();
                    clientHandler.objectOutputStream.writeObject(new ServerPackage("friendAcceptGame",
                            new Result("Accept ", "bro is ready", true)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }

    private void broDoesntWantToPlay(ArrayList<String> input) {
        String username = input.get(0);
        User.getUserByUsername(input.get(0)).getGameRequestsLog().add(input.get(0) + " rejected to play with " + input.get(1));
        User.getUserByUsername(input.get(1)).getGameRequestsLog().add(input.get(0) + " rejected to play with " + input.get(1));
        DataSaver.saveUsers(User.getAllUsers());
        for (ClientHandler clientHandler : clientHandlers) {
            if (Objects.equals(clientHandler.clientUser.getUsername(), username)) {
                try {
                    clientHandler.objectOutputStream.writeObject(new ServerPackage("friendRejectGame",
                            new Result("Reject ", "bro doesn't want to play with you", false)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }

    private void requestForNewGameWithFriend(String username) {
        boolean check = false;
        for (ClientHandler clientHandler : clientHandlers) {
            if (Objects.equals(clientHandler.clientUser.getUsername(), username)) {
                check = true;
                break;
            }
        }
        if (!check) {
            try {
                objectOutputStream.writeObject(new ServerPackage("friendIsOffline",
                        new Result("offline", "bro is offline", false)));
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (ArrayList<ClientHandler> list : usersThatAreBusyInGame) {
            for (ClientHandler clientHandler : list) {
                if (Objects.equals(clientHandler.clientUser.getUsername(), username)) {
                    try {
                        clientUser.getGameRequestsLog().add(username +
                                "was too busy to play with" +
                                clientUser.getUsername());
                        User.getUserByUsername(username).getGameRequestsLog().add(username +
                                "was too busy to play with" +
                                clientUser.getUsername());
                        DataSaver.saveUsers(User.getAllUsers());
                        objectOutputStream.writeObject(new ServerPackage("friendIsBusy",
                                new Result("busy", "player is playing game or watching game", false)));
                        return;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        for (ClientHandler clientHandler : clientHandlers) {
            if (Objects.equals(clientHandler.clientUser.getUsername(), username)) {
                try {
                    clientHandler.objectOutputStream.writeObject(new ServerPackage("someoneWantToPlayWithYou", clientUser.getUsername()));
                    return;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    private void sendFriends() {
        ArrayList<String> output = new ArrayList<>();
        for (User user : clientUser.getFriends()) {
            output.add(user.getUsername() + " " + user.getNickname());
        }
        try {
            objectOutputStream.writeObject(
                    new ServerPackage("friends", output));
        } catch (IOException e) {
            System.out.println(" failed r");
        }
    }


    private synchronized static void findGame(ClientHandler clientHandler) throws IOException {

        clientHandlersThatWantToPlay.add(clientHandler);

        if (clientHandler.friendThatIsWaiting == null) {
            boolean check = false;
            ClientHandler opponent = null;
            for (ClientHandler handler : clientHandlersThatWantToPlay) {
                if (handler != clientHandler && handler.friendThatIsWaiting == null) {
                    check = true;
                    opponent = handler;
                    break;
                }
            }
            if (check) {
                User firstUser = opponent.clientUser;
                User secondUser = clientHandler.clientUser;

                ArrayList<ClientHandler> busy = new ArrayList<>();
                busy.add(opponent);
                busy.add(clientHandler);
                usersThatAreBusyInGame.add(busy);
                HistoryOfALlUsersBusyInGame.put(gameCode, busy); // for game room
                gameCode++;
                firstUser.setOpponentUser(secondUser);
                secondUser.setOpponentUser(firstUser);

                ArrayList<User> bothUsers = new ArrayList<>();
                Random random = new Random();
                int randomNumber = random.nextInt(2);
                if (randomNumber == 0) {
                    bothUsers.add(firstUser);
                    bothUsers.add(secondUser);
                } else {
                    bothUsers.add(secondUser);
                    bothUsers.add(firstUser);
                }

                opponent.objectOutputStream.writeObject(
                        new ServerPackage("makeGameWithUsers", bothUsers));
                clientHandler.objectOutputStream.writeObject(
                        new ServerPackage("makeGameWithUsers", bothUsers));
                clientHandlersThatWantToPlay.remove(opponent);
                clientHandlersThatWantToPlay.remove(clientHandler);

                clientHandler.isInPrivateGame = false;
                opponent.isInPrivateGame = false;
            }
        } else {

            boolean check = false;
            ClientHandler friend = null;
            for (ClientHandler handler : clientHandlersThatWantToPlay) {
                if (Objects.equals(handler.friendThatIsWaiting, clientHandler.clientUser.getUsername())) {
                    check = true;
                    friend = handler;
                    break;
                }
            }

            if (check) {
                User firstUser = friend.clientUser;
                User secondUser = clientHandler.clientUser;

                ArrayList<ClientHandler> busy = new ArrayList<>();
                busy.add(friend);
                busy.add(clientHandler);
                usersThatAreBusyInGame.add(busy);
                HistoryOfALlUsersBusyInGame.put(gameCode, busy); // for game room
                gameCode++;
                firstUser.setOpponentUser(secondUser);
                secondUser.setOpponentUser(firstUser);

                ArrayList<User> bothUsers = new ArrayList<>();
                Random random = new Random();
                int randomNumber = random.nextInt(2);
                if (randomNumber == 0) {
                    bothUsers.add(firstUser);
                    bothUsers.add(secondUser);
                } else {
                    bothUsers.add(secondUser);
                    bothUsers.add(firstUser);
                }

                friend.objectOutputStream.writeObject(
                        new ServerPackage("makeGameWithUsers", bothUsers));
                clientHandler.objectOutputStream.writeObject(
                        new ServerPackage("makeGameWithUsers", bothUsers));
                clientHandlersThatWantToPlay.remove(friend);
                clientHandlersThatWantToPlay.remove(clientHandler);

                clientHandler.isInPrivateGame = true;
                friend.isInPrivateGame = true;

            }
        }
    }


    private void sendFriendReq(String object) {
        Result result;
        if (User.getUserByUsername(object) == null)
            result = new Result("request result",
                    "user not found",
                    false);
        else if (object.equals(clientUser.getUsername())) {
            result = new Result("request result",
                    "really? with yourself???",
                    false);
        } else {
            FriendRequest r = new FriendRequest(clientUser, User.getUserByUsername(object));
            clientUser.getFriendRequests().add(r);
            User.getUserByUsername(object).getFriendRequests().add(r);
            DataSaver.saveUsers(User.getAllUsers());
            result = new Result("request result", "sent successfully", true);
        }
        try {
            objectOutputStream.writeObject(
                    new ServerPackage("reqRes", result));
        } catch (IOException e) {
            System.out.println(" failed request username change");
        }
    }

    private void searchFor(String object) {
        ArrayList<String> result = new ArrayList<>();
        for (User u : User.getAllUsers()) {
            if (u.getUsername().startsWith(object))
                result.add(u.getUsername() + "  " + u.getNickname() + "  " + u.getEmail());
        }
        try {
            objectOutputStream.writeObject(
                    new ServerPackage("search", result));
        } catch (IOException e) {
            System.out.println(" failed request username change");
        }
    }

    private void sendHistoryOfRequests() {
        ArrayList<String> result = new ArrayList<>();
        for (FriendRequest r : clientUser.getFriendRequests()) {
            result.add(r.toString());
        }
        try {
            objectOutputStream.writeObject(
                    new ServerPackage("RHistory", result));
        } catch (IOException e) {
            System.out.println("failed request username change");
        }
    }

    private void sendChangePassResult(Result result) {
        try {
            objectOutputStream.writeObject(
                    new ServerPackage("passR", result));
        } catch (IOException e) {
            System.out.println(" failed request username change");
        }
    }

    private void sendChangeResult(Result result, String whatToChange) {
        try {
            objectOutputStream.writeObject(
                    new ServerPackage(whatToChange, result));
        } catch (IOException e) {
            System.out.println(" failed request username change");
        }
    }

    private void sendLeaderBoard() {
        try {
            objectOutputStream.writeObject(
                    new ServerPackage("ld",
                            leaderBoardController.getLeaderBoardReport()));
        } catch (IOException e) {
            System.out.println(" failed request getting CurrentGamesList");
        }
    }

    private void loginWithSec(Object arrayList) {
        ArrayList<String> input = new ArrayList<>((ArrayList<String>) arrayList);
        String username, num, answer, password;
        username = input.get(0);
        num = input.get(1);
        answer = input.get(2);
        password = input.get(3);

        Result result = loginController.checkQuestionSecurity(num, answer, username);
        if (!LoginRegex.VALID_PASSWORD.matches(password))
            result = new Result("oh", "password invalid", false);
        if (!LoginRegex.STRONG_PASSWORD.matches(password))
            result = new Result("oh", "password weak", false);
        ArrayList<Object> output = new ArrayList<>();
        output.add(result);
        if (result.isSuccessful()) {
            Objects.requireNonNull(User.getUserByUsername(username)).setPassword(password);
            output.add(User.getUserByUsername(username));
            this.clientUser = User.getUserByUsername(username);
            this.clientUser.online = true;
        }
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new ServerPackage("loginResult", output));
        } catch (IOException e) {
            System.out.println(" failed request getting CurrentGamesList");
        }
    }

    private void giveQuestion(Object arrayList) {
        ArrayList<String> input = new ArrayList<>((ArrayList<String>) arrayList);
        Result output = loginController.getQuestion(Integer.parseInt(input.get(0)), input.get(1));
        try {
            objectOutputStream.writeObject(new ServerPackage("secQuestion", output));
        } catch (IOException e) {
            System.out.println(" failed request getting CurrentGamesList");
        }
    }

    private void loginServer(Object arrayList) {
        ArrayList<String> input = new ArrayList<>((ArrayList<String>) arrayList);
        if (input.size() != 2) {
            System.out.println("invalid args security");
            return;
        }
        String username, password;
        username = input.get(0);
        password = input.get(1);

        Result result = loginController.login(username, password);
        ArrayList<Object> output = new ArrayList<>();
        output.add(result);
        if (result.isSuccessful()) {
            output.add(User.getUserByUsername(username));
            this.clientUser = User.getUserByUsername(username);
            System.out.println(username + " logged in");
            this.clientUser.online = true;
        }
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new ServerPackage("loginResult", output));
        } catch (IOException ignored) {
        }
    }

    private void updateSec(Object arrayList) {
        ArrayList<String> input = new ArrayList<>((ArrayList<String>) arrayList);
        if (input.size() != 3) {
            System.out.println("invalid args security");
            return;
        }
        String username, question, answer;
        username = input.get(0);
        question = input.get(1);
        answer = input.get(2);
        registerController.saveQuestion(question, username);
        registerController.saveAnswer(answer, username);
    }

    private void sendRegisterResult(Object arrayList) {
        String username, password, nickName, email;
        try {
            ArrayList<String> input = new ArrayList<>((ArrayList<String>) arrayList);
            if (input.size() != 4) {
                System.out.println("invalid args register");
                return;
            }
            username = input.get(0);
            password = input.get(1);
            nickName = input.get(2);
            email = input.get(3);
        } catch (Exception e) {
            System.out.println("failed to receive register data");
            return;
        }
        Result result = registerController.register(username, password, nickName, email);
        try {
            objectOutputStream.writeObject(new ServerPackage("registerResult", result));
        } catch (IOException e) {
            System.out.println(this.clientUser.getUsername() + " failed request getting CurrentGamesList");
        }
    }

    private void broadCastMessage(String message) {

        ArrayList<ClientHandler> viewersAndPlayers = new ArrayList<>();
        for (ArrayList<ClientHandler> list : usersThatAreBusyInGame) {
            if (list.size() >= 2) {
                if (list.get(0) == this || list.get(1) == this) {
                    viewersAndPlayers = list;
                    break;
                }
            }
        }
        for (ClientHandler clientHandler : viewersAndPlayers) {
            if (clientHandler != this) {
                try {
                    LocalTime currentTime = LocalTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    String formattedTime = currentTime.format(formatter);
                    clientHandler.objectOutputStream.writeObject(new ServerPackage("getMessageFromOther",
                            clientUser.getUsername() + " (" + formattedTime + ") >> " + message));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void sendAllGamesList() {
        ArrayList<String> output = new ArrayList<>();
        for (int i = 0; i < gameCode; i++) {
            ArrayList<ClientHandler> clientHandlerArrayList = HistoryOfALlUsersBusyInGame.get(i);
            if (clientHandlerArrayList.get(0).isInPrivateGame || clientHandlerArrayList.get(1).isInPrivateGame)
                continue;
            output.add("game code:" + i + " " + clientHandlerArrayList.get(0).clientUser.getUsername() +
                    " vs " +
                    clientHandlerArrayList.get(1).clientUser.getUsername());
        }
        if (HistoryOfALlUsersBusyInGame.isEmpty())
            output.add("no rooms found");
        try {
            objectOutputStream.writeObject(new ServerPackage("allGamesList", output));
        } catch (IOException e) {
            System.out.println(this.clientUser.getUsername() + " failed request getting AllGamesList");
        }
    }

    public void sendCurrentGamesList() {
        ArrayList<String> output = new ArrayList<>();
        for (ArrayList<ClientHandler> clientHandlerArrayList : usersThatAreBusyInGame) {
            if (clientHandlerArrayList.get(0).isInPrivateGame || clientHandlerArrayList.get(1).isInPrivateGame)
                continue;
            output.add(clientHandlerArrayList.get(0).clientUser.getUsername() +
                    " vs " +
                    clientHandlerArrayList.get(1).clientUser.getUsername());
        }
        if (usersThatAreBusyInGame.isEmpty())
            output.add("no rooms found");
        try {
            objectOutputStream.writeObject(new ServerPackage("currentGamesList", output));
        } catch (IOException e) {
            System.out.println(this.clientUser.getUsername() + " failed request getting CurrentGamesList");
        }
    }

    public void sendGamePackageToOpponent(ServerPackage serverPackage) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.clientUser.getUsername().equals(clientUser.getOpponentUser().getUsername())) {
                try {
                    clientHandler.objectOutputStream.writeObject(serverPackage);
                    break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void updateUserGameHistoryInformation(ServerPackage serverPackage) {
        GameHistoryInformationPackage ghiPackage = (GameHistoryInformationPackage) serverPackage.object;

        User user = User.getUserByUsername(clientUser.getUsername());

        user.getGameInfo().setScore(ghiPackage.getScore());
        user.getGameInfo().setHighestScore(ghiPackage.getHighestScore());
        user.getGameInfo().setVictoryCount(ghiPackage.getVictoryCount());
        user.getGameInfo().setDefeatCount(ghiPackage.getDefeatCount());
        user.getGameInfo().setDrawCount(ghiPackage.getDrawCount());

        for (ArrayList<ClientHandler> gameUserClientHandlers : usersThatAreBusyInGame) {
            if (gameUserClientHandlers.get(0).clientUser.equals(clientUser)) {
                user.getUserGameHistories().add(new GameHistory(user, gameUserClientHandlers.get(1).clientUser));
                break;
            }
            else if (gameUserClientHandlers.get(1).clientUser.equals(clientUser)){
                user.getUserGameHistories().add(new GameHistory(gameUserClientHandlers.get(0).clientUser, user));
                break;
            }
        }

        GameHistory gameHistory = user.getUserGameHistories().get(user.getUserGameHistories().size() - 1);

        if (ghiPackage.isWinner()) gameHistory.setWinner(gameHistory.getUser1());
        else gameHistory.setWinner(gameHistory.getUser2());

        gameHistory.setGameFinished(ghiPackage.isGameFinished());
        gameHistory.setResultDraw(ghiPackage.isResultDraw());
        gameHistory.setRoundsPlayed(ghiPackage.getRoundsPlayed());
        gameHistory.setUser1EveryRoundScore(ghiPackage.getUser1EveryRoundScore());
        gameHistory.setUser2EveryRoundScore(ghiPackage.getUser2EveryRoundScore());
    }

    public void saveImage(byte[] imageBytes) {
        try {
            // Convert byte array to BufferedImage
//            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
//            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            for (ArrayList<ClientHandler> clientHandlerArrayList : usersThatAreBusyInGame) {
                if (clientHandlerArrayList.size() > 2 &&
                        (
                                clientHandlerArrayList.get(0).clientUser.getUsername().equals(this.clientUser.getUsername()) ||
                                        clientHandlerArrayList.get(1).clientUser.getUsername().equals(this.clientUser.getUsername())
                        )) {
                    for (int index = 2; index < clientHandlerArrayList.size(); index++) {
                        ServerPackage serverPackage = new ServerPackage("image", imageBytes);
                        clientHandlerArrayList.get(index).objectOutputStream.writeObject(serverPackage);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendImage(String address) {
        try {
            FileInputStream fis = new FileInputStream(address);

            // Read the image data into a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] imageData = baos.toByteArray();
            fis.close();

            // Create a ServerPackage with the image data
            ServerPackage serverPackage = new ServerPackage("image", imageData); // Use the "image" identifier

            // Send the ServerPackage to the client
            objectOutputStream.writeObject(serverPackage);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
