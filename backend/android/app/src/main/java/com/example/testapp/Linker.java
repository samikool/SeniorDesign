package com.example.testapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Linker implements Runnable, Serializable {
    private static LinkedBlockingQueue<String> q;
    private static ArrayList<String> todoList;
    private static ArrayList<Item> drinkItems;
    private static ArrayList<Item> bbqItems;
    private static ArrayList<Item> sideItems;
    private static Connection connection;
    private static int id;
    private static boolean isWaiter;
    private static Receipt receipt;

    //going to pass in anything structure that can be updated over network
    public Linker(int id, boolean isWaiter, ArrayList<String> todoList){
        this.todoList = todoList;
        drinkItems = new ArrayList<>();
        bbqItems = new ArrayList<>();
        sideItems = new ArrayList<>();
        q = new LinkedBlockingQueue<>();

        connection = new Connection("10.0.2.2", 4044, q);
        connection.start();

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(this);

        initializeItems();

        Linker.id = id;
        Linker.isWaiter = isWaiter;

        if(isWaiter){
            registerWaiter();
        }else{
            registerTable();
        }
    }

    public static ArrayList<Item> getBbqItems() {
        return bbqItems;
    }

    public static ArrayList<Item> getDrinkItems() {
        return drinkItems;
    }

    public static ArrayList<Item> getSideItems() {
        return sideItems;
    }

    public static ArrayList<String> getTodoList(){
        return todoList;
    }

    private void processItems(ArrayList<String> data){
        if (data.get(0).equals("drink")){
            drinkItems.clear();
            fillItemList(data, drinkItems);
        }
        else if((data.get(0).equals("side"))){
            sideItems.clear();
            fillItemList(data, sideItems);
        }
        else if(data.get(0).equals("bbq")){
            bbqItems.clear();
            fillItemList(data, bbqItems);
        }
    }

    private void fillItemList(ArrayList<String> data, ArrayList<Item> itemList){
        for(int i=1; i<data.size(); i+=3){
            Item item = new Item(Integer.parseInt(data.get(i)), data.get(i+1), Double.parseDouble(data.get(i+2)));
            itemList.add(item);
        }
        System.out.println(itemList);
    }

    public static void orderBBQ(int iid, int quant){
        iid += 1;
        sendMessage("order,bbq,"+iid+","+quant);

        receipt.addItem(bbqItems.get(iid), quant);
    }

    public static void orderDrink(int iid, int quant){
        iid += 1;
        sendMessage("order,drink,"+iid+","+quant);

        receipt.addItem(drinkItems.get(iid), quant);
    }

    public static void orderSide(int iid, int quant){
        iid += 1;
        sendMessage("order,side,"+iid+","+quant);

        receipt.addItem(sideItems.get(iid), quant);
    }

    private static void initializeItems(){
        sendMessage("items");
    }

    private static void registerTable(){
        connection.sendData("register,table,"+id);
    }

    private static void registerWaiter(){
        connection.sendData("register,waiter,"+id);
    }

    public static void call(){
        connection.sendData("call");
    }

    public static void claim(int tid){
        connection.sendData("claim,"+tid);
    }

    public static void sendMessage(String message){
        connection.sendData(message);
    }

    @Override
    public void run() {
        while(true){
            if(!q.isEmpty()){
                //get message
                String request = q.poll();
                String[] parts = request.split(",");

                int tid = Integer.parseInt(parts[0]);
                String command = parts[1];

                ArrayList<String> data = new ArrayList<>();

                if(parts.length > 2){
                    for (int i = 2; i < parts.length; i++) {
                        data.add(parts[i]);
                    }
                }
                //general commands
                if(command.equals("items")){
                    processItems(data);
                }
                //...
                //waiter commands
                else if(isWaiter){

                }
                //table commands
                else{

                }

                System.out.println("Table ID: " + tid + " || Request: " + command + " || Data: " + data);
                todoList.add("Table ID: " + tid + " || Request: " + command + " || Data: " + data);

            }
        }
    }
}
