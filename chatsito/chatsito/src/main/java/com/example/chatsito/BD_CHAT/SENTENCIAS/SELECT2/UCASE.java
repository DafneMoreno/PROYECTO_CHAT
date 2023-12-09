package com.example.chatsito.BD_CHAT.SENTENCIAS.SELECT2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UCASE {
    public static void main(String directorio, String sentencia) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        ArrayList<Double> valores = new ArrayList<>();
        ArrayList<Integer> indexs = new ArrayList<>();

        String nombreTabla = extractTableName(sentencia);

        directorio = directorio + "/" + nombreTabla + ".csv";

        String columnas = sentencia.trim();
        String[] col = columnas.split("\\s+");

        //String regex = "SELECT\\s+(?:UCASE\\s*\\(\\s*[`'\"]?\\w+[`'\"]?\\s*\\))(?:\\s*,\\s*(?:UCASE\\s*\\(\\s*[`'\"]?\\w+[`'\"]?\\s*\\)))?\\s*FROM\\s*[`'\"]?\\w+[`'\"]?;|SELECT\\s*[`'\"]?\\w+[`'\"]?(?:\\s*,\\s*[`'\"]?\\w+[`'\"]?)*\\s*FROM\\s*[`'\"]?\\w+[`'\"]?;";

        if (sentencia.toUpperCase().contains("SELECT") && sentencia.toUpperCase().contains("FROM")) {

            ArrayList<String> columnasUCASE = findColumnsInUCASE(columnas.toUpperCase());

            // Extraer los nombres de las columnas de la sentencia SQL
            List<String> nombresColumnas = extractColumnNames(columnas.toUpperCase());

            for(String co : nombresColumnas){
                if(columnasUCASE.contains(co.trim())){
                    System.out.print("upper_case_" + co + " ");
                    indexs.add(nombresColumnas.indexOf(co));
                } else {
                    System.out.print(co + " ");
                }
            }
            System.out.println();
            // Leer el archivo CSV y mostrar solo las columnas especificadas
            try (BufferedReader br = new BufferedReader(new FileReader(directorio))) {
                String linea;
                boolean primeraLinea = true; // Para omitir la primera línea (encabezados)
                int[] columnIndexes = new int[nombresColumnas.size()]; // Índices de las columnas a mostrar

                while ((linea = br.readLine()) != null) {
                    if (primeraLinea) {
                        primeraLinea = false;
                        String[] encabezados = linea.split(",");

                        // Encontrar los índices de las columnas a mostrar
                        for (int i = 0; i < nombresColumnas.size(); i++) {
                            columnIndexes[i] = -1; // Inicializar a -1 en caso de no encontrar la columna
                            for (int j = 0; j < encabezados.length; j++) {
                                if (encabezados[j].trim().equalsIgnoreCase(nombresColumnas.get(i))) {
                                    columnIndexes[i] = j; // Encontrar el índice de la columna
                                    break;
                                }
                            }

                            // Verificar si la columna especificada se encontró en los encabezados
                            if (columnIndexes[i] == -1) {
                                System.out.println("La columna especificada '" + nombresColumnas.get(i) + "' no fue encontrada en el archivo CSV.");
                                return;
                            }
                        }

                        continue; // Omitir la primera línea
                    }

                    String[] partes = linea.split(",");
                    for (int columnIndex : columnIndexes) {
                        String valorColumna = partes[columnIndex].trim();
                        if(indexs.contains(columnIndex-1)){
                            System.out.print(valorColumna.toUpperCase() + " ");
                        } else {
                            System.out.print(valorColumna + " ");
                        }

                    }
                    System.out.println();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no match");
        }


    }

        private static ArrayList<String> findColumnsInUCASE(String sentenciaSQL) {
            ArrayList<String> columnasUCASE = new ArrayList<>();
            String regex = "UCASE\\s*\\(\\s*[`'\"]?(\\w+)[`'\"]?\\s*\\)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(sentenciaSQL);

            while (matcher.find()) {
                columnasUCASE.add(matcher.group(1));
            }

            return columnasUCASE;
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


        // Método para extraer los nombres de las columnas de una sentencia SQL
        private static List<String> extractColumnNames(String sentenciaSQL) {
            List<String> nombresColumnas = new ArrayList<>();
            int startIndex = sentenciaSQL.indexOf("SELECT") + 6;
            int endIndex = sentenciaSQL.indexOf("FROM");
            String columnNames = sentenciaSQL.substring(startIndex, endIndex).trim();

            String[] partes = columnNames.split(",");
            for (String parte : partes) {
                nombresColumnas.add(obtenerContenidoEntreParentesis(parte.replaceAll("[\"'`]", "").trim()));

            }

            return nombresColumnas;
        }

    private static String obtenerContenidoEntreParentesis(String input) {
        int inicio = input.indexOf('(');
        int fin = input.indexOf(')');

        if (inicio != -1 && fin != -1 && inicio < fin) {
            return input.substring(inicio + 1, fin).trim();
        } else {
            return input;
        }
    }
}
