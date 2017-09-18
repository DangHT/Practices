import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * UDPClient demo
 * @author DHT <dht925nerd@126.com>
 * @version 2017/9/18.
 */
public class UDPClient {
    public static void main(String[] args) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket();
        BufferedReader inFromUser =
                new BufferedReader(
                        new InputStreamReader(System.in)
                );
        InetAddress IPAddress = InetAddress.getLocalHost();
        byte[] sendData;
        byte[] receiveData = new byte[1024];
        System.out.println("Please enter a string, and the server will return its uppercase（enter \"exit\" to exit）");
        while (true) {
            String sentence = inFromUser.readLine();
            if (sentence.equals("exit")) break;
            sendData = sentence.getBytes();
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket =
                    new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData());
            System.out.println("FROM SERVER: " + modifiedSentence);
        }
        clientSocket.close();
    }
}
