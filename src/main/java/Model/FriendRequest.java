package Model;

import Model.User.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class FriendRequest implements Serializable {
    private User sender;
    private User receiver;
    private String answer = "w";
    private static ArrayList<FriendRequest> requests = new ArrayList<>();

    public FriendRequest(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        requests.add(this);
    }

    @Override
    public String toString() {
        String situation;
        if (Objects.equals(answer, "w"))
            situation = "waiting";
        else if (Objects.equals(answer, "a"))
            situation = "accepted";
        else
            situation = "rejected";
        return sender.getUsername() + " />to>/ " + receiver.getUsername() + " situation:" + situation;
    }

    public static FriendRequest fromString(String req) {
        String[] parts = req.split(" />to>/ ");

        String senderUsername = parts[0];
        String receiverUsername = parts[1].split(" situation:")[0];
        String situation = parts[1].split(" situation:")[1];

        User sender = User.getUserByUsername(senderUsername);
        User receiver = User.getUserByUsername(receiverUsername);

        FriendRequest friendRequest = new FriendRequest(sender, receiver);

        switch (situation) {
            case "waiting":
                friendRequest.answer = "w";
                break;
            case "accepted":
                friendRequest.answer = "a";
                break;
            case "rejected":
                friendRequest.answer = "r";
                break;
        }

        return friendRequest;
    }

    public void accept() {
        if (answer.equals("w")) {
            answer = "a";
            sender.getFriends().add(receiver);
            receiver.getFriends().add(sender);
        }
    }

    public void reject() {
        if (answer.equals("w")) {
            answer = "r";
        }
    }

    public static FriendRequest getRequest(String sender, String receiver) {
        for (FriendRequest f : FriendRequest.getRequests()) {
            if (f.sender.getUsername().equals(sender) && f.receiver.getUsername().equals(receiver) && f.getAnswer().equals("w"))
                return f;
        }
        return null;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public static ArrayList<FriendRequest> getRequests() {
        return requests;
    }

    public String getAnswer() {
        return answer;
    }
}
