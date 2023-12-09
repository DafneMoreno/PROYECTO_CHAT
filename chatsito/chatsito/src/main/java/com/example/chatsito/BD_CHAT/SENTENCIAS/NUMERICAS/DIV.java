package com.example.chatsito.BD_CHAT.SENTENCIAS.NUMERICAS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DIV {
    public static void main(String directorio, String sentencia) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Define un patrón para buscar la expresión "DIV" en la sentencia SQL
        Pattern patron = Pattern.compile("(?i)\\s*SELECT\\s+(\\d+)\\s*DIV\\s*(\\d+)");

        Matcher matcher = patron.matcher(sentencia);
        try {
            if (matcher.find()) {
                int numero1 = Integer.parseInt(matcher.group(1));
                int numero2 = Integer.parseInt(matcher.group(2));

                if (numero2 != 0) {
                    int resultado = numero1 / numero2;
                    System.out.println(resultado);
                } else {
                    System.out.println("La división por cero no está permitida.");
                }
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

    }
}