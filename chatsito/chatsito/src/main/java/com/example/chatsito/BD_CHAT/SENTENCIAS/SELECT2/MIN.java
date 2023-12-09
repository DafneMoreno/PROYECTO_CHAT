package com.example.chatsito.BD_CHAT.SENTENCIAS.SELECT2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MIN {
    public static void main(String directorio, String sentencia) {
        try {
            // Patrón para reconocer la consulta MIN
            String patron = "(?i)^SELECT\\s+MIN\\s*\\(\\s*(\\w+)\\s*\\)\\s+FROM\\s+(\\w+)$";
            // Verificar si el comando SQL coincide con el patrón
            if (sentencia.matches(patron)) {
                String columna = sentencia.replaceAll(patron, "$1");
                String nombreTabla = sentencia.replaceAll(patron, "$2");

                // Cargar la tabla desde el archivo
                File tabla = new File(directorio, nombreTabla + ".csv");

                if (tabla.exists()) {
                    try (BufferedReader tablaReader = new BufferedReader(new FileReader(tabla))) {
                        String encabezados = tablaReader.readLine();
                        int minimo = Integer.MAX_VALUE;
                        String fila;
                        boolean datosNoNumericos = false;
                        while ((fila = tablaReader.readLine()) != null) {
                            // Obtener el valor de la columna
                            String[] valores = fila.split(",");
                            String[] headers = encabezados.split(",");
                            int indiceColumna = -1;
                            // Buscar el índice de la columna en los encabezados
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
                            try {
                                int valorFila = Integer.parseInt(valores[indiceColumna].trim());
                                // Actualizar el mínimo si se encuentra un valor menor
                                if (valorFila < minimo) {
                                    minimo = valorFila;
                                }
                            } catch (NumberFormatException e) {
                                datosNoNumericos = true;
                            }
                        }
                        if (datosNoNumericos) {
                            System.out.println("Advertencia: Se encontraron datos no numéricos en la columna.");
                        } else {
                            System.out.println("MIN (" + columna  +")\n" + minimo);                        }

                    } catch (IOException e) {
                        System.out.println("Error al leer la tabla: " + e.getMessage());
                    }
                } else {
                    System.out.println("La tabla '" + nombreTabla + "' no existe en el directorio.");
                }
            } else {
                System.out.println("El comando SQL no tiene el formato correcto.");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
