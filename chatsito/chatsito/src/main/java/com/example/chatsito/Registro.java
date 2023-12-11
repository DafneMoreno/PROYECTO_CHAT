package com.example.chatsito;

import javafx.scene.control.Alert;
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
    private String Directorio=".\\chatsito\\src\\main\\java\\com\\example\\chatsito\\BD_CHAT";
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
            if (!validarContrasena(contrasenaInput.getText())) {
                // Muestra una ventana emergente indicando que la contraseña no cumple con los requisitos
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setTitle("Advertencia");
                alertWarning.setHeaderText(null);
                alertWarning.setContentText("La contraseña debe tener al menos 6 caracteres y contener tanto letras como números.");
                alertWarning.showAndWait();
                return; // Sale del método si la contraseña no es válida
            }
            int resultado = validarRegistro(usuarioInput.getText(), contrasenaInput.getText());
            switch (resultado) {
                case 1:
                    System.out.println("El usuario ya existe o la contraseña es demasiado corta");
                    Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                    alertWarning.setTitle("Advertencia");
                    alertWarning.setHeaderText(null);
                    alertWarning.setContentText("El usuario ya existe o la contraseña es demasiado corta. Por favor, elige otro usuario o utiliza una contraseña más larga.");
                    alertWarning.showAndWait();
                    break;
                case 0:
                    int nuevoId = obtenerProximoId();
                    String id= String.valueOf(nuevoId);
                //    String directorio= ".\\chatsito\\src\\main\\java\\com\\example\\chatsito\\BD_CHAT";
                    String sentencia = "INSERT INTO Usuarios (id,username,password) VALUES ("+id+","+usuarioInput.getText()+","+contrasenaInput.getText()+")";
                    System.out.println(sentencia);
                    INSERT.main(Directorio, sentencia);
                    System.out.println("Registro exitoso");
                    Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                    alertSuccess.setTitle("Registro Exitoso");
                    alertSuccess.setHeaderText(null);
                    alertSuccess.setContentText("¡Registro exitoso! Ahora puedes iniciar sesión con tu nuevo usuario.");
                    alertSuccess.showAndWait();
                    InicioSesion InicioSesion = new InicioSesion();
                    Stage stage = new Stage();
                    InicioSesion.start(stage);
                    primaryStage.close();
                    break;
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
       // String directorio= ".\\chatsito\\src\\main\\java\\com\\example\\chatsito\\BD_CHAT";
        String sentencia= "SELECT * FROM Usuarios WHERE usuario= "+usuario+" AND password= "+ contrasena;
        System.out.println(sentencia);
        return SELECT_Usuarios.main(Directorio, sentencia, usuario, contrasena);
    }

    private int obtenerProximoId() {
        int maxId = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(Directorio+"\\Usuarios.csv"))) {
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
    private boolean validarContrasena(String contrasena) {
        // Verifica que la contraseña tenga al menos 6 caracteres
        if (contrasena.length() < 6) {
            return false;
        }

        // Verifica que la contraseña contenga tanto letras como números
        boolean contieneLetras = false;
        boolean contieneNumeros = false;

        for (char caracter : contrasena.toCharArray()) {
            if (Character.isLetter(caracter)) {
                contieneLetras = true;
            } else if (Character.isDigit(caracter)) {
                contieneNumeros = true;
            }
        }

        return contieneLetras && contieneNumeros;
    }

    public void setDirectorio(String directorio) {
        Directorio = directorio;
    }
}