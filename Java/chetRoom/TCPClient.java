package chetRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
public class TCPClient extends Socket {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 6789;
    PrintWriter outToServer = new PrintWriter(getOutputStream(), true);
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(getInputStream()));

    public Client() throws IOException {
        super(SERVER_HOST, SERVER_PORT);
        setSoTimeout(30000);
    }

    public void ConnectToServer() throws IOException {
        System.out.println("Please enter any words to connect:");
        /*
           If the response from the server contains "bye", the link is broken
         */
        String serverResponse = "";
        while (serverResponse.indexOf("bye") == -1) {
            BufferedReader sysBuff = new BufferedReader(new InputStreamReader(System.in));
            outToServer.println(sysBuff.readLine());
            outToServer.flush();

            serverResponse = inFromUser.readLine();
            System.out.println("Server : " + serverResponse);
        }

        outToServer.close();
        inFromUser.close();
        close();
    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.ConnectToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
