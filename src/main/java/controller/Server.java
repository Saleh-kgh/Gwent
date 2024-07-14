package controller;

import Model.DataSaver;
import Model.InGameObjects.Cards.CardFactory;
import Model.User.User;

import java.io.IOException;
import java.net.CacheRequest;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        CardFactory.createAllCards();
        DataSaver.loadUsers();
        DataSaver.loadFriends(User.getAllUsers());
        while (!serverSocket.isClosed()) {

            try {

                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected");
                ClientHandler clientHandler = new ClientHandler(socket, this);

                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            Server server = new Server(serverSocket);
            server.startServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
