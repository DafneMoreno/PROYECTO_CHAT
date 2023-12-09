package com.example.chatsito.BD_CHAT.SENTENCIAS.SENTENCIAS;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class DROP {
    public static void main(String directorio, String sqlCommand) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String patron = "(?i)^DROP TABLE\\s+(\\w+)\\s*";
            if (sqlCommand.matches(patron)) {
                String nombreArchivo = sqlCommand.replaceAll(patron, "$1");
                File archivo = new File(directorio, nombreArchivo+".csv");

            if (archivo.exists()) {
                System.out.print("¿Realmente desea eliminar la tabla '" + nombreArchivo + "'? (si/no): ");
                String respuesta = reader.readLine().toLowerCase();


                if (respuesta.equals("si")||respuesta.equals("Si")||respuesta.equals("SI")) {
                    try {
                        if (archivo.delete()) {
                            System.out.println("La tabla ha sido eliminada con éxito.");
                        } else {
                            System.out.println("No se pudo eliminar la tabla.");
                        }
                    } catch (SecurityException e) {/*Se produce una SecurityException excepción cuando un autor de la llamada no tiene los permisos necesarios para acceder a un recurso. En el ejemplo siguiente se crea una instancia de un PermissionSet objeto que incluye un UIPermission objeto para permitir el acceso a objetos de interfaz de usuario y el Portapapeles y un RegistryPermission objeto para evitar el acceso al registro.*/
                        System.out.println("No tiene permisos para eliminar la tabla.");
                    } catch (Exception e) {
                        System.out.println("Se produjo un error al eliminar la tabla: " + e.getMessage());
                    }
                } else {
                    System.out.println("La eliminación de la tabla ha sido cancelada.");
                }
            } else {
                System.out.println("La tabla '" + nombreArchivo + "' no existe en el directorio.");
            }}else {
                    System.out.println("El comando SQL no tiene el formato correcto.");
                }
        } catch (IOException e) {
            System.out.println("Error al leer la entrada del usuario: " + e.getMessage());
        }
    }
}
