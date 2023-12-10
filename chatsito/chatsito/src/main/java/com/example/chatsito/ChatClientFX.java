package com.example.chatsito;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClientFX extends Application {
    private PrintWriter serverOutput;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);

        TextField messageInput = new TextField();
        messageInput.setPromptText("Type your message...");

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage(messageInput.getText(), chatArea));

        VBox root = new VBox(chatArea, messageInput, sendButton);
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("Chat Client");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> closeClient());
        primaryStage.show();

        connectToServer(chatArea);
    }

    private void connectToServer(TextArea chatArea) {
        try {
            Socket socket = new Socket("localhost", 12345);
            serverOutput = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> {
                try {
                    Scanner serverInput = new Scanner(socket.getInputStream());
                    while (serverInput.hasNextLine()) {
                        String message = serverInput.nextLine();
                        updateChatArea(chatArea, message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message, TextArea chatArea) {
        serverOutput.println(message);
        updateChatArea(chatArea, "You: " + message);
    }

    private void updateChatArea(TextArea chatArea, String message) {
        chatArea.appendText(message + "\n");
    }

    private void closeClient() {
        System.exit(0);
    }
}
