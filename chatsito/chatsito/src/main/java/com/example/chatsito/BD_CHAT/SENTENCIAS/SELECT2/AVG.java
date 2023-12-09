package com.example.chatsito.BD_CHAT.SENTENCIAS.SELECT2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AVG {

public static void main(String directorio, String sentencia) {

    ArrayList<Double> valores = new ArrayList<>();

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    String nombreTabla = extractTableName(sentencia);

    directorio = directorio + "/" + nombreTabla + ".csv";

    String columnas = sentencia.trim();
    String[] col = columnas.split("\\s+");

    String regex = "SELECT\\s+AVG\\s*\\(\\s*[`'']?\\w+[`'']?\\s*\\)\\s*FROM\\s*[`'']?\\w+[`'']?";

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(columnas.toUpperCase());

    String nombreColumna = extractColumnName(columnas);

    if (matcher.matches()) {

        try (BufferedReader br = new BufferedReader(new FileReader(directorio))) {
            String linea;
            boolean primeraLinea = true; // Para omitir la primera línea (encabezados)
            int columnIndex = -1; // Índice de la columna a mostrar

            System.out.println(nombreColumna);
            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    String[] encabezados = linea.split(",");
                    for (int i = 0; i < encabezados.length; i++) {
                        if (encabezados[i].trim().equalsIgnoreCase(nombreColumna)) {
                            columnIndex = i; // Encontrar el índice de la columna
                            break;
                        }
                    }
                    if (columnIndex == -1) {
                        System.out.println("La columna especificada no fue encontrada en el archivo CSV.");
                        return;
                    }
                    continue; // Omitir la primera línea
                }

                String[] partes = linea.split(",");
                if (partes.length > columnIndex) {
                    String valorColumna = partes[columnIndex].trim();
                    try{
                        valores.add(Double.parseDouble(valorColumna));
                    } catch (NumberFormatException e){
                        System.out.println("No es un número");
                    }

                }
            }

            System.out.println("Promedio: " + calcularPromedio(valores));

        } catch (IOException e) {
            e.printStackTrace();
        }

    } else {
        System.out.println("La cadena no cumple con el formato deseado.");
    }

}

    private static String extractTableName(String sentenciaSQL) {
        int indexFrom = sentenciaSQL.toUpperCase().indexOf("FROM");
        if (indexFrom != -1) {
            // Añadir la longitud de "FROM" para omitir la palabra clave
            String nombreTabla = sentenciaSQL.substring(indexFrom + 4).trim();
            // Eliminar comillas si están presentes
            nombreTabla = nombreTabla.replaceAll("[`'\"]", "");
            return nombreTabla;
        } else {
            return null; // "FROM" no encontrado en la sentencia SQL
        }
    }

    public static double calcularPromedio(ArrayList<Double> numeros) {
        if (numeros.isEmpty()) {
            return 0.0; // Si la lista está vacía, el promedio es 0.0
        }

        double suma = 0.0;
        for (Double numero : numeros) {
            suma += numero;
        }

        return suma / numeros.size();
    }

    private static String extractColumnName(String sentenciaSQL) {

        String regex = "\\((.*?)\\)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sentenciaSQL);

        String textoEncontrado = "";
        while (matcher.find()) {
            textoEncontrado = matcher.group(1);
        }

        return textoEncontrado.replaceAll("[`'\"]", "");
    }


}