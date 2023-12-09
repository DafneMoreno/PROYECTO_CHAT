package com.example.chatsito.PRUEBA1;

import com.example.chatsito.CServidorArchivo;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ChatInterface extends Application {

    private TextArea chatArea;
    private TextArea messageArea;
    private Button sendFileButton;
    private Button sendMessageButton;

    private CServidorArchivo servidorArchivo;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chat con envío de archivos");

        chatArea = new TextArea();
        chatArea.setEditable(false);

        messageArea = new TextArea();
        messageArea.setWrapText(true);

        sendFileButton = new Button("Enviar Archivo");
        sendFileButton.setOnAction(e -> enviarArchivo());

        sendMessageButton = new Button("Enviar Mensaje");
        sendMessageButton.setOnAction(e -> enviarMensaje());

        HBox hbox = new HBox(sendFileButton, sendMessageButton);
        hbox.setSpacing(10);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(chatArea, messageArea, hbox);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));

        Scene scene = new Scene(vBox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para enviar archivos
    private void enviarArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo a enviar");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            servidorArchivo = new CServidorArchivo(); // Creamos una instancia del servidor
            Thread enviarThread = new Thread(() -> {
                try {
                    servidorArchivo.enviarArchivo(selectedFile);
                    // Mensaje en el área de chat
                    chatArea.appendText("Archivo enviado: " + selectedFile.getName() + "\n");
                } catch (IOException e) {
                    chatArea.appendText("Error al enviar el archivo: " + e.getMessage() + "\n");
                }
            });
            enviarThread.start(); // Iniciamos un hilo para enviar el archivo
        }
    }

    // Método para enviar mensajes
    private void enviarMensaje() {
        String mensaje = messageArea.getText();
        if (!mensaje.isEmpty()) {
            // Envía el mensaje al chat (podrías imprimirlo en el área de chat o enviarlo por red, dependiendo de tu lógica)
            chatArea.appendText("Yo: " + mensaje + "\n");
            // Limpia el área de texto del mensaje después de enviarlo
            messageArea.clear();
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
