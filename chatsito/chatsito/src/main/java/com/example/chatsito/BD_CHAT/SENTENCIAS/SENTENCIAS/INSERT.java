package com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS;
import java.io.*;
import java.util.Scanner;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class INSERT {
    public static void main(String directorio, String sentencia) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
          //  System.out.print("Ingrese el comando SQL INSERT: ");
         //   String sentencia = "";


                String patron = "(?i)^INSERT INTO\\s+(\\w+)\\s*\\(([^)]+)\\)\\s+VALUES\\s+\\(([^)]+)\\)";
                String columnas = sentencia.replaceAll(patron, "$2");
                columnas = columnas.replaceAll("\\s+", "").trim();

                if (sentencia.matches(patron)) {
                    String nombreTabla = sentencia.replaceAll(patron, "$1");// el numero es el grupo que se genero y que obtiee
                    //String columnas = sentencia.replaceAll(patron, "$2");
                    String valores = sentencia.replaceAll(patron, "$3");

                   // columnas = columnas.replaceAll("\\s+", "").trim();
                    valores= valores.replaceAll("\\s+", " ").trim();

                    File tabla = new File(directorio, nombreTabla + ".csv");
                    System.out.println(tabla);

                    if (tabla.exists()) {
                        // Verificar si las columnas coinciden con los encabezados
                        BufferedReader tablaReader = new BufferedReader(new FileReader(tabla));
                        String encabezados = tablaReader.readLine();
                        tablaReader.close();

                        if (encabezados.equals(columnas)) {
                            // Insertar datos en la tabla
                            try (FileWriter writer = new FileWriter(tabla, true)) {
                                writer.write(valores + "\n");
                                System.out.println("Datos insertados con éxito en la tabla '" + nombreTabla + "'.");
                            } catch (IOException e) {
                                System.out.println("Error al insertar datos en la tabla: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Las columnas especificadas en el comando SQL no coinciden con los encabezados de la tabla.");
                        }
                    } else {
                        System.out.println("La tabla '" + nombreTabla + "' no existe en el directorio.");
                    }
                } else {
                    System.out.println("El comando SQL no tiene el formato correcto.");

                }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
