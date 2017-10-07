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
public class TCPServer extends ServerSocket {
    private static final int SERVER_PORT = 6789;
    private String[] response = {
            "OK!",
            "Got it!",
            "Roger that!"
    };

    public Server() throws IOException {
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
        private String name;
        private Random resp_num = new Random();

        public CreateServerThread(Socket s)throws IOException {
            client = s;
            name = client.getInetAddress().getHostName();

            bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            printWriter = new PrintWriter(client.getOutputStream(),true);

            System.out.println("Client(" + name +") come in...");
            printWriter.println("Connection succeed!");

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
                    int s = resp_num.nextInt(response.length);
                    printWriter.println(response[s]);
                    line = bufferedReader.readLine();
                    System.out.println("Client(" + name +") : " + line);
                }
                printWriter.println("bye, " + name +"!");

                System.out.println("Client(" + name +") exit!");
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
