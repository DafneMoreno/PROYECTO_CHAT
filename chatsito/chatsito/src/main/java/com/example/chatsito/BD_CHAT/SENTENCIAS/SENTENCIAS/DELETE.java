package com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;

public class DELETE {
    public static void main(String directorio, String sqlCommand) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String patron = "DELETE FROM (\\w+) WHERE (.+)";

        // Verificar si el comando SQL coincide con el patrón
        if (sqlCommand.matches(patron)) {
            String nombreTabla = sqlCommand.replaceAll(patron, "$1");
            String condicion = sqlCommand.replaceAll(patron, "$2");
            //System.out.println("CONDICIONNNNNNN "+ condicion);

            File tabla = new File(directorio, nombreTabla + ".csv");

            if (tabla.exists()) {
                // Eliminar filas que cumplan con la condición
                File tempFile = new File(directorio, "temp.csv");

                try (BufferedReader tablaReader = new BufferedReader(new FileReader(tabla));
                     FileWriter writer = new FileWriter(tempFile)) {

                    String encabezados = tablaReader.readLine();
                    writer.write(encabezados + "\n");

                    String fila;
                    while ((fila = tablaReader.readLine()) != null) {
                        // Evaluar la condición para cada fila
                        if (!evaluarCondicion(fila, condicion)) {
                            writer.write(fila + "\n");
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error al eliminar filas: " + e.getMessage());
                }

                // Reemplazar el archivo original con el archivo temporal
                tempFile.renameTo(tabla);
                System.out.println("Filas eliminadas con éxito en la tabla '" + nombreTabla + "'.");
            } else {
                System.out.println("La tabla '" + nombreTabla + "' no existe en el directorio.");
            }
        } else {
            System.out.println("El comando SQL no tiene el formato correcto.");
        }
    }

    private static boolean evaluarCondicion(String fila, String condicion) {
        // Separar la condición en partes usando el operador "AND"
        String[] condicionesAND = condicion.split("\\s+AND\\s+");

        // Evaluar cada condición AND
        for (String condicionAND : condicionesAND) {
            // Dividir la condición AND en partes (campo, operador y valor)
            String[] partesCondicion = condicionAND.split("\\s+");
            if (partesCondicion.length == 3) {
                String campo = partesCondicion[0];
                String operador = partesCondicion[1];
                String valor = partesCondicion[2];

                // Obtener el valor correspondiente en la fila
                String valorEnFila = obtenerValorEnFilaSegunCampo(fila, campo);

                // Evaluar la condición según el operador
                boolean cumpleCondicion = evaluarCondicionSegunOperador(valorEnFila, operador, valor);

                // Si alguna condición AND no se cumple, retornar false
                if (!cumpleCondicion) {
                    return false;
                }
            } else {
                // La condición AND no tiene el formato correcto, retornar false
                return false;
            }
        }

        // Si todas las condiciones AND se cumplen, retornar true
        return true;
    }

    private static boolean evaluarCondicion2(String fila, String condicion) {
        // Separar la condición en partes usando el operador "="

        String[] partesCondicion = condicion.split("=");

        if (partesCondicion.length == 2) {
            String encabezado = partesCondicion[0].trim();
            String valor = partesCondicion[1].trim();

            // Obtener el valor correspondiente en la fila
            String valorEnFila = obtenerValorEnFilaSegunCampo(fila, encabezado);

            // Evaluar si el valor en la fila coincide con el valor en la condición
            return valorEnFila != null && valorEnFila.equals(valor);
        } else {
            // La condición no tiene el formato correcto
            return false;
        }
    }


    private static boolean evaluarCondicion1(String fila, String condicion) {

        // Separar condiciones con operador OR
        String[] condicionesOR = condicion.split("\\s+OR\\s+");

        // Evaluar cada condición OR
        for (String condicionOR : condicionesOR) {
            // Separar condiciones con operador AND
            String[] condicionesAND = condicionOR.split("\\s+AND\\s+");

            boolean cumpleCondicionAND = true;
            for (String condicionAND : condicionesAND) {
                // Dividir la condición en sus partes (campo, operador y valor)
                String[] partesCondicion = condicionAND.split("\\s+");
                if (partesCondicion.length == 3) {
                    String campo = partesCondicion[0];
                    String operador = partesCondicion[1];
                    String valor = partesCondicion[2];

                    // Obtener el valor correspondiente en la fila
                    String valorEnFila = obtenerValorEnFilaSegunCampo(fila, campo);

                    // Evaluar la condición según el operador
                    boolean cumpleCondicion = evaluarCondicionSegunOperador(valorEnFila, operador, valor);

                    // Si alguna condición AND no se cumple, marca cumpleCondicionAND como false
                    if (!cumpleCondicion) {
                        cumpleCondicionAND = false;
                        break; // No es necesario seguir evaluando las demás condiciones AND
                    }
                } else {
                    cumpleCondicionAND = false;
                }
            }
            if (cumpleCondicionAND) {
                return true;
            }

        }

        return false;
    }
    private static boolean evaluarCondicionSegunOperador(String valorEnFila, String operador, String valor) {
        if (operador.equals("=")) {
            return valorEnFila.equals(valor);
        } else if (operador.equals("<>")) {
            return !valorEnFila.equals(valor);
        }
        return false;
    }
    private static String obtenerValorEnFilaSegunCampo(String fila, String campo) {
        // Divide la fila en sus valores individuales
        String[] valores = fila.split(",");

        for (String valor : valores) {
            String[] partes = valor.split("=");
            if (partes.length == 2) {
                String nombreColumna = partes[0].trim();
                String valorColumna = partes[1].trim();

                if (nombreColumna.equalsIgnoreCase(campo)) {
                    return valorColumna;
                }
            }
        }

        return null;
    }


}
