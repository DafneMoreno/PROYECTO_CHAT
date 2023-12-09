package com.example.chatsito.BD_CHAT.SENTENCIAS.SELECT2;

import java.io.*;

public class COUNT {
    public static void main(String directorio, String sentencia) {
        try {
            String patronAsterisco = "(?i)^SELECT\\s+COUNT\\s*\\(\\s*\\*\\s*\\)\\s+FROM\\s+(\\w+)(?:\\s+WHERE\\s+([^;]+))?$";
            String patronColumnas = "(?i)^SELECT\\s+COUNT\\s*\\(\\s*([^\\s]+)\\s*\\)\\s+FROM\\s+(\\w+)(?:\\s+WHERE\\s+([^;]+))?$";

            if (sentencia.matches(patronAsterisco)) {
                // Manejo para COUNT(*)
                String nombreTabla = sentencia.replaceAll(patronAsterisco, "$1");
                String condicion = sentencia.replaceAll(patronAsterisco, "$2");

                File tabla = new File(directorio, nombreTabla + ".csv");

                if (tabla.exists()) {
                    try (BufferedReader tablaReader = new BufferedReader(new FileReader(tabla))) {
                        String encabezados = tablaReader.readLine();

                        int count = 0;
                        String fila;
                        while ((fila = tablaReader.readLine()) != null) {
                            if (condicion == null || evaluarCondicion(fila, encabezados, condicion)) {
                                count++;
                            }
                        }

                        System.out.println("COUNT (" + condicion + ")\n " + count);

                    } catch (IOException e) {
                        System.out.println("Error al leer la tabla: " + e.getMessage());
                    }
                } else {
                    System.out.println("La tabla '" + nombreTabla + "' no existe en el directorio.");
                }

                return;
            } else if (sentencia.matches(patronColumnas)) {
                // Manejo para COUNT(columnas)
                String nombreColumna = sentencia.replaceAll(patronColumnas, "$1");
                String nombreTabla = sentencia.replaceAll(patronColumnas, "$2");
                String condicion = sentencia.replaceAll(patronColumnas, "$3");

                File tabla = new File(directorio, nombreTabla + ".csv");

                if (tabla.exists()) {
                    try (BufferedReader tablaReader = new BufferedReader(new FileReader(tabla))) {
                        String encabezados = tablaReader.readLine();

                        // Verificar que ambas columnas existan
                        if (!columnasExistentes(nombreColumna, condicion, encabezados)) {
                            System.out.println("Advertencia: Una o ambas columnas no existen.");
                            return;
                        }

                        int count = 0;
                        String fila;
                        while ((fila = tablaReader.readLine()) != null) {
                            if (evaluarCondicion(fila, encabezados, condicion)) {
                                count++;
                            }
                        }

                        System.out.println("COUNT (" + condicion + ")\n " + count);

                    } catch (IOException e) {
                        System.out.println("Error al leer la tabla: " + e.getMessage());
                    }
                } else {
                    System.out.println("La tabla '" + nombreTabla + "' no existe en el directorio.");
                }

                return;
            }

            // Resto del código para otros casos...

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean columnasExistentes(String nombreColumna, String condicion, String encabezados) {
        String[] headers = encabezados.split(",");
        boolean columnaNombreExiste = false;
        boolean columnaCondicionExiste = false;

        for (String header : headers) {
            if (header.trim().equalsIgnoreCase(nombreColumna)) {
                columnaNombreExiste = true;
            }

            if (condicion != null && condicion.contains(header.trim())) {
                columnaCondicionExiste = true;
            }
        }

        return columnaNombreExiste && columnaCondicionExiste;
    }
    private static boolean evaluarCondicion(String fila, String encabezados, String condicion) {
        String[] valores = fila.split(",");
        String[] headers = encabezados.split(",");
        String[] condicionTokens = condicion.trim().split("\\s+");
        String columna = condicionTokens[0];
        String operador = condicionTokens[1];
        String valor = condicionTokens[2];

        int indiceColumna = -1;
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].trim().equalsIgnoreCase(columna)) {
                indiceColumna = i;
                break;
            }
        }
        if (indiceColumna == -1) {
            System.out.println("Advertencia: La columna especificada en la condición no existe.");
            return false;
        }
        String valorFila = valores[indiceColumna].trim();
        if (operador.equals("=")) {
            return valorFila.equals(valor);
        } else if (operador.equals(">")) {
            return Double.parseDouble(valorFila) > Double.parseDouble(valor);
        } else if (operador.equals("<")) {
            return Double.parseDouble(valorFila) < Double.parseDouble(valor);
        } else {
            throw new IllegalArgumentException("Operador no soportado: " + operador);
        }
    }
}
