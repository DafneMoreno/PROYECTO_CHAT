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

public class InicioSesion extends Application {

    private Stage primaryStage; // Agregar una referencia al Stage de la ventana de inicio de sesión

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; // Inicializar la referencia al Stage
        primaryStage.setTitle("Inicio de Sesión");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Etiquetas y campos de texto
        Label usuarioLabel = new Label("Usuario:");
        GridPane.setConstraints(usuarioLabel, 0, 0);

        TextField usuarioInput = new TextField();
        usuarioInput.setPromptText("Ingrese su usuario");
        GridPane.setConstraints(usuarioInput, 1, 0);

        Label contrasenaLabel = new Label("Contraseña:");
        GridPane.setConstraints(contrasenaLabel, 0, 1);

        PasswordField contrasenaInput = new PasswordField();
        contrasenaInput.setPromptText("Ingrese su contraseña");
        GridPane.setConstraints(contrasenaInput, 1, 1);

        // Botón de inicio de sesión
        Button iniciarSesionButton = new Button("Iniciar Sesión");
        GridPane.setConstraints(iniciarSesionButton, 1, 2);

        // Acción del botón de inicio de sesión
        // Acción del botón de inicio de sesión
        iniciarSesionButton.setOnAction(e -> {
            int resultado = validarCredenciales(usuarioInput.getText(), contrasenaInput.getText());
            switch (resultado) {
                case 1:
                    System.out.println("Inicio de sesión exitoso");
                    // Realiza las acciones correspondientes al inicio de sesión exitoso
                    break;
                case 0:
                    System.out.println("Credenciales incorrectas");
                    // Realiza las acciones correspondientes al inicio de sesión incorrecto
                    break;
                case -1:
                    System.out.println("La tabla no tiene las columnas 'username' o 'password'.");
                    // Realiza las acciones correspondientes a un error en la tabla
                    break;
            }
        });


        // Botón para registrarse
        Button registrarseButton = new Button("Registrarse");
        GridPane.setConstraints(registrarseButton, 2, 2);

        // Acción del botón para registrarse
        registrarseButton.setOnAction(e -> {
            abrirVentanaRegistro();
        });

        grid.getChildren().addAll(usuarioLabel, usuarioInput, contrasenaLabel, contrasenaInput, iniciarSesionButton, registrarseButton);

        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    // Método para validar las credenciales (puedes implementar tu lógica de autenticación aquí)
    private int validarCredenciales(String usuario, String contrasena) {
        String directorio= ".\\chatsito\\src\\main\\java\\com\\example\\chatsito\\BD_CHAT";
        String sentencia= "SELECT * FROM Usuarios WHERE usuario= "+usuario+" AND password= "+ contrasena;
        return SELECT_Usuarios.main(directorio, sentencia, usuario, contrasena);
    }

    // Método para abrir la ventana de registro
    private void abrirVentanaRegistro() {
        Registro registro = new Registro();
        Stage stage = new Stage();
        registro.start(stage);
        primaryStage.close(); // Cerrar la ventana de inicio de sesión al abrir la ventana de registro
    }
}
