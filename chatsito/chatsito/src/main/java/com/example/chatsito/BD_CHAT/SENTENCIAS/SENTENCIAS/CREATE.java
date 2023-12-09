package com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CREATE {
    public static void main(String directorio, String userInput) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {


                // Expresión regular para extraer el nombre de la tabla y la lista de columnas
                String pattern = "(?i)^CREATE TABLE\\s+(\\w+)\\s*\\((.*?)\\)";
//se indica que puede venir uno o más símbolos de espacio en blanco, de ahí \\s+.
                //\\s*  permite cero o más espacios en blanco antes de los paréntesis
                //`((.*?))` en una expresión regular se utiliza para capturar un grupo de caracteres dentro de un texto.
                // Buscar el patrón en el comando SQL
                Pattern regexPattern = Pattern.compile(pattern, Pattern.DOTALL);
                Matcher matcher = regexPattern.matcher(userInput);

                if (matcher.matches()) {
                    String nombreTabla = matcher.group(1);
                    String ListaColuma = matcher.group(2);

                    // Eliminar saltos de línea y espacios en blanco adicionales
                    ListaColuma = ListaColuma.replaceAll("\\s+", " ").trim();

                    // Dividir la lista de columnas en columnas individuales
                    String[] columnas = ListaColuma.split(",");

                    // Crear el archivo CSV en el directorio actual
                    String currentDirectory = System.getProperty("user.dir", directorio);
                    String csvFilePath = currentDirectory + File.separator + nombreTabla + ".csv";

                    try (FileWriter writer = new FileWriter(csvFilePath)) {
                        // Escribir encabezados de columna en el archivo CSV
                        for (int i = 0; i < columnas.length; i++) {
                            String[] PartesColumna = columnas[i].trim().split(" ");
                            String nombreCOlumna = PartesColumna[0];
                            writer.append(nombreCOlumna);

                            if (i < columnas.length - 1) {
                                writer.append(",");
                            } else {
                                writer.append("\n");
                            }
                        }
                        // Cerrar el archivo
                        System.out.println("Tabla creada correctament en la ruta: " + csvFilePath);

                    } catch (IOException e) {
                        System.out.println("Error al crear la tabla: " + e.getMessage());
                    }
                } else {
                    System.out.println("El comando SQL no tiene el formato correctoo.");
                }
            } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

