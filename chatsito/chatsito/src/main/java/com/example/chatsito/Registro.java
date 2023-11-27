package com.example.chatsito;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Registro extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        //label para el nombre
        Label nom = new Label(("Nombre"));

        //Caja de texto para el nombre
        TextField nombre = new TextField();

        //Label para la fecha de nacimiento
        Label fecha = new Label("Fecha de nacimiento");

        //Seleccionador de Fecha
        //DatePicker date = new DatePicker();

        //Seleccionador de

        //Label para genero
        Label gen = new Label("Genero");

        //Seleccionador de genero
        ToggleGroup btngen = new ToggleGroup();
        RadioButton hombre = new RadioButton("Hombre");
        hombre.setToggleGroup(btngen);
        RadioButton mujer = new RadioButton(("Mujer"));
        mujer.setToggleGroup(btngen);

        //Label para registrar
        Button btnregistro = new Button("Registrar");

        //grid - cuadricula
        GridPane grid = new GridPane();
        grid.setMinSize(500,300);
        grid.setPadding(new Insets(10,10,10,10));

        //setting the vertical and horizontal gaps
        grid.setVgap(5);
        grid.setHgap(5);

        //setting the grid aligment
        grid.setAlignment(Pos.CENTER);

        //Arranging all the nodes in the grid
        grid.add(nom,0,0);
        grid.add(nombre,1,0);

       /* grid.add(fecha,0,1);
        grid.add(date,1,1);*/

        grid.add(gen,0,2);
        grid.add(hombre,1,2);
        grid.add(mujer,2,2);

        grid.add(btnregistro,2,4);




        //Styling nodes
        btnregistro.setStyle("-fx-background-color:darkslateblue; -fx-textfill:white;");

        nom.setStyle("-fx-font:normal bold 15px 'serif' ");
        fecha.setStyle("-fx-font:normal bold 15px 'serif' ");
        gen.setStyle("-fx-font:normal bold 15px 'serif' ");

        //setting the background color
        grid.setStyle("-fx-background-color:BEIGE;");

        //creating a scene object
        Scene scene = new Scene(grid);

        //setting the title of stage
        stage.setTitle("Registrar");

        //Adding scene to the stage
        stage.setScene(scene);

        //displaying the contents of the stage
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}