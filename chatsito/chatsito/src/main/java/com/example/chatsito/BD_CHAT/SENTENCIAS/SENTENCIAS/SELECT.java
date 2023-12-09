package com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS;
import java.io.*;
import java.util.regex.*;

public class SELECT {
    public static void main(String directorio, String sqlCommand) {
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
                    for (int i = 0; i < indicesColumnasSeleccionadas.length; i++) {
                        int indice = indicesColumnasSeleccionadas[i];
                        if (indice >= 0) {
                            System.out.print(columnas[indice]);
                            if (i < indicesColumnasSeleccionadas.length - 1) {
                                System.out.print(", ");
                            }
                        }
                    }
                    System.out.println();
                    String fila;
                    while ((fila = tablaReader.readLine()) != null) {
                        String[] valores = fila.split(",");
                        if (cumpleCondicion(valores, encabezados, condicion)) {
                            // Mostrar valores de las columnas seleccionadas
                            for (int i = 0; i < indicesColumnasSeleccionadas.length; i++) {
                                int indice = indicesColumnasSeleccionadas[i];
                                if (indice >= 0) {
                                    System.out.print(valores[indice]);
                                    if (i < indicesColumnasSeleccionadas.length - 1) {
                                        System.out.print(", ");
                                    }
                                }
                            }
                            System.out.println(); // Nueva línea después de la fila
                        }
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
    }
    private static boolean cumpleCondicion(String[] valores, String encabezados, String condicion) {
        if (condicion == null || condicion.trim().isEmpty()) {
            return true;
        }

        String[] condiciones = condicion.trim().split("\\s+AND\\s+");

        for (String cond : condiciones) {
            if (!evaluarCondicionIndividual(valores, encabezados, cond)) {
                return false;
            }
        }
        return true;
    }
    private static boolean evaluarCondicionIndividual(String[] valores, String encabezados, String condicion) {
        // Dividir la condición en columna y valor
        String[] partes = condicion.trim().split("=");

        if (partes.length == 2) {
            String columna = partes[0].trim();
            String valor = partes[1].trim();
            String[] columnas = encabezados.split(",");
            int indiceColumna = -1;
            for (int i = 0; i < columnas.length; i++) {
                if (columnas[i].trim().equalsIgnoreCase(columna)) {
                    indiceColumna = i;
                    break;
                }
            }

            if (indiceColumna >= 0 && indiceColumna < valores.length) {
                String valorEnFila = valores[indiceColumna].trim();
                if (valorEnFila.equalsIgnoreCase(valor) || valorEnFila.equals("'" + valor + "'")) {
                    return true;
                }
            }
        }
        return false;
    }

}
