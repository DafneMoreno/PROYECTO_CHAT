package com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


public class UPDATE {
    public static void main(String directorio, String sqlCommand) {
        String patron = "(?i)^UPDATE\\s+(\\w+)\\s+SET\\s+(.+)\\s+WHERE\\s+(.+)";
        if (sqlCommand.matches(patron)) {
            String nombreTabla = sqlCommand.replaceAll(patron, "$1");
            String actualizaciones = sqlCommand.replaceAll(patron, "$2");
            String condicion = sqlCommand.replaceAll(patron, "$3");

            File tabla = new File(directorio, nombreTabla + ".csv");

            if (tabla.exists()) {
                File tempFile = new File(directorio, "updateArch.csv");

                try (BufferedReader tablaReader = new BufferedReader(new FileReader(tabla));
                     FileWriter writer = new FileWriter(tempFile)) {

                    String encabezados = tablaReader.readLine();
                    writer.write(encabezados + "\n");

                    String fila;
                    boolean algunaFilaActualizada = false;
                    while ((fila = tablaReader.readLine()) != null) {
                        // Evaluar la condición para cada fila
                        if (evaluarCondicion(fila, encabezados, condicion)) {
                            // Aplicar las actualizaciones en la fila
                            String filaActualizada = aplicarActualizaciones(fila, encabezados, actualizaciones);
                            writer.write(filaActualizada + "\n");
                            algunaFilaActualizada = true;
                        } else {
                            // Mantener la fila sin cambios
                            writer.write(fila + "\n");
                        }
                    }

                    if (!algunaFilaActualizada) {
                        System.out.println("No se actualizaron filas.");
                    }  else if (algunaFilaActualizada) {
                        System.out.println("Filas actualizadas con éxito en la tabla '" + nombreTabla + "'.");
                    }
                } catch (IOException e) {
                    System.out.println("Error al actualizar filas: " + e.getMessage());
                }
            } else {
                System.out.println("La tabla '" + nombreTabla + "' no existe en el directorio.");
            }
        } else {
            System.out.println("El comando SQL no tiene el formato correcto.");
        }
    }

    private static boolean evaluarCondicion(String fila, String encabezados, String condicion) {
        String[] columnas = encabezados.split(",");
        String[] valores = fila.split(",");

        // Separar condiciones con operador AND
        String[] condicionesAND = condicion.split("\\s+AND\\s+");

        // Evaluar cada condición AND
        for (String condicionAND : condicionesAND) {
            boolean cumpleCondicionAND = true;

            // Separar la condición en campo y valor
            String[] partesCondicion = condicionAND.split("=");
            if (partesCondicion.length == 2) {
                String campo = partesCondicion[0].trim();
                String valor = partesCondicion[1].trim();

                // Buscar el índice de la columna correspondiente al campo
                int indiceColumna = -1;
                for (int i = 0; i < columnas.length; i++) {
                    if (columnas[i].trim().equalsIgnoreCase(campo)) {
                        indiceColumna = i;
                        break;
                    }
                }

                // Si se encontró la columna, verificar si cumple con la condición
                if (indiceColumna >= 0 && indiceColumna < valores.length) {
                    String valorEnFila = valores[indiceColumna].trim();
                    if (!valorEnFila.equalsIgnoreCase(valor)) {
                        cumpleCondicionAND = false;
                    }
                } else {
                    cumpleCondicionAND = false; // El campo no existe en los encabezados
                }
            } else {
                cumpleCondicionAND = false; // La condición AND no tiene el formato correcto
            }

            if (!cumpleCondicionAND) {
                return false;
            }
        }

        return false;
    }

    public static String aplicarActualizaciones(String fila, String encabezados, String actualizaciones) {
        String[] actualizacionesArray = actualizaciones.split(",");

        String[] valores = fila.split(",");

        Map<String, String> actualizacionesMap = new HashMap<>();

        for (String actualizacion : actualizacionesArray) {
            // Dividir cada actualización en "columna" y "valor"
            String[] partes = actualizacion.trim().split("=");

            if (partes.length == 2) {
                String columna = partes[0].trim();
                String valor = partes[1].trim();
                actualizacionesMap.put(columna, valor);
            }
        }

        String[] columnas = encabezados.split(",");

        for (String columna : actualizacionesMap.keySet()) {
            if (!contieneColumna(columnas, columna)) {
                System.out.println("La columna '" + columna + "' no existe en los encabezados.");
                return fila; // No se realiza ninguna actualización
            }
        }

        for (int i = 0; i < columnas.length; i++) {
            String columna = columnas[i].trim();
            if (actualizacionesMap.containsKey(columna)) {
                valores[i] = actualizacionesMap.get(columna);
            }
        }

        StringBuilder filaActualizada = new StringBuilder();
        for (String valor : valores) {
            filaActualizada.append(valor).append(",");
        }

        if (filaActualizada.length() > 0) {
            filaActualizada.setLength(filaActualizada.length() - 1);
        }

        return filaActualizada.toString();
    }

    private static boolean contieneColumna(String[] columnas, String columna) {
        for (String col : columnas) {
            if (col.trim().equalsIgnoreCase(columna)) {
                return true;
            }
        }
        return false;
    }
}
