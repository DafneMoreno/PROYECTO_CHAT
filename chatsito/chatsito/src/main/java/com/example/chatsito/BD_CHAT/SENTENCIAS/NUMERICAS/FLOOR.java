package com.example.chatsito.BD_CHAT.SENTENCIAS.NUMERICAS;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class FLOOR {
    /*
    FLOOR : esta función elimina decimales de un número y lo redondea al número más bajo más cercano. El script que se muestra a continuación demuestra su uso.

SELECT FLOOR (23/6) AS `floor_result`;
Ejecutar el script anterior nos da los siguientes resultados.

Floor_result
3
     */
    public static void main(String directorio, String sentencia) {
        Pattern patron = Pattern.compile("(?i)\\s*SELECT\\s+FLOOR\\s*\\(\\s*(\\d+)\\s*/\\s*(\\d+)\\s*\\)\\s+AS\\s+'([a-zA-Z_]+)'\\s*");

        Matcher matcher = patron.matcher(sentencia);
        try {
            if (matcher.find()) {
                double numero1 = Integer.parseInt(matcher.group(1));
                double numero2 = Integer.parseInt(matcher.group(2));
                String nombreResultado = matcher.group(3);

                double resultado = Math.floor(numero1 / numero2);
                int resultadoEntero = (int) resultado;
                System.out.println(nombreResultado + "\n" + resultadoEntero);
            } else {
                System.out.println("El comando SQL no tiene el formato correcto.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error al convertir números: " + e.getMessage());
        }
    }
}
