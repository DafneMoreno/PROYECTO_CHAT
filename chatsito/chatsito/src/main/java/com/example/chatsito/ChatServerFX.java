package com.example.chatsito;

import com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS.SELECT_Usuarios;
import javafx.application.Application;
import javafx.application.Platform;
import com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS.INSERT;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatServerFX extends Application {
    private String Directorio="./chatsito/src/main/java/com/example/chatsito/BD_CHAT";
    private Registro registro;

    private String userID;
    private final List<PrintWriter> clients = new ArrayList<>();
    private TextArea chatArea;

    public static void main(String[] args) {
        launch(args);
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public void setRegistro(Registro registro) {
        this.registro = registro;
    }
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
        String usuario = "";
        String contra = "";
        try {
            Scanner scanner = new Scanner(clientSocket.getInputStream());

            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();


                if (message.contains("/registroUser")) {
                    usuario = message.replaceAll("^/registroUser\\s+", "");
                }
                if (message.contains("/registroContra")) {
                    contra = message.replaceAll("^/registroContra\\s+", "");
                }


                if (!usuario.isEmpty() && !contra.isEmpty()) {
                    int resultadoValidacion = validarRegistro(usuario, contra);

                    switch (resultadoValidacion) {
                        case 1:
                            // Usuario ya existe o la contraseña es demasiado corta
                            enviarRespuestaAlCliente(clientSocket, "1");
                            break;
                        case 0:
                            enviarRespuestaAlCliente(clientSocket, "0");
                            System.out.println(usuario + contra);
                            int nuevoId = obtenerProximoId();
                            String id = String.valueOf(nuevoId);
                            String sentencia = "INSERT INTO Usuarios (id, username, password, fotoperfil) VALUES (" + id + "," + usuario + "," + contra + ",img.png)";
                            System.out.println(sentencia);
                            INSERT.main(Directorio, sentencia);
                            break;
                        case -1:
                            enviarRespuestaAlCliente(clientSocket, "-1");
                            break;
                    }


                }
                if(!message.contains("/clean")){
                    broadcastMessage(message);
                    try {
                        File file = new File("./chatsito/src/main/java/com/example/chatsito/BD_CHAT/Mensajes.csv");
                        FileWriter writer = new FileWriter(file, true);
                        if(!message.contains("/user")){
                            writer.write(message + "\n");
                        }
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        ArrayList<String> lines = readCSV("./chatsito/src/main/java/com/example/chatsito/BD_CHAT/Mensajes.csv");
                        for (String line : lines) {
                            broadcastMessage(line);
                        }
                    } catch (FileNotFoundException e) {
                        System.err.println("Archivo no encontrado: " + e.getMessage());
                    }

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void enviarRespuestaAlCliente(Socket clientSocket, String respuesta) {
        try {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println(respuesta);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
    private static ArrayList<String> readCSV(String filePath) throws FileNotFoundException {
        ArrayList<String> lines = new ArrayList<>();

        Scanner scanner = new Scanner(new File(filePath));

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lines.add(line);
        }
        scanner.close();
        return lines;
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