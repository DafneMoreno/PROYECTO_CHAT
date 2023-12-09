package com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DELETE2 {
    public static void main(String directorio, String sqlCommand) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Patron para reconocer la consulta DELETE
        String patron = "(?i)^DELETE\\s+FROM\\s+(\\w+)\\s+WHERE\\s+(.+)";

        // Verificar si el comando SQL coincide con el patrón
        if (sqlCommand.matches(patron)) {
            // Obtiene el nombre de la tabla y la condición de la consulta
            String nombreTabla = sqlCommand.replaceAll(patron, "$1");
            String condicion = sqlCommand.replaceAll(patron, "$2");
            // Carga la tabla desde el archivo
            File tabla = new File(directorio, nombreTabla + ".csv");

            if (tabla.exists()) {
                // Elimina filas que cumplan con la condición
                File tempFile = new File(directorio, "temp.csv");

                try (BufferedReader tablaReader = new BufferedReader(new FileReader(tabla));
                     FileWriter writer = new FileWriter(tempFile)) {

                    String encabezados = tablaReader.readLine();
                    writer.write(encabezados + "\n");

                    String fila;
                    boolean algunaFilaEliminada = false;
                    while ((fila = tablaReader.readLine()) != null) {
                        // Evaluar la condición para cada fila
                        if (!evaluarCondicion(fila, encabezados, condicion)) {
                            writer.write(fila + "\n");
                        }   else {
                        algunaFilaEliminada = true; // Marcar que al menos una fila se ha eliminado
                    }
                }

                if (!algunaFilaEliminada) {
                    System.out.println("No se eliminaron filas.");
                } else {
                    // Reemplazar el archivo original con el archivo temporal
                  /*  tabla.delete();
                    tempFile.renameTo(tabla);*/
                    System.out.println("Filas eliminadas con éxito en la tabla '" + nombreTabla + "'.");

                   /* try (BufferedReader lector = new BufferedReader(new FileReader(tempFile));
                         BufferedWriter escritor = new BufferedWriter(new FileWriter(tabla))) {

                        String linea;
                        while ((linea = lector.readLine()) != null) {
                            escritor.write(linea);
                            escritor.newLine();
                        }

                        System.out.println("Contenido copiado con éxito de '" + tempFile + "' a '" + tabla + "'.");

                    } catch (IOException e) {
                        System.err.println("Error al copiar el contenido: " + e.getMessage());
                    }*/
                }
            } catch (IOException e) {
                System.out.println("Error al eliminar filas: " + e.getMessage());
            }
        } else {
            System.out.println("La tabla '" + nombreTabla + "' no existe en el directorio.");
        }
    } else {
            System.out.println("El comando SQL no tiene el formato correcto.");
        }
    }

    // Método para evaluar la condición WHERE
    private static boolean evaluarCondicion(String fila, String encabezados, String condicion) {
        // Dividir los encabezados en columnas
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

            // Si alguna condición AND no se cumple, retornar false
            if (!cumpleCondicionAND) {
                return false;
            }
        }

        // Si todas las condiciones AND se cumplen, retornar true
        return true;
    }
}
