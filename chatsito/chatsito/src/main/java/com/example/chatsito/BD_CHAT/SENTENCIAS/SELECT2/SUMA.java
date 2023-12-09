package com.example.chatsito.BD_CHAT.SENTENCIAS.SELECT2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SUMA {
    public static void main(String directorio, String sentencia) {
        // Obtener el nombre de la columna y de la tabla desde la sentencia SQL
        String columna = sentencia.replaceAll("(?i)^SELECT\\s+SUM\\s*\\(\\s*(\\w+)\\s*\\)\\s+FROM\\s+(\\w+)$", "$1");
        String nombreTabla = sentencia.replaceAll("(?i)^SELECT\\s+SUM\\s*\\(\\s*(\\w+)\\s*\\)\\s+FROM\\s+(\\w+)$", "$2");

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
                double suma = 0;
                boolean datosNoNumericos = false;
                while ((fila = tablaReader.readLine()) != null) {
                    String[] valores = fila.split(",");
                    String valorFila = valores[indiceColumna].trim();
                    try {
                        double valorNumerico = Double.parseDouble(valorFila);
                        suma += valorNumerico;
                    } catch (NumberFormatException e) {
                        datosNoNumericos = true;
                    }
                }
                if (datosNoNumericos) {
                    System.out.println("Advertencia: Se encontraron datos no numéricos en la columna.");
                } else {
                    System.out.println("SUM (" + columna + ") \n" + suma);
                }
            } catch (IOException e) {
                System.out.println("Error al leer la tabla: " + e.getMessage());
            }
        } else {
            System.out.println("La tabla '" + nombreTabla + "' no existe en el directorio.");
        }
    }
}
