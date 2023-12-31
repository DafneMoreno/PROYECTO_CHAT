package com.example.chatsito.Controlador;

import com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS.SELECT_ID;
import com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS.SELECT_Usuarios;
import com.example.chatsito.ChatClientFX;
import com.example.chatsito.ChatServerFX;
import com.example.chatsito.Registro;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

public class ControllerInicioSesion extends Application {
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


    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/vista/iniciosesion1.fxml"));
        // Configurar la escena
        Scene scene = new Scene(root, 700, 500);

        // Configurar el escenario
        primaryStage.setTitle("Inicio de Sesión");
        primaryStage.setScene(scene);
        // Show the stage
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }

}
