package com.example.testapp;

import java.io.BufferedWriter;
import java.util.concurrent.ArrayBlockingQueue;

public class ServerSender implements Runnable{
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