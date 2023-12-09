package com.example.chatsito.BD_CHAT.SENTENCIAS.NUMERICAS;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RAND {
    /*
    RAND : esta función se usa para generar un número aleatorio, su valor cambia cada vez que se llama a la función. El script que se muestra a continuación demuestra su uso.

SELECT RAND() AS `random_result`;
     */
    public static void main(String directorio, String sentencia) {
        // Define un patrón para buscar la expresión "RAND()"
        Pattern patron = Pattern.compile("(?i)\\s*SELECT\\s+RAND\\(\\)\\s+AS\\s+'([a-zA-Z_]+)'\\s*");

        Matcher matcher = patron.matcher(sentencia);
        try {
            if (matcher.matches()) {
                Random rand = new Random();
                int resultado = rand.nextInt(); // Genera un número aleatorio entre 0 (inclusive) y 1 (exclusivo)
               // int resultado = rand.nextInt(Integer.MAX_VALUE); // Genera un número aleatorio positivo

                String nombreResultado = matcher.group(1);
                System.out.println(nombreResultado+ "\n" + resultado);
            } else {
                System.out.println("El comando SQL no tiene el formato correcto.");
            }
        } catch (Exception e) {
            System.out.println("Error al generar el número aleatorio: " + e.getMessage());
        }
    }
}



