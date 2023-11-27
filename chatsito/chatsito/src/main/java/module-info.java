module com.example.chatsito {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chatsito to javafx.fxml;
    exports com.example.chatsito;
}