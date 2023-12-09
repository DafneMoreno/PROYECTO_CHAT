package com.example.chatsito.BD_CHAT.SENTENCIAS.NUMERICAS;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DIVISION {
    public static void main(String directorio, String sentencia) {
        // No es necesario crear un nuevo BufferedReader aquí

        // Define un patrón para buscar la expresión "SELECT num1 / num2;" en la sentencia SQL
        Pattern patron = Pattern.compile("(?i)\\s*SELECT\\s+(\\d+)\\s*/\\s*(\\d+)");

        Matcher matcher = patron.matcher(sentencia);
        try {
            if (matcher.find()) {
                double numero1 = Double.parseDouble(matcher.group(1));
                double numero2 = Double.parseDouble(matcher.group(2));

                if (numero2 != 0) {
                    double resultado = numero1 / numero2;
                    System.out.println( resultado);
                } else {
                    System.out.println("La división por cero no está permitida.");
                }
            } else {
                System.out.println("El comando SQL no tiene el formato correcto.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error al convertir números: " + e.getMessage());
        }
    }
}
