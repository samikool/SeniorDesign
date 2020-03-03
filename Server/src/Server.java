// Fig. 28.7: Server.java
// Server side of connectionless client/server computing with datagrams.

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {

    private DatagramSocket socket; // socket to connect to client

    // set up GUI and DatagramSocket
    public Server() {
        try // create DatagramSocket for sending and receiving packets
        {
            socket = new DatagramSocket(4044);
        } catch (SocketException socketException) {
            socketException.printStackTrace();
            System.exit(1);
        }
    }

    // wait for packets to arrive, display data and echo packet to client
    public void waitForPackets() {
        while (true) {
            try // receive packet, display contents, return copy to client
            {
                byte[] data = new byte[100]; // set up packet
                DatagramPacket receivePacket =
                        new DatagramPacket(data, data.length);

                socket.receive(receivePacket); // wait to receive packet

                // display information from received packet
                System.out.println("\nPacket received:" +
                        "\nFrom host: " + receivePacket.getAddress() +
                        "\nHost port: " + receivePacket.getPort() +
                        "\nLength: " + receivePacket.getLength() +
                        "\nContaining:\n\t" + new String(receivePacket.getData(),
                        0, receivePacket.getLength()));

                sendPacketToClient(receivePacket); // send packet to client
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // echo packet to client
    private void sendPacketToClient(DatagramPacket receivePacket) throws IOException {
        // create packet to send
        DatagramPacket sendPacket = new DatagramPacket(
                receivePacket.getData(), receivePacket.getLength(),
                receivePacket.getAddress(), receivePacket.getPort());

        socket.send(sendPacket); // send packet to client

    }
    public static void main(String[] args){
        Server server = new Server();
        server.waitForPackets();
    }
}
