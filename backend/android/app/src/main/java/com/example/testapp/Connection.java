package com.example.testapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.DataFormatException;

public class Connection implements Runnable {
    private String ip;
    private int port;
    private Socket socket;
    private BufferedWriter output;
    private BufferedReader input;
    private boolean done = false;
    private volatile boolean connected = false;
    ExecutorService executor = Executors.newCachedThreadPool();
    ServerSender sender;
    ServerRequester requester;

    public Connection(String ip, int port) throws IOException{
        this.ip = ip;
        this.port = port;
    }

    public void start(){
        executor.execute(this);
        try{
            executor.awaitTermination(1, TimeUnit.SECONDS);
        }catch (Exception e){
            System.out.println(e);
        }
        sender = new ServerSender(this, output);
        requester = new ServerRequester(this, input);
    }

    public void connect() {
        try{
            this.socket = new Socket(InetAddress.getByName(ip),port);
        }catch (Exception e){
            e.printStackTrace();
            System.err.println(e);
        }
    }

    public void initializeStreams(){
        try{
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            output.flush();

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Buffers successfully setup");
        }catch (IOException e){
            System.err.println("Error initializing buffers...");
            System.err.println(e);
        }
    }

    public void processConnection(){
        connected = true;
        while(!done){


        }
    }

    public void closeConnection(){
        System.out.println("Attempting to close connection with server");
        try{
            socket.close();
            input.close();
            output.close();
            System.out.println("Connection connection closed" );

        }catch (IOException e){
            System.err.println("Error closing client connection");
            System.err.println(e);
        }
    }

    public void sendData(String data){
        sender.addData(data);
        executor.execute(sender);
    }

    public String receiveData(){
        try{
            executor.execute(requester);
            executor.awaitTermination(5, TimeUnit.SECONDS);
            return requester.getData();
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    //Runnable interface
    @Override
    public void run() {
        try{
            connect();
            initializeStreams();
            processConnection();
        }catch (Exception e){
            System.err.println(e);
        }
    }

    public static void main(String[] args){
        try{
            Connection connection = new Connection("172.17.52.78", 4044);
            connection.connect();
            connection.initializeStreams();
            connection.closeConnection();
        }catch (Exception e){
            System.err.println(e);
        }

    }
}
