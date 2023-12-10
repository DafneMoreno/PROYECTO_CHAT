package com.example.chatsito;

import com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS.INSERT;
import com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS.SELECT_Usuarios;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Registro    extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Registro de Usuario");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Etiquetas y campos de texto
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

        // Botón de registro
        Button registrarButton = new Button("Registrar");
        GridPane.setConstraints(registrarButton, 1, 2);

        // Acción del botón de registro
        registrarButton.setOnAction(e -> {

            int resultado = validarRegistro(usuarioInput.getText(), contrasenaInput.getText());
            switch (resultado) {
                case 1:
                    System.out.println("El usuario ya existe o la contraseña es demasiado corta");
                    break;
                case 0:
                    int nuevoId = obtenerProximoId();
                    String id= String.valueOf(nuevoId);
                    String directorio= ".\\chatsito\\src\\main\\java\\com\\example\\chatsito\\BD_CHAT";
                    String sentencia = "INSERT INTO Usuarios (id,username,password) VALUES ("+id+","+usuarioInput.getText()+","+contrasenaInput.getText()+")";
                    System.out.println(sentencia);
                    INSERT.main(directorio, sentencia);
                    System.out.println("Registro exitoso");
                    InicioSesion InicioSesion = new InicioSesion();
                    Stage stage = new Stage();
                    InicioSesion.start(stage);
                    primaryStage.close();                    break;
                case -1:
                    System.out.println("La tabla no tiene las columnas 'username' o 'password'.");
                    // Realiza las acciones correspondientes a un error en la tabla
                    break;
            }
        });

        grid.getChildren().addAll(usuarioLabel, usuarioInput, contrasenaLabel, contrasenaInput, registrarButton);

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    // Método para validar el registro (puedes implementar tu lógica de validación aquí)
    private int validarRegistro(String usuario, String contrasena) {
        String directorio= ".\\chatsito\\src\\main\\java\\com\\example\\chatsito\\BD_CHAT";
        String sentencia= "SELECT * FROM Usuarios WHERE usuario= "+usuario+" AND password= "+ contrasena;
        System.out.println(sentencia);
        return SELECT_Usuarios.main(directorio, sentencia, usuario, contrasena);
    }

    private int obtenerProximoId() {
        int maxId = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(".\\chatsito\\src\\main\\java\\com\\example\\chatsito\\BD_CHAT\\Usuarios.csv"))) {
            String line;

            // Ignorar la primera línea (encabezados)
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] campos = line.split(",");
                if (campos.length > 0) {
                    int id = Integer.parseInt(campos[0].trim());

                    if (id > maxId) {
                        maxId = id;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return maxId + 1;
    }


}