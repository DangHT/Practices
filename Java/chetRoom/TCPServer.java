package chetRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This is chetRoom 2.0.
 * This version supports multiple clients.
 * The server provides a thread for each client
 *
 * @author dht925nerd@126.com
 * @date 2017/10/6
 * @version 2.0
 */
public class Server extends ServerSocket {
    private static final int SERVER_PORT = 6789;

    public Server()throws IOException {
        super(SERVER_PORT);

        try {
            while (true) {
                Socket socket = accept();
                //When there is a request, start a thread
                new CreateServerThread(socket);
            }
        }catch (IOException e) {
        }finally {
            close();
        }
    }

    //Server Thread
    class CreateServerThread extends Thread {
        private Socket client;
        private BufferedReader bufferedReader;
        private PrintWriter printWriter;

        public CreateServerThread(Socket s)throws IOException {
            client = s;

            bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            printWriter = new PrintWriter(client.getOutputStream(),true);
            System.out.println("Client(" + getName() +") come in...");

            start();
        }

        public void run() {
            try {
                String line = bufferedReader.readLine();

                /*
                   If the client enters "bye", the link is broken,
                   and returns information containing "bye" to the client
                 */
                while (!line.equals("bye")) {
                    printWriter.println("continue, Client(" + getName() +")!");
                    line = bufferedReader.readLine();
                    System.out.println("Client(" + getName() +") say: " + line);
                }
                printWriter.println("bye, Client(" + getName() +")!");

                System.out.println("Client(" + getName() +") exit!");
                printWriter.close();
                bufferedReader.close();
                client.close();
            }catch (IOException e) {
            }
        }
    }

    public static void main(String[] args)throws IOException {
        new Server();
    }
}
