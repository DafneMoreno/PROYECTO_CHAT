package com.example.chatsito.PRUEBA1;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class CClienteArchivo {
    public final static int PUERTO = 13268;
    public final static String NOMBRE_SERVIDOR = "192.168.100.42"; // Cambia esto con la IP o nombre del servidor

    public static void main(String[] args) throws IOException {
        int bytesLeidos;
        int actual = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket skCliente = null;

        try {
            skCliente = new Socket(NOMBRE_SERVIDOR, PUERTO);
            System.out.println("Conectando al servidor...");

            byte[] ab = new byte[6022386]; // Tamaño del archivo

            // Manejo de mensajes recibidos
            InputStream is = skCliente.getInputStream();
            Scanner scanner = new Scanner(is);

            // Manejo de archivos recibidos
            fos = new FileOutputStream("archivorecibido.pdf");
            bos = new BufferedOutputStream(fos);

            while (true) {
                if (is.available() > 0) {
                    bytesLeidos = is.read(ab, actual, (ab.length - actual));
                    if (bytesLeidos >= 0) {
                        actual += bytesLeidos;
                    }
                }

                if (actual > 0) {
                    bos.write(ab, 0, actual);
                    bos.flush();
                    System.out.println("Archivo recibido (" + actual + " bytes leidos)");
                    actual = 0;
                }

                // Manejo de mensajes
                if (scanner.hasNextLine()) {
                    String mensajeRecibido = scanner.nextLine();
                    System.out.println("Mensaje recibido: " + mensajeRecibido);
                }
            }
        } finally {
            if (fos != null) fos.close();
            if (bos != null) bos.close();
            if (skCliente != null) skCliente.close();
        }
    }
}
