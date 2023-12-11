package com.example.chatsito;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS.UPDATE2;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClientFX extends Application {
    private String userID;
    private String Directorio;
    private PrintWriter serverOutput;
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        VBox chatList = new VBox();
        chatList.setMinWidth(150);

        ListView<String> chatListView = new ListView<>();
        chatListView.getItems().addAll("Chat 1", "Chat 2", "Chat 3"); // Puedes cargar la lista desde el servidor
        chatListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Lógica para cargar la conversación correspondiente al chat seleccionado
            // Puedes enviar un mensaje al servidor para obtener el historial del chat, por ejemplo
        });

        chatList.getChildren().addAll(new Label("Chats"), chatListView);

        VBox chatArea = new VBox();
        chatArea.setAlignment(Pos.BOTTOM_LEFT);

        TextField messageInput = new TextField();
        messageInput.setPromptText("Type your messagee...");

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage(messageInput.getText(), chatArea));
        Button FOTOButton = new Button("Foto");
        FOTOButton.setOnAction(e -> cambiarFoto(userID, chatArea));

        HBox messageBox = new HBox(messageInput, sendButton, FOTOButton);
        messageBox.setAlignment(Pos.CENTER);

        chatArea.getChildren().addAll(new Label("Chat with ---- tu id es "+userID), new ScrollPane(), messageBox);
        chatArea.setMinWidth(250);


        SplitPane splitPane = new SplitPane(chatList, chatArea);
        splitPane.setDividerPositions(0.2); // Ajusta la posición del divisor

        root.setCenter(splitPane);

        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("WhatsApp-like Chat Client");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> closeClient());
        primaryStage.show();

        connectToServer(chatArea);
    }

    private void connectToServer(VBox chatArea) {
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


    private void sendMessage(String message, VBox chatArea) {
        serverOutput.println(message);
        updateChatArea(chatArea, "You: " + message);
    }
   /* private void cambiarFoto(String id, VBox chatArea) {
        String sentencia ="UPDATE Usuarios SET username = PRUEBA111 WHERE id ="+id ;
        System.out.println(sentencia);
        System.out.println(Directorio);
        UPDATE2.main(Directorio, sentencia);
    }*/

    private void updateChatArea(VBox chatArea, String message) {
        Platform.runLater(() -> {
            Label messageLabel = new Label(message);
            chatArea.getChildren().add(messageLabel);
        });
    }
    private void cambiarFoto(String id, VBox chatArea) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Foto");

        // Filtro para mostrar solo archivos de imagen (puedes personalizar esto según tus necesidades)
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Ruta de la carpeta donde deseas guardar las fotos
            String destino = ".\\chatsito\\src\\main\\java\\com\\example\\chatsito\\BD_CHAT\\FOTOS\\";

            // Copiar el archivo seleccionado a la carpeta de destino
            try {
                java.nio.file.Files.copy(selectedFile.toPath(), new java.io.File(destino + selectedFile.getName()).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Foto copiada con éxito a: " + destino + selectedFile.getName());


                ImageView imageView = new ImageView(new Image(((File) selectedFile).toURI().toString()));
                imageView.setFitHeight(50); // Ajusta la altura según tu preferencia
                imageView.setFitWidth(50);  // Ajusta el ancho según tu preferencia
                chatArea.getChildren().add(imageView);
                String sentencia ="UPDATE Usuarios SET fotoperfil = .\\chatsito\\src\\main\\java\\com\\example\\chatsito\\BD_CHAT\\FOTOS\\" + selectedFile.getName()+" WHERE id ="+id ;
                System.out.println(sentencia);
                UPDATE2.main(Directorio, sentencia);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void closeClient() {
        System.exit(0);
    }

    public void setDirectorio(String directorio) {
        Directorio = directorio;
    }
}
