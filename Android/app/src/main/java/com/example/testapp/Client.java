package com.example.testapp;//

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client implements Runnable {
    private DatagramSocket socket; // socket to connect to server
    private ExecutorService executor = Executors.newCachedThreadPool();
    private String message = "";
    private String response = "";
    private volatile boolean done = false;

    // set up GUI and DatagramSocket
    public Client() {
        try // create and send packet
        {
            socket = new DatagramSocket();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public String send(String message){
        this.message = message;
        executor.execute(this);
        while (!done){}
        done = false;
        return response;
    }

    private void sendPacket(){
        try{
            byte[] data = message.getBytes(); // convert to bytes

            // create sendPacket
            DatagramPacket sendPacket = new DatagramPacket(data,
                    data.length, InetAddress.getByName("10.0.2.2"), 4044);

            socket.send(sendPacket); // send packet
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // wait for packets to arrive from Server, display packet contents
    private String receivePacket() {
        byte[] data = new byte[100]; // set up packet
        DatagramPacket receivePacket = new DatagramPacket(
                data, data.length);
        try {// receive packet and display contents

            socket.receive(receivePacket); // wait for packet

            // display packet contents
            System.out.println("\nPacket received:" +
                    "\nFrom host: " + receivePacket.getAddress() +
                    "\nHost port: " + receivePacket.getPort() +
                    "\nLength: " + receivePacket.getLength() +
                    "\nContaining:\n\t" + new String(receivePacket.getData(),
                    0, receivePacket.getLength()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return new String(receivePacket.getData(), 0, receivePacket.getLength());
    }

    @Override
    public void run(){
        sendPacket();
        this.response = receivePacket();
        done = true;
    }
}
