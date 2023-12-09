package com.example.chatsito.BD_CHAT.SENTENCIAS;

import com.example.chatsito.BD_CHAT.SENTENCIAS.NUMERICAS.*;
import com.example.chatsito.BD_CHAT.SENTENCIAS.SELECT2.*;
import com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Funciones {
    private static String currentdirectorio;

    public static void main(String directorio) {

        currentdirectorio = directorio;

        System.setProperty("user.dir", currentdirectorio);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String userInput = "";

            while (true) {
                    System.out.println("\n   OPCIONES: \nUCASE\nFunciones numéricas: DIV / - + * % MOD FLOOR ROUND RAND() \nCOUNT \nDISTINCT  \nMIN  \nMAX  \nSUMA \nAVG");
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



                //AQUI CADA UNO HARA LO QUE SEA PARA QUE VALIDE QUE TIENE LA ESTRUCTURA CORRECTA SU SENTENCIA

                if (userInput.matches("UCASE")) {
                    //SELECT `movie_id`,` title`, UCASE (`title`) FROM` movies`; ASI DEBE SER LA ESTRUCTURA
                    UCASE.main(directorio, userInput);
                } else if(primeraPalabra.equalsIgnoreCase("CREATE")) {
                    CREATE.main(directorio, userInput);
                }
                else if(primeraPalabra.equalsIgnoreCase("DISTINCT ")) {
                    //SELECT DISTINCT `movie_id` FROM` movierentals`;
                    DISTINCT.main(directorio, userInput);
                }
                else if(primeraPalabra.equalsIgnoreCase("COUNT")) {
                    //SELECT COUNT (`movie_id`) FROM` movierentals` WHERE `movie_id` = 2;
                    COUNT.main(directorio, userInput);
                }else if(primeraPalabra.equalsIgnoreCase("MIN ")) {
                    //SELECT MIN (`year_released`) FROM` movies`;
                    DISTINCT.main(directorio, userInput);
                }else if(primeraPalabra.equalsIgnoreCase("MAX ")) {
                    //SELECT MAX (`year_released`) FROM` movies`;
                    MAX.main(directorio, userInput);
                }else if(primeraPalabra.equalsIgnoreCase("SUMA ")) {
                    //SELECT SUM(`amount_paid`) FROM` payments`;
                    MIN.main(directorio, userInput);
                }else if(primeraPalabra.equalsIgnoreCase("AVG ")) {
                    //SELECT AVG(`amount_paid`) FROM` payments`;
                    AVG.main(directorio, userInput);
                }else   if (matcherDIV.find()) {
                    System.out.println("---ENTRO en DIV");
                    DIV.main(directorio, userInput);
                }else
                if (matcherSUS.find()) {
                    System.out.println("---ENTRO EN -");
                    SUSTRACCION.main(directorio, userInput);
                }else
                if (matcherADICION.find()) {
                    System.out.println("---ENTRO EN +");
                    ADICION.main(directorio, userInput);
                }else
                if (matcherMULTIPLICACION.find()) {
                    System.out.println("---ENTRO EN *");
                    MULTIPLICACION.main(directorio, userInput);
                }else
                if (matcherPOR.find()||matcherMOD.find()) {
                    System.out.println("---ENTRO EN %");
                    MODULO.main(directorio, userInput);
                }else
                if (matcherFLOOR.find()) {
                    System.out.println("------ENTRO EN FLOOR ");
                    FLOOR.main(directorio, userInput);
                }
                else
                if (matcherROUND.find()) {
                    System.out.println("---ENTRO EN ROUND ");
                    ROUND.main(directorio, userInput);
                }
                else
                if (matcherRAND.find()) {
                    System.out.println("---ENTRO EN RAND");
                    RAND.main(directorio, userInput);
                }else
                if (matcherDIVISION.find()) {
                    System.out.println("---ENTRO EN /");
                    DIVISION.main(directorio, userInput);
                }
                else if (userInput.equalsIgnoreCase("exit")) {
                            break;
                } else {
                            System.out.println("Comando no reconocido.");
                        }
                    }
                } catch(IOException e){
                    System.out.println("Error al leer la entrada del usuario: " + e.getMessage());
                }
            }
        }