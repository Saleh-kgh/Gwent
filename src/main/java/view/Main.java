package view;

import controller.Client;

import java.io.IOException;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            Client client = new Client(socket);
            Thread thread = new Thread(client);
            thread.start();

            AppView.main(args);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}