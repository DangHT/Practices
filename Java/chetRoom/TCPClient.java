package chetRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
public class TCPClient extends Socket{

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 6789;
    private String name;
    PrintWriter outToServer = new PrintWriter(getOutputStream(), true);
    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(getInputStream()));
    BufferedReader sysBuff = new BufferedReader(new InputStreamReader(System.in));

    public TCPClient() throws IOException {
        super(SERVER_HOST, SERVER_PORT);
        System.out.println("Please enter your name:");
        name = sysBuff.readLine();
        outToServer.println(name);
        System.out.println(inFromServer.readLine());
        setSoTimeout(30000);
    }

    public void ConnectToServer() throws IOException {
        /*
           If the response from the server contains "bye", the link is broken
         */
        String serverResponse = "";
        while (serverResponse.indexOf("bye") == -1) {
            outToServer.println(sysBuff.readLine());
            outToServer.flush();

            serverResponse = inFromServer.readLine();
            System.out.println("Server : " + serverResponse);
        }

        outToServer.close();
        inFromServer.close();
        close();
    }

    public static void main(String[] args) {
        try {
            TCPClient TCPClient = new TCPClient();
            TCPClient.ConnectToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
