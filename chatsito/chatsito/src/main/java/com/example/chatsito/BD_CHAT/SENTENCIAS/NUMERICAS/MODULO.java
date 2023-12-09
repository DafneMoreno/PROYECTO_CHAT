package com.example.chatsito.BD_CHAT.SENTENCIAS.NUMERICAS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MODULO {
    public static void main(String directorio, String sentencia) {
        // Define un patrón para buscar la expresión "SELECT num1 % num2;" o "SELECT num1 MOD num2;" en la sentencia SQL
        Pattern patron = Pattern.compile("(?i)\\s*SELECT\\s+(\\d+)\\s*(%|MOD)\\s*(\\d+)\\s*");

        Matcher matcher = patron.matcher(sentencia);
        try {
            if (matcher.find()) {
                int numero1 = Integer.parseInt(matcher.group(1));
                String operador = matcher.group(2);
                int numero2 = Integer.parseInt(matcher.group(3));

                int resultado;
                if (operador.equalsIgnoreCase("%")) {
                    resultado = numero1 % numero2;
                } else { // Si el operador es "MOD"
                    resultado = numero1 % numero2;
                }

                System.out.println(resultado);
            } else {
                System.out.println("El comando SQL no tiene el formato correcto.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error al convertir números: " + e.getMessage());
        }
    }
}
