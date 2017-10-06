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
public class TCPClient {
    public static void main(String[] args) {
        try {
            Socket client = new Socket("localhost", 6789);
            client.setSoTimeout(30000);

            PrintWriter outToServer = new PrintWriter(client.getOutputStream(), true);
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(client.getInputStream()));

            /*
               If the information returned from the server contains "bye", the link is broken
             */
            String sentence = "";
            while (sentence.indexOf("bye") == -1) {
                BufferedReader sysBuff = new BufferedReader(new InputStreamReader(System.in));
                outToServer.println(sysBuff.readLine());
                outToServer.flush();

                sentence = inFromUser.readLine();
                System.out.println("Server say : " + sentence);
            }

            outToServer.close();
            inFromUser.close();
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
