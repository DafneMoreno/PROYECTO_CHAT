package com.example.chatsito.BD_CHAT.SENTENCIAS.SELECT2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MAX {
    public static void main(String directorio, String sentencia) {

        // Obtener el nombre de la columna y de la tabla desde la sentencia SQL
        String columna = sentencia.replaceAll("(?i)^SELECT\\s+MAX\\s*\\(\\s*(\\w+)\\s*\\)\\s+FROM\\s+(\\w+)$", "$1");
        String nombreTabla = sentencia.replaceAll("(?i)^SELECT\\s+MAX\\s*\\(\\s*(\\w+)\\s*\\)\\s+FROM\\s+(\\w+)$", "$2");

        // Combinar el directorio y el nombre de la tabla para obtener la ruta del archivo
        File tabla = new File(directorio, nombreTabla + ".csv");

        if (tabla.exists()) {
            try (BufferedReader tablaReader = new BufferedReader(new FileReader(tabla))) {
                String encabezados = tablaReader.readLine();

                int indiceColumna = -1;
                String[] headers = encabezados.split(",");
                for (int i = 0; i < headers.length; i++) {
                    if (headers[i].trim().equalsIgnoreCase(columna)) {
                        indiceColumna = i;
                        break;
                    }
                }

                if (indiceColumna == -1) {
                    System.out.println("Advertencia: La columna especificada en la condición no existe.");
                    return;
                }

                String fila;
                String maximoValor = null;
                boolean datosNoNumericos = false; // Bandera para rastrear si se encuentran datos no numéricos
                while ((fila = tablaReader.readLine()) != null) {
                    String[] valores = fila.split(",");
                    String valorFila = valores[indiceColumna].trim();

                    try {
                        Double.parseDouble(valorFila); // Intentar convertir a número
                        if (maximoValor == null || valorFila.compareTo(maximoValor) > 0) {
                            maximoValor = valorFila;
                        }
                    } catch (NumberFormatException e) {
                        datosNoNumericos = true; // Se encontraron datos no numéricos
                    }
                }

                if (datosNoNumericos) {
                    System.out.println("Advertencia: Se encontraron datos no numéricos en la columna.");
                } else {
                    System.out.println("MAX (" + columna + ")\n" + maximoValor);
                }

            } catch (IOException e) {
                System.out.println("Error al leer la tabla: " + e.getMessage());
            }
        } else {
            System.out.println("La tabla '" + nombreTabla + "' no existe en el directorio.");
        }
    }
}
