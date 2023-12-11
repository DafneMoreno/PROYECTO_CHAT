module com.example.chatsito {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.scripting;


    opens com.example.chatsito to javafx.fxml;
    exports com.example.chatsito;
    exports com.example.chatsito.PRUEBA1;
    opens com.example.chatsito.PRUEBA1 to javafx.fxml;
  exports com.example.chatsito.Controlador;

}

