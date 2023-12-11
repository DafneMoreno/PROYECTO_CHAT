package com.example.chatsito;

import com.example.chatsito.Controlador.ControllerInicioSesion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS.INSERT;
import com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS.SELECT_ID;
import com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS.SELECT_Usuarios;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.io.IOException;
import java.io.PrintWriter;
public class InicioSesion extends Application {
    @FXML
    private Button iniciarsesionbtn;
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    private String Directorio = ".\\chatsito\\src\\main\\java\\com\\example\\chatsito\\BD_CHAT";
    private PrintWriter serverOutput;

public void setDirectorio(String Directorio) {
        this.Directorio = Directorio;
    }
    private Stage primaryStage; // Agregar una referencia al Stage de la ventana de inicio de sesión

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/iniciosesion1.fxml"));
            // Configurar la escena
            Scene scene = new Scene(root, 700, 500);

            // Configurar el escenario
            primaryStage.setTitle("Inicio de Sesión");
            primaryStage.setScene(scene);
            // Show the stage
            primaryStage.show();
            // Resto del código...


        } catch (IOException e) {
            e.printStackTrace();
        }

       /* this.primaryStage = primaryStage; // Inicializar la referencia al Stage
        primaryStage.setTitle("Inicio de Sesión");
        PrintStream originalOut = System.out;*/

        // Crear un nuevo flujo de salida
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        /*GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
*/
        // Etiquetas y campos de texto
        Label usuarioLabel = new Label("Usuario:");
        GridPane.setConstraints(usuarioLabel, 0, 0);

        TextField username = new TextField();
        username.setPromptText("Ingrese su usuario");
        GridPane.setConstraints(username, 1, 0);

        Label contrasenaLabel = new Label("Contraseña:");
        GridPane.setConstraints(contrasenaLabel, 0, 1);

        PasswordField password = new PasswordField();
        password.setPromptText("Ingrese su contraseña");
        GridPane.setConstraints(password, 1, 1);

        // Botón de inicio de sesión
        Button iniciarsesionbtn = new Button("Iniciar Sesión");
        GridPane.setConstraints(iniciarsesionbtn, 1, 2);

        // Acción del botón de inicio de sesión
        // Acción del botón de inicio de sesión


        iniciarsesionbtn.setOnAction(e ->
        });


        // Botón para registrarse
        Button registrarseButton = new Button("Registrarse");
        GridPane.setConstraints(registrarseButton, 2, 2);

        //grid.getChildren().addAll(usuarioLabel, usuarioInput, contrasenaLabel, contrasenaInput, iniciarSesionButton, registrarseButton);

        //Scene scene = new Scene(grid, 400, 200);
       // primaryStage.setScene(scene);

        primaryStage.show();
    }

    // Método para validar las credenciales (puedes implementar tu lógica de autenticación aquí)
    private int validarCredenciales(String usuario, String contrasena) {
       // String directorio= ".\\chatsito\\src\\main\\java\\com\\example\\chatsito\\BD_CHAT";
        String sentencia= "SELECT * FROM Usuarios WHERE usuario= "+usuario+" AND password= "+ contrasena;
        return SELECT_Usuarios.main(Directorio, sentencia, usuario, contrasena);
    }

    // Método para abrir la ventana de registro
    private void abrirVentanaRegistro() {
        Registro registro = new Registro();
        Stage stage = new Stage();
        registro.start(stage);
        primaryStage.close(); // Cerrar la ventana de inicio de sesión al abrir la ventana de registro
    }
    private void abrirVentanaCliente(String ID) {
        ChatClientFX  cliente = new ChatClientFX();
        Stage stage = new Stage();
        cliente.setUserID(ID);
        cliente.setDirectorio(Directorio);
        cliente.start(stage);
        primaryStage.close(); // Cerrar la ventana de inicio de sesión al abrir la ventana de registro
    }
    private void abrirVentanaServer(String ID) {
        ChatServerFX  server = new ChatServerFX();
        Stage stage = new Stage();
        server.setUserID(ID);
        server.start(stage);
        primaryStage.close(); // Cerrar la ventana de inicio de sesión al abrir la ventana de registro
    }
}
