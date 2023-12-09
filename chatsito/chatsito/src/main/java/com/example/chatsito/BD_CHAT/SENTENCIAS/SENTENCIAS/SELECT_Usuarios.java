package com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS;

import java.io.*;
import java.util.regex.*;

public class SELECT_Usuarios {
    public static int main(String directorio, String sqlCommand, String usuarioInput, String contrasenaInput) {
        //String sqlCommand = "SELECT * FROM Usuarios WHERE usuario = '" + usuarioInput + "' AND password = '" + contrasenaInput + "'";

        String patron = "(?i)^SELECT\\s+(.+?)\\s+FROM\\s+(\\w+)(?:\\s+WHERE\\s+(.+))?$";
        Matcher matcher = Pattern.compile(patron).matcher(sqlCommand);

        if (matcher.find()) {
            String columnasSeleccionadas = matcher.group(1).trim();
            String nombreTabla = matcher.group(2).trim();
            String condicion = matcher.group(3);

            File tabla = new File(directorio, nombreTabla + ".csv");

            if (tabla.exists()) {
                try (BufferedReader tablaReader = new BufferedReader(new FileReader(tabla))) {
                    String encabezados = tablaReader.readLine();
                    String[] columnas = encabezados.split(",");

                    // Verificar si se seleccionan todas las columnas con "*"
                    String[] columnasASeleccionar;
                    if (columnasSeleccionadas.equals("*")) {
                        columnasASeleccionar = columnas;
                    } else {
                        columnasASeleccionar = columnasSeleccionadas.split(",");
                    }

                    // Encontrar los índices de las columnas seleccionadas
                    int[] indicesColumnasSeleccionadas = new int[columnasASeleccionar.length];
                    for (int i = 0; i < columnasASeleccionar.length; i++) {
                        String columnaSeleccionada = columnasASeleccionar[i].trim();
                        indicesColumnasSeleccionadas[i] = -1; // Inicializar en -1 (no encontrado)
                        for (int j = 0; j < columnas.length; j++) {
                            if (columnas[j].trim().equalsIgnoreCase(columnaSeleccionada)) {
                                indicesColumnasSeleccionadas[i] = j;
                                break;
                            }
                        }
                    }

                    // Verificar si la tabla tiene la columna de usuario y contraseña
                    int indiceUsuario = encontrarIndiceColumna("username", columnas);
                    int indiceContrasena = encontrarIndiceColumna("password", columnas);

                    if (indiceUsuario != -1 && indiceContrasena != -1) {
                        String fila;
                        while ((fila = tablaReader.readLine()) != null) {
                            String[] valores = fila.split(",");
                            if (cumpleCondicion(valores, encabezados, condicion)
                                    && valores.length > indiceUsuario
                                    && valores.length > indiceContrasena
                                    && valores[indiceUsuario].equals(usuarioInput)
                                    && valores[indiceContrasena].equals(contrasenaInput)) {
                                // Mostrar valores de las columnas seleccionadas
                                for (int i : indicesColumnasSeleccionadas) {
                                    if (i >= 0) {
                                        System.out.print(columnas[i] + ": " + valores[i] + ", ");
                                    }
                                }
                                return 1; // Usuario y contraseña coinciden
                            }
                        }
                        return 0; // Usuario no encontrado
                    } else {
                        return -1;
                    }
                } catch (IOException e) {
                    System.out.println("Error al leer la tabla: " + e.getMessage());
                }
            } else {
                System.out.println("La tabla '" + nombreTabla + "' no existe en el directorio.");
            }
        } else {
            System.out.println("El comando SQL no tiene el formato correcto.");
        }

        return -1; // Error general
    }

    private static boolean cumpleCondicion(String[] valores, String encabezados, String condicion) {
        // Implementar tu lógica de validación de condiciones si es necesario
        return true;
    }

    private static int encontrarIndiceColumna(String nombreColumna, String[] columnas) {
        for (int i = 0; i < columnas.length; i++) {
            if (columnas[i].trim().equalsIgnoreCase(nombreColumna)) {
                return i;
            }
        }
        return -1; // No encontrado
    }
}
