package com.example.chatsito.BD_CHAT.SENTENCIAS.NUMERICAS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SUSTRACCION { public static void main(String directorio, String sentencia) {

    Pattern patron = Pattern.compile("(?i)\\s*SELECT\\s+(\\d+)\\s*-\\s*(\\d+)");

    Matcher matcher = patron.matcher(sentencia);
    try {
        if (matcher.find()) {
            int numero1 = Integer.parseInt(matcher.group(1));
            int numero2 = Integer.parseInt(matcher.group(2));

            int resultado = numero1 - numero2;
            System.out.println(resultado);
        } else {
            System.out.println("El comando SQL no tiene el formato correcto.");
        }
    } catch (NumberFormatException e) {
        System.out.println("Error al convertir números: " + e.getMessage());
    }
}
}