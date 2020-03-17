package com.example.testapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Connection implements Runnable {
    private String ip;
    private int port;
    private Socket socket;
    private BufferedWriter output;
    private BufferedReader input;
    private boolean done = false;
    private volatile boolean connected = false;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private ServerSender sender;
    private ServerRequester requester;
    private Linker linker;

    public Connection(String ip, int port, Linker linker) throws IOException {
        this.ip = ip;
        this.port = port;
        this.linker = linker;
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
            try{
                String message = input.readLine();
                if(message != null){
                    System.out.println(message);
                    linker.addMessage(message);
                }
            }catch (IOException e){
                System.err.println(e);
            }

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

    private class ServerRequester implements Runnable {
        private Connection connection;
        private BufferedReader input;
        private String data;

        public ServerRequester(Connection connection, BufferedReader input){
            this.connection = connection;
            this.input = input;
            this.data = null;

        }

        public String getData(){
            String returnData = data;
            this.data = null;
            return returnData;
        }

        @Override
        public void run() {
            try{
                this.data = input.readLine();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class ServerSender implements Runnable {
        private Connection connection;
        private BufferedWriter output;
        private ArrayBlockingQueue<String> dataArray = new ArrayBlockingQueue<>(128);

        public ServerSender(Connection connection, BufferedWriter output){
            this.connection = connection;
            this.output = output;
        }

        public void addData(String data){
            dataArray.add(data);
        }

        @Override
        public void run() {
            while(!dataArray.isEmpty()){
                try{
                    String data = dataArray.poll();
                    output.write(data+"\n");
                    output.flush();
                    System.out.println(data + " was sent...");
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        }
    }
}
