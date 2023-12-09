package com.example.chatsito.BD_CHAT.SENTENCIAS;
import com.example.chatsito.BD_CHAT.SENTENCIAS.NUMERICAS.*;
import com.example.chatsito.BD_CHAT.SENTENCIAS.SELECT2.*;
import com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

class AutoCloseableBufferedReader extends BufferedReader implements AutoCloseable {

    public AutoCloseableBufferedReader(InputStreamReader in) {
        super(in);
    }


    @Override
    public void close() throws IOException {
        super.close();
    }
}
public class USE {
    public static void main() {
        try (AutoCloseableBufferedReader reader = new AutoCloseableBufferedReader(new InputStreamReader(System.in))) {
            String userInput = "";
            SHOW show = new SHOW();

            while (true) {
                StringBuilder userInputBuilder = new StringBuilder();
                char inputChar;
                while (true) {
                    inputChar = (char) reader.read();
                    if (inputChar == ';') {
                        break;  // Salir del bucle si se ingresa ';'
                    }
                    userInputBuilder.append(inputChar);
                }


                userInput = userInputBuilder.toString().trim();

                // Expresión regular para verificar el formato
                String patron = "(?i)^USE\\s+(.*?)";//a parte (?i)al comienzo de la expresión regular hace que USE sea insensible a mayúsculas y minúsculas
//                String patron = "(?i)^USE \\$(.*?)\\$";//a parte (?i)al comienzo de la expresión regular hace que USE sea insensible a mayúsculas y minúsculas
              if (userInput.matches(patron)) {
                    String path = userInput.replaceAll(patron, "$1");
                  //  System.out.println("Ruta ingresada: " + path);
                    File file = new File(path);
                    if (file.isDirectory()) {
                        System.setProperty("user.dir", path);
                        System.out.println("Conectado a la base de datos");
                        show.main(path);

                        break;
                    }
                    else {
                        System.out.println("La BD no existe");
                    }
                } else {
                    System.out.println("La ruta no tiene el formato correcto.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer la entrada del usuario: " + e.getMessage());
        }
    }
    public void puntocoma(){

    }

}