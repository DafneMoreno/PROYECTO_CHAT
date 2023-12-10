package com.example.chatsito;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatServerFX extends Application {
    private final List<PrintWriter> clients = new ArrayList<>();
    private TextArea chatArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        chatArea = new TextArea();
        chatArea.setEditable(false);

        Scene scene = new Scene(chatArea, 400, 300);
        primaryStage.setTitle("Chat Server");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> closeServer());
        primaryStage.show();

        startServer();
    }

    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);

            new Thread(() -> {
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                        clients.add(writer);

                        new Thread(() -> handleClient(clientSocket)).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            Scanner scanner = new Scanner(clientSocket.getInputStream());

            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                broadcastMessage(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcastMessage(String message) {
        Platform.runLater(() -> chatArea.appendText(message + "\n"));

        for (PrintWriter client : clients) {
            client.println(message);
        }
    }

    private void closeServer() {
        Platform.exit();
        System.exit(0);
    }
}
