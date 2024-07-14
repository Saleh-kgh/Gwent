package Model;

import Model.Game.GameObject;
import Model.Game.GameHistory;
import Model.InGameObjects.Cards.Card;
import Model.InGameObjects.Cards.CardFactory;
import Model.InGameObjects.Cards.LeaderCard;
import Model.User.UserCardInventory;
import Model.User.UserGameInfo;
import Model.User.User;
import Enum.CardNames;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DataSaver {
    private static final String USER_DATA_FILE = "userData.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void saveUsers(ArrayList<User> users) {
        JsonArray userArray = new JsonArray();
        for (User user : users) {
            JsonObject userJson = new JsonObject();
            userJson.addProperty("username", user.getUsername());
            userJson.addProperty("password", user.getPassword());
            userJson.addProperty("nickname", user.getNickname());
            userJson.addProperty("email", user.getEmail());

            userJson.addProperty("score", user.getGameInfo().getScore());
            userJson.addProperty("wins", user.getGameInfo().getVictoryCount());
            userJson.addProperty("defeats", user.getGameInfo().getDefeatCount());
            userJson.addProperty("draws", user.getGameInfo().getDrawCount());
            // here are friendReqs and gameReqs
            JsonArray FriendReqArray = new JsonArray();
            for (FriendRequest req : user.getFriendRequests()) {
                FriendReqArray.add(req.toString());
            }
            userJson.add("friendReqs", FriendReqArray);

            JsonArray GameReqArray = new JsonArray();
            for (String req : user.getGameRequestsLog()) {
                GameReqArray.add(req);
            }
            userJson.add("gameReqs", GameReqArray);
            //


            /////northernRealms done
            userJson.addProperty("northernRealms leader",
                    user.getCardInventory().getNorthernRealms().getCurrentLeader().getCardName().getCardNameString());
            JsonArray northernRealms = new JsonArray();
            for (Card card : user.getCardInventory().getNorthernRealms().getDeck()) {
                northernRealms.add(card.getCardName().getCardNameString());
            }
            userJson.add("northernRealms", northernRealms);
            ///////nilfgaardEmpire done
            userJson.addProperty("nilfgaardEmpire leader",
                    user.getCardInventory().getNilfgaardEmpire().getCurrentLeader().getCardName().getCardNameString());
            JsonArray nilfgaardEmpire = new JsonArray();
            for (Card card : user.getCardInventory().getNilfgaardEmpire().getDeck()) {
                nilfgaardEmpire.add(card.getCardName().getCardNameString());
            }
            userJson.add("nilfgaardEmpire", nilfgaardEmpire);
            ////////monsters done
            userJson.addProperty("monsters leader",
                    user.getCardInventory().getMonsters().getCurrentLeader().getCardName().getCardNameString());
            JsonArray monsters = new JsonArray();
            for (Card card : user.getCardInventory().getMonsters().getDeck()) {
                monsters.add(card.getCardName().getCardNameString());
            }
            userJson.add("monsters", monsters);
            /////////scoiaTael done
            userJson.addProperty("scoiaTael leader",
                    user.getCardInventory().getScoiaTael().getCurrentLeader().getCardName().getCardNameString());
            JsonArray scoiaTael = new JsonArray();
            for (Card card : user.getCardInventory().getScoiaTael().getDeck()) {
                scoiaTael.add(card.getCardName().getCardNameString());
            }
            userJson.add("scoiaTael", scoiaTael);
            ///////skellige
            userJson.addProperty("skellige leader",
                    user.getCardInventory().getSkellige().getCurrentLeader().getCardName().getCardNameString());
            JsonArray skellige = new JsonArray();
            for (Card card : user.getCardInventory().getSkellige().getDeck()) {
                skellige.add(card.getCardName().getCardNameString());
            }
            userJson.add("skellige", skellige);
            //////(^_^)
            JsonArray friendsArray = new JsonArray();
            for (String question : user.getFriendsNames()) {
                friendsArray.add(question);
            }
            userJson.add("friendsArray", friendsArray);

            JsonArray passwordQuestionsArray = new JsonArray();
            for (String question : user.getPasswordQuestions()) {
                passwordQuestionsArray.add(question);
            }
            userJson.add("passwordQuestions", passwordQuestionsArray);

            JsonArray passwordAnswersArray = new JsonArray();
            for (String answer : user.getPasswordAnswers()) {
                passwordAnswersArray.add(answer);
            }
            userJson.add("passwordAnswers", passwordAnswersArray);

            userArray.add(userJson);
        }

        try (FileWriter fileWriter = new FileWriter(USER_DATA_FILE)) {
            GSON.toJson(userArray, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        try (FileReader fileReader = new FileReader(USER_DATA_FILE)) {
            JsonArray userArray = GSON.fromJson(fileReader, JsonArray.class);
            if (userArray == null) {
                return new ArrayList<>();
            }
            for (int i = 0; i < userArray.size(); i++) {
                JsonObject userJson = userArray.get(i).getAsJsonObject();
                String username = userJson.get("username").getAsString();
                String password = userJson.get("password").getAsString();
                String nickname = userJson.get("nickname").getAsString();
                String email = userJson.get("email").getAsString();

                String score = userJson.get("score").getAsString();
                String wins = userJson.get("wins").getAsString();
                String defeats = userJson.get("defeats").getAsString();
                String draws = userJson.get("draws").getAsString();

                // here are friendReqs and gameReqs
                ArrayList<String> friendLog = new ArrayList<>();
                JsonArray FriendReqArray = userJson.getAsJsonArray("friendReqs");
                for (int j = 0; j < FriendReqArray.size(); j++) {
                    friendLog.add(FriendReqArray.get(j).getAsString());
                }

                ArrayList<String> gameReqs = new ArrayList<>();
                JsonArray gameReqsArray = userJson.getAsJsonArray("gameReqs");
                for (int j = 0; j < gameReqsArray.size(); j++) {
                    gameReqs.add(gameReqsArray.get(j).getAsString());
                }
                //


                CardNames cardNames = CardNames.ARACHAS_1;
                //northernRealms
                Card northernRealmsLeader = CardFactory.getCopiedCardByName(cardNames.getCardNameFromString(
                        userJson.get("northernRealms leader").getAsString()));
                JsonArray northernRealms = userJson.getAsJsonArray("northernRealms");
                ArrayList<Card> northernRealmsCards = new ArrayList<>();
                for (int j = 0; j < northernRealms.size(); j++) {
                    northernRealmsCards.add(
                            CardFactory.getCopiedCardByName(cardNames.getCardNameFromString(northernRealms.get(j).getAsString())));
                }
                ////nilfgaardEmpire
                Card nilfgaardEmpireLeader = CardFactory.getCopiedCardByName(cardNames.getCardNameFromString(
                        userJson.get("nilfgaardEmpire leader").getAsString()));
                JsonArray nilfgaardEmpire = userJson.getAsJsonArray("nilfgaardEmpire");
                ArrayList<Card> nilfgaardEmpireCards = new ArrayList<>();
                for (int j = 0; j < nilfgaardEmpire.size(); j++) {
                    nilfgaardEmpireCards.add(
                            CardFactory.getCopiedCardByName(cardNames.getCardNameFromString(nilfgaardEmpire.get(j).getAsString())));
                }
                ////monsters
                Card monstersLeader = CardFactory.getCopiedCardByName(cardNames.getCardNameFromString(
                        userJson.get("monsters leader").getAsString()));
                JsonArray monsters = userJson.getAsJsonArray("monsters");
                ArrayList<Card> monstersCards = new ArrayList<>();
                for (int j = 0; j < monsters.size(); j++) {
                    monstersCards.add(
                            CardFactory.getCopiedCardByName(cardNames.getCardNameFromString(monsters.get(j).getAsString())));
                }
                ////scoiaTael
                Card scoiaTaelLeader = CardFactory.getCopiedCardByName(cardNames.getCardNameFromString(
                        userJson.get("scoiaTael leader").getAsString()));
                JsonArray scoiaTael = userJson.getAsJsonArray("scoiaTael");
                ArrayList<Card> scoiaTaelCards = new ArrayList<>();
                for (int j = 0; j < scoiaTael.size(); j++) {
                    scoiaTaelCards.add(
                            CardFactory.getCopiedCardByName(cardNames.getCardNameFromString(scoiaTael.get(j).getAsString())));
                }
                /////skellige
                Card skelligeLeader = CardFactory.getCopiedCardByName(cardNames.getCardNameFromString(
                        userJson.get("skellige leader").getAsString()));
                JsonArray skellige = userJson.getAsJsonArray("skellige");
                ArrayList<Card> skelligeCards = new ArrayList<>();
                for (int j = 0; j < skellige.size(); j++) {
                    skelligeCards.add(
                            CardFactory.getCopiedCardByName(cardNames.getCardNameFromString(skellige.get(j).getAsString())));
                }
                //// (^-^)
                JsonArray friendsArray = userJson.getAsJsonArray("friendsArray");
                ArrayList<String> friends = new ArrayList<>();
                for (int j = 0; j < friendsArray.size(); j++) {
                    friends.add(friendsArray.get(j).getAsString());
                }

                JsonArray passwordQuestionsArray = userJson.getAsJsonArray("passwordQuestions");
                ArrayList<String> passwordQuestions = new ArrayList<>();
                for (int j = 0; j < passwordQuestionsArray.size(); j++) {
                    passwordQuestions.add(passwordQuestionsArray.get(j).getAsString());
                }

                JsonArray passwordAnswersArray = userJson.getAsJsonArray("passwordAnswers");
                ArrayList<String> passwordAnswers = new ArrayList<>();
                for (int j = 0; j < passwordAnswersArray.size(); j++) {
                    passwordAnswers.add(passwordAnswersArray.get(j).getAsString());
                }
                User user = new User(username, password, nickname, email, true);
                user.setFriendsNames(friends);
                user.setPasswordQuestions(passwordQuestions);
                user.setPasswordAnswers(passwordAnswers);
                user.getFriendRequestsLog().addAll(friendLog);
                user.getGameRequestsLog().addAll(gameReqs);
                //NorthernRealms
                user.getCardInventory().getNorthernRealms().setCurrentLeader((LeaderCard) northernRealmsLeader);
                user.getCardInventory().getNorthernRealms().getDeck().addAll(northernRealmsCards);

                for (int j = 0; j < user.getCardInventory().getNorthernRealms().getDeck().size(); j++) {
                    for (int k = 0; k < user.getCardInventory().getNorthernRealms().getStorage().size(); k++) {
                        if (user.getCardInventory().getNorthernRealms().getDeck().get(j).getCardName() == user.getCardInventory().getNorthernRealms().getStorage().get(k).getCardName()) {
                            user.getCardInventory().getNorthernRealms().getStorage().remove(k);
                            break;
                        }
                    }
                }
                //NilfgaardEmpire
                user.getCardInventory().getNilfgaardEmpire().setCurrentLeader((LeaderCard) nilfgaardEmpireLeader);
                user.getCardInventory().getNilfgaardEmpire().getDeck().addAll(nilfgaardEmpireCards);
                for (int j = 0; j < user.getCardInventory().getNilfgaardEmpire().getDeck().size(); j++) {
                    for (int k = 0; k < user.getCardInventory().getNilfgaardEmpire().getStorage().size(); k++) {
                        if (user.getCardInventory().getNilfgaardEmpire().getDeck().get(j).getCardName() == user.getCardInventory().getNilfgaardEmpire().getStorage().get(k).getCardName()) {
                            user.getCardInventory().getNilfgaardEmpire().getStorage().remove(k);
                            break;
                        }
                    }
                }

                //monsters
                user.getCardInventory().getMonsters().setCurrentLeader((LeaderCard) monstersLeader);
                user.getCardInventory().getMonsters().getDeck().addAll(monstersCards);
                for (int j = 0; j < user.getCardInventory().getMonsters().getDeck().size(); j++) {
                    for (int k = 0; k < user.getCardInventory().getMonsters().getStorage().size(); k++) {
                        if (user.getCardInventory().getMonsters().getDeck().get(j).getCardName() == user.getCardInventory().getMonsters().getStorage().get(k).getCardName()) {
                            user.getCardInventory().getMonsters().getStorage().remove(k);
                            break;
                        }
                    }
                }
                //scoiaTael
                user.getCardInventory().getScoiaTael().setCurrentLeader((LeaderCard) scoiaTaelLeader);
                user.getCardInventory().getScoiaTael().getDeck().addAll(scoiaTaelCards);
                for (int j = 0; j < user.getCardInventory().getScoiaTael().getDeck().size(); j++) {
                    for (int k = 0; k < user.getCardInventory().getScoiaTael().getStorage().size(); k++) {
                        if (user.getCardInventory().getScoiaTael().getDeck().get(j).getCardName() == user.getCardInventory().getScoiaTael().getStorage().get(k).getCardName()) {
                            user.getCardInventory().getScoiaTael().getStorage().remove(k);
                            break;
                        }
                    }
                }
                //skellige
                user.getCardInventory().getSkellige().setCurrentLeader((LeaderCard) skelligeLeader);
                user.getCardInventory().getSkellige().getDeck().addAll(skelligeCards);
                for (int j = 0; j < user.getCardInventory().getSkellige().getDeck().size(); j++) {
                    for (int k = 0; k < user.getCardInventory().getSkellige().getStorage().size(); k++) {
                        if (user.getCardInventory().getSkellige().getDeck().get(j).getCardName() == user.getCardInventory().getSkellige().getStorage().get(k).getCardName()) {
                            user.getCardInventory().getSkellige().getStorage().remove(k);
                            break;
                        }
                    }
                }
                user.getGameInfo().addScore(Integer.parseInt(score));
                user.getGameInfo().setDefeatCount(Integer.parseInt(defeats));
                user.getGameInfo().setVictoryCount(Integer.parseInt(wins));
                user.getGameInfo().setDrawCount(Integer.parseInt(draws));
            }
        } catch (IOException e) {
            System.out.println("GSON gand zadddddddd");
        }
        return users;
    }

    public static void loadFriends(ArrayList<User> users) {
        for (User user : users)
            for (String name : user.getFriendsNames())
                if (!user.isYourFriend(User.getUserByUsername(name)))
                    user.getFriends().add(User.getUserByUsername(name));
        for (User user : users)
            for (String friendReq : user.getFriendRequestsLog())
                user.getFriendRequests().add(FriendRequest.fromString(friendReq));

    }
}