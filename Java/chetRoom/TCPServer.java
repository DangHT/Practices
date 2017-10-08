package chetRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This is chetRoom 2.4.
 * This version supports multiple clients.
 * The server provides a thread for each client
 *
 * @author dht925nerd@126.com
 * @date 2017/10/8
 * @version 2.4
 */
public class TCPServer extends ServerSocket {
    private static final int SERVER_PORT = 6789;
    private List<String> userList = new ArrayList();
    private String[] response = {
            "OK!",
            "Got it!",
            "Roger that!"
    };

    public TCPServer() throws IOException {
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

    //TCPServer Thread
    class CreateServerThread extends Thread {
        private Socket client;
        private BufferedReader inFromClient;
        private PrintWriter outToClient;
        private String name;
        private Random resp_num = new Random();

        public CreateServerThread(Socket s)throws IOException {
            client = s;

            inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outToClient = new PrintWriter(client.getOutputStream(),true);

            name = inFromClient.readLine();
            userList.add(name);
            if (name == null
                    || name.isEmpty()
                    || name.trim().isEmpty())
                name = client.getInetAddress().getHostName();
            System.out.println("TCPClient(" + name +") come in...");
            outToClient.println("Hello, " + name + "! " + "We can talk now!");

            start();
        }

        public void run() {
            try {
                String line = "";

                /*
                   If the client enters "bye", the link is broken,
                   and returns information containing "bye" to the client
                 */
                while (true) {
                    line = inFromClient.readLine();
                    System.out.println("Client(" + name +") : " + line);
                    if (line.equals("bye")) break;
                    if (line.equals("showusers")) {
                        System.out.println(this.ShowUsers());
                        outToClient.println(this.ShowUsers());
                    }
                    else {
                        int s = resp_num.nextInt(response.length);
                        outToClient.println(response[s] + " " + line);
                    }
                }
                outToClient.println("bye, " + name +"!");
                System.out.println("Client(" + name +") exit!");
                userList.remove(name);

                outToClient.close();
                inFromClient.close();
                client.close();
            }catch (IOException e) {
            }
        }

        public String ShowUsers() {
            String s = "--------Online Users--------\n";
            for (String name : userList) {
                s += name + "\n";
            }
            s += "----------------------------\n";
            return s;
        }
    }

    public static void main(String[] args)throws IOException {
        new TCPServer();
    }
}
