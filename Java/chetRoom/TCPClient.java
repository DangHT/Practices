package chetRoom;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * This program is to simulate a chetRoom.
 * This is version 1.0 and base on Client / Server mode
 * I let TCPClient pretend client_1 and TCPServer pretend client_2
 * The problem is the conversation must start with client_2 and end with client_1
 * This is Client_1
 * @author 党昊天 <dht925nerd@126.com>
 * @version 1.0
 */
public class TCPClient {
    public static void main(String[] args) throws Exception {
        String sentence;
        String modifiedSentence;
        while (true) {
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            Socket clientSocket = new Socket("localhost", 6789);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            sentence = inFromUser.readLine();
            if (sentence.equals("exit")) break;
            outToServer.writeBytes(sentence + '\n');
            modifiedSentence = inFromServer.readLine();
            System.out.println("FROM SERVER: " + modifiedSentence);
            clientSocket.close();
        }
    }
}
