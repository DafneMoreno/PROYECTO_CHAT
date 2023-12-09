package com.example.chatsito.BD_CHAT.SENTENCIAS.NUMERICAS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MULTIPLICACION {
    public static void main(String directorio, String sentencia) {
        // Define un patrón para buscar la expresión "SELECT num1 * num2 AS `nombre_resultado`;" en la sentencia SQL
        Pattern patron = Pattern.compile("(?i)\\s*SELECT\\s+(\\d+)\\s*\\*\\s*(\\d+)\\s+AS\\s+'([a-zA-Z_]+)'\\s*");

        Matcher matcher = patron.matcher(sentencia);
        try {
            if (matcher.find()) {
                int numero1 = Integer.parseInt(matcher.group(1));
                int numero2 = Integer.parseInt(matcher.group(2));
                String nombreResultado = matcher.group(3);

                int resultado = numero1 * numero2;
                System.out.println(nombreResultado);
                System.out.println(resultado);
            } else {
                System.out.println("El comando SQL no tiene el formato correcto.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error al convertir números: " + e.getMessage());
        }
    }
}

