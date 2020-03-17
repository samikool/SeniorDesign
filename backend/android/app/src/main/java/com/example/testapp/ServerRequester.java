package com.example.testapp;

import java.io.BufferedReader;
import java.util.zip.DataFormatException;

public class ServerRequester implements Runnable{
    private Connection connection;
    private BufferedReader input;
    private String data;

    public boolean hasDataReady() {
        return dataReady;
    }

    private volatile boolean dataReady;

    public ServerRequester(Connection connection, BufferedReader input){
        this.connection = connection;
        this.input = input;
        this.data = null;
        dataReady = false;
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