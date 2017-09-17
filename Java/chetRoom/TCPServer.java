package chetRoom;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This program is to simulate a chetRoom.
 * This is version 1.0 and base on Client / Server mode
 * I let TCPClient pretend client_1 and TCPServer pretend client_2
 * The problem is the conversation must start with client_2 and end with client_1
 * This is Client_2
 * @author 党昊天 <dht925nerd@126.com>
 * @version 1.0
 */
public class TCPServer {
    public static void main(String[] args) throws Exception {
        String clientSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(6789);
        while(true) {
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            clientSentence = inFromClient.readLine();
            System.out.println("FROM CLIENT: " + clientSentence);
            capitalizedSentence = inFromUser.readLine() + '\n';
            outToClient.writeBytes(capitalizedSentence);
        }
    }
}
