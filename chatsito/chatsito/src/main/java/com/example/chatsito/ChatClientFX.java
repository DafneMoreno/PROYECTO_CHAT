    package com.example.chatsito;

    import com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS.SELECT_ID;
    import com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS.UPDATE2;
    import javafx.application.Application;
    import javafx.application.Platform;
    import javafx.event.EventHandler;
    import javafx.geometry.Pos;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.layout.BorderPane;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.StackPane;
    import javafx.scene.layout.VBox;
    import javafx.scene.input.MouseEvent;
    import javafx.stage.FileChooser;
    import javafx.stage.Stage;

    import java.io.File;
    import java.io.IOException;
    import java.io.PrintWriter;
    import java.net.Socket;
    import java.util.Scanner;
    import java.util.regex.Matcher;
    import java.util.regex.Pattern;

    public class ChatClientFX extends Application {
        private String userID;
        private int msgcont = 0;
        private PrintWriter serverOutput;
        private String selected = "";
        private String Directorio;
        private boolean priv = false;
        private String usuario;
        ListView<String> chatListView = new ListView<>();
        public void setUserID(String userID) {
            this.userID = userID;
        }

        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }
        public void setDirectorio(String directorio) {
            Directorio = directorio;
        }
        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage primaryStage) {

            BorderPane root = new BorderPane();

            VBox chatList = new VBox();
            chatList.setMinWidth(150);

            VBox chatArea = new VBox();
            chatArea.setAlignment(Pos.BOTTOM_LEFT);

            TextField messageInput = new TextField();
            messageInput.setPromptText("Type your message...");

            SplitPane splitPane = new SplitPane(chatList, chatArea);
            splitPane.setDividerPositions(0.2); // Ajusta la posición del divisor

            root.setCenter(splitPane);

            Button sendButton = new Button("Send");
            sendButton.setOnAction(e -> sendMessage(messageInput.getText(), chatArea, priv));

            Button FOTOButton = new Button("Foto");
            FOTOButton.setOnAction(e -> cambiarFoto(userID, chatArea, root,splitPane));

            HBox messageBox = new HBox(messageInput, sendButton, FOTOButton);
            messageBox.setAlignment(Pos.CENTER);

            chatListView.getItems().addAll("Server Group"); // Puedes cargar la lista desde el servidor
            chatListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                // Lógica para cargar la conversación correspondiente al chat seleccionado
                // Puedes enviar un mensaje al servidor para obtener el historial del chat, por ejemplo
            });

            chatListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    String selectedItem = chatListView.getSelectionModel().getSelectedItem();
                    if (selectedItem != null && selectedItem.equals("Server Group")) {
                        priv = false;
                    } else if(selectedItem != null) {
                        priv = true;
                        selected = selectedItem;
                    }
                    msgcont = 0;
                    chatArea.getChildren().clear();
                    chatArea.getChildren().addAll(messageBox);
                    serverOutput.println("/clean");
                }
            });

            chatList.getChildren().addAll(new Label("Chats"), chatListView);

            chatArea.getChildren().addAll(messageBox);
            chatArea.setMinWidth(250);

            Scene scene = new Scene(root, 600, 400);

            primaryStage.setTitle("Chat");
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> closeClient());
            primaryStage.show();

            connectToServer(chatArea);
            setupProfilePicture(root);
        }

        private void connectToServer(VBox chatArea) {
            try {
                Socket socket = new Socket("192.168.100.33", 12345);
                serverOutput = new PrintWriter(socket.getOutputStream(), true);


                // Enviar el ID del usuario al servidor
                serverOutput.println("/clean");
                serverOutput.println("/user " + userID + " " + usuario);

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

        private void sendMessage(String message, VBox chatArea, boolean priv) {
            if(priv){
                String[] parts = selected.split("\\s+");
                String numberString = parts[0];
                serverOutput.println("/private" + " " + userID + "-" + numberString + " " + usuario + ": " + message);
            } else {
                serverOutput.println("/mensaje" + " " + userID + " " + usuario + ": " + message);
            }
        }

        private void updateChatArea(VBox chatArea, String message) {
            Platform.runLater(() -> {
                if(message.contains("/mensaje")){
                    String result = message.replaceAll("^/mensaje\\s+", "");
                    Label messageLabel = new Label(result);
                    chatArea.getChildren().add(msgcont, messageLabel);
                    msgcont++;
                }
                String[] parts = selected.split("\\s+");
                String numberString = parts[0];
                if(message.contains("/private " + userID + "-" + numberString) || message.contains("/private " + numberString + "-" + userID)){
                    String patternString = "/private \\d+-\\d+\\s+";
                    Pattern pattern = Pattern.compile(patternString);
                    Matcher matcher = pattern.matcher(message);
                    String result = matcher.replaceFirst("");
                    Label messageLabel = new Label(result.trim());
                    chatArea.getChildren().add(msgcont, messageLabel);
                    msgcont++;
                }
                if(message.contains("/user")){
                    String result = message.replaceAll("^/user\\s+", "");
                    if(!result.contains(userID + " " + usuario)){
                        if(!chatListView.getItems().contains(result)){
                            chatListView.getItems().addAll(result);
                        }
                        serverOutput.println("/user " + userID + " " + usuario);
                    }
                }
            });
        }

        private void closeClient() {
            System.exit(0);
        }

        private void cambiarFoto(String id, VBox chatArea, BorderPane root, SplitPane splitPane) {
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


                    String sentencia ="UPDATE Usuarios SET fotoperfil = "+ selectedFile.getName()+" WHERE id ="+id ;
                    System.out.println(sentencia);
                    UPDATE2.main(Directorio, sentencia);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            setupProfilePicture(root);
        }
        private void setupProfilePicture(BorderPane root) {
            // Crea un ImageView con la imagen predefinida
            String sentencia= "SELECT fotoperfil FROM Usuarios WHERE id="+userID;
            //  System.out.println(sentencia+" SENTENCIA DEL SELECT");
            SELECT_ID selectFOTO = new SELECT_ID(Directorio);
            String linkfoto =  selectFOTO.execute(sentencia);
            // System.out.println("foto de perfil: " + linkfoto);
            // System.out.println("D:\\USUARIO\\Escritorio\\CUATRI 6\\PROGRAMACIÓN ORIENTADA A OBJETOS\\PROYECTO\\PROYECTO_CHAT\\chatsito\\"+linkfoto);

            Image defaultImage = new Image("D:\\USUARIO\\Escritorio\\CUATRI 6\\PROGRAMACIÓN ORIENTADA A OBJETOS\\PROYECTO\\PROYECTO_CHAT\\chatsito\\chatsito\\src\\main\\java\\com\\example\\chatsito\\BD_CHAT\\FOTOS\\"+linkfoto);
            //  Image defaultImage = new Image("D:\\USUARIO\\Escritorio\\CUATRI 6\\PROGRAMACIÓN ORIENTADA A OBJETOS\\PROYECTO\\PROYECTO_CHAT\\chatsito\\"+linkfoto);
            ImageView profileImageView = new ImageView(defaultImage);

            // Configura la imagen en un círculo
            profileImageView.setFitHeight(50);  // Ajusta el tamaño según tus necesidades
            profileImageView.setFitWidth(50);

            // Crea un StackPane para superponer la imagen en la esquina
            StackPane profilePane = new StackPane(profileImageView);
            StackPane.setAlignment(profileImageView, Pos.TOP_LEFT);

            // Agrega el StackPane al BorderPane en la esquina superior izquierda
            root.setTop(profilePane);
        }

    }