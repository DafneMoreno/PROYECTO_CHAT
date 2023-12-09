package com.example.chatsito.BD_CHAT.SENTENCIAS;
import com.example.chatsito.BD_CHAT.SENTENCIAS.NUMERICAS.*;
import com.example.chatsito.BD_CHAT.SENTENCIAS.SELECT2.*;
import com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS.*;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SHOW {

    private String currentdirectorio;


    public void main(String directorio) {

        currentdirectorio = directorio;
        System.setProperty("user.dir", currentdirectorio);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String userInput = "";
            System.out.println("\n OPCIONES 1: \n"+"SHOW TABLES" +"\n"+
                    "CREATE TABLE" +"\n"+
                    "DROP TABLE" +"\n"+
                    "INSERT" +"\n"+
                    "DELETE" +"\n"+
                    "UPDATE" +"\n"+
                    "SELECT");
            System.out.println("\n   OPCIONES 2: " +
                    "\nUCASE" +
                    "\nFunciones numéricas: DIV / - + * % MOD FLOOR ROUND RAND() " +
                    "\nCOUNT " +
                    "\nDISTINCT  " +
                    "\nMIN  " +
                    "\nMAX  " +
                    "\nSUMA " +
                    "\nAVG");

            while (true) {


                StringBuilder userInputBuilder = new StringBuilder();
                char inputChar;
                while (true) {
                    inputChar = (char) reader.read();
                    if (inputChar == ';') {
                        break;  // Salir del bucle si se ingresa ';'
                    }
                    userInputBuilder.append(inputChar);
                }
                userInput = userInputBuilder.toString().trim();

                StringTokenizer tokens = new StringTokenizer(userInput);
                String primeraPalabra = tokens.nextToken();

                ////////////////////////////////////////////////
                ///BUSCAR LA PALABRA
                Pattern patronDIV = Pattern.compile("(?i)\\bDIV\\b");
                Matcher matcherDIV = patronDIV.matcher(userInput);
                Pattern patronDIVISION = Pattern.compile("(?i)/");
                Matcher matcherDIVISION = patronDIVISION.matcher(userInput);
                Pattern patronSUS = Pattern.compile("(?i)-");
                Matcher matcherSUS = patronSUS.matcher(userInput);
                Pattern patronADI = Pattern.compile("(?i)\\+");
                Matcher matcherADICION = patronADI.matcher(userInput);
                Pattern patronMULTIPLICACION = Pattern.compile("(?i)\\*");
                Matcher matcherMULTIPLICACION = patronMULTIPLICACION.matcher(userInput);
                Pattern patronPOR = Pattern.compile("(?i)%");
                Matcher matcherPOR = patronPOR.matcher(userInput);
                Pattern patronMOD = Pattern.compile("(?i)\\bMOD\\b");
                Matcher matcherMOD = patronMOD.matcher(userInput);
                Pattern patronFLOOR  = Pattern.compile("(?i)\\bFLOOR\\b");
                Matcher matcherFLOOR  = patronFLOOR.matcher(userInput);
                Pattern patronROUND = Pattern.compile("(?i)\\bROUND\\b");
                Matcher matcherROUND = patronROUND.matcher(userInput);
                Pattern patronRAND = Pattern.compile("(?i)\\bRAND\\b");
                Matcher matcherRAND = patronRAND.matcher(userInput);
                Pattern patronINNER = Pattern.compile("(?i)\\bINNER\\b");
                Matcher matcherINNER = patronINNER.matcher(userInput);
                Pattern patronLEFT = Pattern.compile("(?i)\\bLEFT\\b");
                Matcher matcherLEFT = patronLEFT.matcher(userInput);
                Pattern patronRIGHT = Pattern.compile("(?i)\\bRIGHT\\b");
                Matcher matcherRIGHT = patronRIGHT.matcher(userInput);
                Pattern patronOUTER = Pattern.compile("(?i)\\bOUTER\\b");
                Matcher matcherOUTER = patronOUTER.matcher(userInput);
                Pattern patronSELECT = Pattern.compile("(?i)\\bSELECT\\b");
                Matcher matcherSELECT = patronSELECT.matcher(userInput);


                if (userInput.matches("(?i)\\s*SHOW\\s+TABLES\\s*")) {
                    showTables(currentdirectorio);
                } else if(primeraPalabra.equalsIgnoreCase("CREATE")) {
                    CREATE.main(directorio, userInput);
                }else if(primeraPalabra.equalsIgnoreCase("DROP")) {
                    DROP.main(directorio, userInput);
                }else if(primeraPalabra.equalsIgnoreCase("INSERT")) {
                    INSERT.main(directorio, userInput);
                } else if(primeraPalabra.equalsIgnoreCase("DELETE")) {
                    DELETE2.main(directorio, userInput);
                    String patron = "(?i)^DELETE\\s+FROM\\s+(\\w+)\\s+WHERE\\s+(.+)";
                    // Verificar si el comando SQL coincide con el patrÃ³n
                    if (userInput.matches(patron)) {
                        // Obtiene el nombre de la tabla y la condiciÃ³n de la consulta
                        String nombreTabla = userInput.replaceAll(patron, "$1");
                        File tabla = new File(directorio, nombreTabla + ".csv");
                        File tempFile = new File(directorio, "temp.csv");
                        tabla.delete();
                        tempFile.renameTo(tabla);
                    }

                }else if(primeraPalabra.equalsIgnoreCase("UPDATE")) {
                    UPDATE2.main(directorio, userInput);
                   // System.out.println("salio");
                    String patron = "(?i)^UPDATE\\s+(\\w+)\\s+SET\\s+(.+)\\s+WHERE\\s+(.+)";
                    if (userInput.matches(patron)) {
                        String nombreTabla = userInput.replaceAll(patron, "$1");
                        File tabla = new File(directorio, nombreTabla + ".csv");
                        File tempFile = new File(directorio, "updateArch.csv");
                        tabla.delete();
                        tempFile.renameTo(tabla);
                    }
                }else if (esSelectAsterisco(userInput, "*")) {
                   // System.out.println("ENTRO EN SELECT * FROM");
                    SELECT.main(directorio, userInput);
                } else if(userInput.toUpperCase().contains("DISTINCT")) {
                    //SELECT DISTINCT `movie_id` FROM` movierentals`;
                    DISTINCT.main(directorio, userInput);
                }
                else if (userInput.toUpperCase().contains("UCASE")) {
                    //SELECT `movie_id`,` title`, UCASE (`title`) FROM` movies`; ASI DEBE SER LA ESTRUCTURA
                    UCASE.main(directorio, userInput);
                } else if(primeraPalabra.equalsIgnoreCase("CREATE")) {
                    CREATE.main(directorio, userInput);
                } else if (esSelectAsterisco(userInput, "COUNT")) {
                    //SELECT COUNT (`movie_id`) FROM` movierentals` WHERE `movie_id` = 2;
                    COUNT.main(directorio, userInput);
                } else if (esSelectAsterisco(userInput, "MIN")) {
                    //SELECT MIN (`year_released`) FROM` movies`;
                    MIN.main(directorio, userInput);
                } else if (esSelectAsterisco(userInput, "MAX")){
                    //SELECT MAX (`year_released`) FROM` movies`;
                    MAX.main(directorio, userInput);
                } else if (esSelectAsterisco(userInput, "SUM")){
                    //SELECT SUM(`amount_paid`) FROM` payments`;
                    SUMA.main(directorio, userInput);
                }
                else if(userInput.toUpperCase().contains("AVG")) {
                    //SELECT AVG(`amount_paid`) FROM` payments`;
                    AVG.main(directorio, userInput);
                }else   if (matcherDIV.find()) {
                   // System.out.println("---ENTRO en DIV");
                    DIV.main(directorio, userInput);
                }else    if (matcherSUS.find()) {
                   // //System.out.println("---ENTRO EN -");
                    SUSTRACCION.main(directorio, userInput);
                }else if (matcherADICION.find()) {
                    //System.out.println("---ENTRO EN +");
                    ADICION.main(directorio, userInput);
                }else if (matcherMULTIPLICACION.find()) {
                    //System.out.println("---ENTRO EN *");
                    MULTIPLICACION.main(directorio, userInput);
                }else if (matcherPOR.find()||matcherMOD.find()) {
                    //System.out.println("---ENTRO EN %");
                    MODULO.main(directorio, userInput);
                }else if (matcherFLOOR.find()) {
                    //System.out.println("------ENTRO EN FLOOR ");
                    FLOOR.main(directorio, userInput);
                } else if (matcherROUND.find()) {
                    //System.out.println("---ENTRO EN ROUND ");
                    ROUND.main(directorio, userInput);
                } else if (matcherRAND.find()) {
                    //System.out.println("---ENTRO EN RAND");
                    RAND.main(directorio, userInput);
                }else if (matcherDIVISION.find()) {
                    //System.out.println("---ENTRO EN /");
                    DIVISION.main(directorio, userInput);
                } else if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }else if(userInput.toUpperCase().contains("SELECT") && userInput.toUpperCase().contains("FROM")){
                    UCASE.main(directorio, userInput);
                }else {
                    System.out.println("Comando no reconocido.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer la entrada del usuario: " + e.getMessage());
        }
    }
    public static boolean esSelectAsterisco(String userInput, String segundoValor) {
        String[] pala = userInput.split("\\s+");
        if (pala.length >= 2) {
            String primeraPalabra = pala[0];
            String segundaPalabra = pala[1];
            return (primeraPalabra.equalsIgnoreCase("SELECT") && segundaPalabra.equals(segundoValor));
        }
        return false;
    }


    public void showTables(String currentdirectorio) {
        if (!currentdirectorio.isEmpty()) {
            File currentDir = new File(currentdirectorio);
            File[] files = currentDir.listFiles();
            if (files != null) {
                System.out.println("Tablas en la ruta de trabajo:");
                for (File table : files) {
                    if (table.isFile() && table.getName().toLowerCase().endsWith(".csv")) {
                        System.out.println(table.getName());
                    }
                }
            }
        } else {
            System.out.println("La ruta de trabajo no estÃ¡ establecida.");
        }
    }
}