package com.example.chatsito;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CServidorArchivo {
    public final static int PUERTO = 13268;  // Puerto de conexión

    public void enviarArchivo(File archivoAEnviar) throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket skServidor = null;
        Socket sk = null;

        try {
            skServidor = new ServerSocket(PUERTO);
            while (true) {
                System.out.println("Esperando conexión...");
                try {
                    sk = skServidor.accept();
                    System.out.println("Conexión aceptada : " + sk);
                    // Se envía el archivo seleccionado
                    byte[] ab = new byte[(int) archivoAEnviar.length()]; // Se crea un array de bytes
                    fis = new FileInputStream(archivoAEnviar);
                    bis = new BufferedInputStream(fis);
                    bis.read(ab, 0, ab.length);
                    os = sk.getOutputStream();
                    System.out.println("Enviando " + archivoAEnviar.getName() + "(" + ab.length + " bytes)");
                    os.write(ab, 0, ab.length);
                    os.flush();
                    System.out.println("Enviado.");
                } finally {
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (sk != null) sk.close();
                }
            }
        } finally {
            if (skServidor != null) skServidor.close();
        }
    }

    // Método main para pruebas
    public static void main(String[] args) {
        // Puedes agregar aquí la lógica para probar el envío de archivos si lo deseas
    }
}
