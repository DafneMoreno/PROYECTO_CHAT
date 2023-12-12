package com.example.chatsito;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Registro extends Application {
    private String Directorio = ".\\chatsito\\src\\main\\java\\com\\example\\chatsito\\BD_CHAT";
    private PrintWriter serverOutput;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Registro de Usuario");

        GridPane grid = new GridPane();
        grid.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label usuarioLabel = new Label("Usuario:");
        GridPane.setConstraints(usuarioLabel, 0, 0);

        TextField usuarioInput = new TextField();
        usuarioInput.setPromptText("Ingrese un nuevo usuario");
        GridPane.setConstraints(usuarioInput, 1, 0);

        Label contrasenaLabel = new Label("Contraseña:");
        GridPane.setConstraints(contrasenaLabel, 0, 1);

        PasswordField contrasenaInput = new PasswordField();
        contrasenaInput.setPromptText("Ingrese una nueva contraseña");
        GridPane.setConstraints(contrasenaInput, 1, 1);

        Button registrarButton = new Button("Registrar");
        GridPane.setConstraints(registrarButton, 1, 2);

        registrarButton.setOnAction(e -> {
            if (!validarContrasena(contrasenaInput.getText())) {
                mostrarAlerta("Advertencia", "La contraseña no cumple con los requisitos.");
                return;
            }

            int resultado = validarRegistro(usuarioInput.getText(), contrasenaInput.getText());
            if (resultado == 0) {
                mostrarAlerta("Registro Exitoso", "¡Registro exitoso! Ahora puedes iniciar sesión con tu nuevo usuario.");
                // Aquí puedes redirigir al usuario a la pantalla de inicio de sesión si es necesario
            } else {
                mostrarAlerta("Error en el Registro", "El usuario ya existe o la contraseña es demasiado corta. Por favor, elige otro usuario o utiliza una contraseña más larga.");
            }
        });

        grid.getChildren().addAll(usuarioLabel, usuarioInput, contrasenaLabel, contrasenaInput, registrarButton);

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private int validarRegistro(String usuario, String contrasena) {
        try (Socket socket = new Socket("192.168.100.33", 12345)) {
            serverOutput = new PrintWriter(socket.getOutputStream(), true);
            serverOutput.println("/registroUser " + usuario);
            serverOutput.println("/registroContra " + contrasena);

            // Aquí puedes leer la respuesta del servidor
            Scanner scanner = new Scanner(socket.getInputStream());
            if (scanner.hasNextLine()) {
                String respuesta = scanner.nextLine();
                System.out.println("Respuesta del servidor: " + respuesta);
                return Integer.parseInt(respuesta);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return -1; // Valor de error por defecto
    }

    private boolean validarContrasena(String contrasena) {
        // Implementa tu lógica de validación de contraseña aquí
        return contrasena.length() >= 6;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
