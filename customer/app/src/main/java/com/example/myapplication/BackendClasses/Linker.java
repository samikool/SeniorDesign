package com.example.myapplication.BackendClasses;



import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
    private static HashMap<Integer, Receipt> receiptMap;
    private static HashMap<Integer, Integer> tableMap;
    public static View currentView;

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
            receiptMap = new HashMap<Integer, Receipt>();
            tableMap = new HashMap<Integer, Integer>();
        }else{
            registerTable();
        }
    }

    public static int getId(){
        return id;
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
        itemList.add(new Item()); //empty item so index matches iid
        for(int i=1; i<data.size(); i+=4){
            Item item = new Item(Integer.parseInt(data.get(i)), data.get(i+1), Double.parseDouble(data.get(i+2)), data.get(i+3));

            itemList.add(item);
        }
        System.out.println(itemList);
    }

    //used by tables
    public static void orderBBQ(int iid, int quant){
        sendMessage("order,bbq,"+iid+","+quant);
        receipt.addItem(bbqItems.get(iid), quant);
    }

    public static void orderDrink(int iid, int quant){
        sendMessage("order,drink,"+iid+","+quant);
        receipt.addItem(drinkItems.get(iid), quant);
    }

    public static void orderSide(int iid, int quant){
        sendMessage("order,side,"+iid+","+quant);
        receipt.addItem(sideItems.get(iid), quant);
    }

    //used by waiters to add to certain tables receipt
    public static void orderBBQ(int iid, int quant, int tid){
        sendMessage("order,"+tid+",bbq,"+iid+","+quant);
        receiptMap.get(tid).addItem(bbqItems.get(iid), quant);
    }

    public static void orderDrink(int iid, int quant, int tid){
        sendMessage("order,"+tid+",drink,"+iid+","+quant);
        receiptMap.get(tid).addItem(drinkItems.get(iid), quant);
    }

    public static void orderSide(int iid, int quant, int tid){
        sendMessage("order,"+tid+",side,"+iid+","+quant);
        receiptMap.get(tid).addItem(sideItems.get(iid), quant);
    }

    public static void requestUtensil(String utensil, int quant){
        sendMessage("chop,"+utensil+","+quant);
    }

    public static void voidBBq(int iid, int quant, int tid){
        sendMessage("void,"+tid+",bbq,"+iid+","+quant);
        receiptMap.get(tid).addItem(bbqItems.get(iid), quant);
    }

    public static void voidDrink(int iid, int quant, int tid){
        sendMessage("void,"+tid+",drink,"+iid+","+quant);
        receiptMap.get(tid).addItem(drinkItems.get(iid), quant);
    }

    public static void voidUtensil(int iid, int quant, int tid){
        sendMessage("void,"+tid+",side,"+iid+","+quant);
        receiptMap.get(tid).addItem(sideItems.get(iid), quant);
    }





    public static void requestCheck(){
        sendMessage("check");
    }


    private static void initializeItems(){
        sendMessage("items");
    }

    private static void registerTable(){
        sendMessage("register,table,"+id);
    }

    private static void registerWaiter(){
        sendMessage("register,waiter,"+id);
    }

    public static void call(){
        sendMessage("call");
    }

    public static void claim(int tid){
        receiptMap.put(tid, new Receipt(tid, id));
        tableMap.put(tid, id);
        sendMessage("claim,"+tid);
    }

    public static void closeTable(int tid){
        receiptMap.remove(tid);
        tableMap.remove(tid);
        sendMessage("close,"+tid);
    }

    public static void sendMessage(String message){
        connection.sendData(message);
    }

    public static void printReceipt(){
        if(isWaiter){
            for (Receipt receipt : receiptMap.values()) {
                if(receipt.getNumItems() > 0){
                    System.out.println(receipt);
                }
            }
        }else{
            System.out.println(receipt);
        }
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

                System.out.println("Table ID: " + tid + " || Request: " + command + " || Data: " + data);
                if(!command.equals("items")){
                    Snackbar.make(currentView, "Table ID: " + tid + " || Request: " + command + " || Data: " + data, Snackbar.LENGTH_LONG).show();
                }

                //general commands
                if(command.equals("items")){
                    processItems(data);
                }
                //...
                //command is for waiter
                else if(isWaiter){
                    if(command.equals("call")){
                        todoList.add("Table ID: " + tid + " || Request: " + command);
                    }
                    else if(command.equals("check")){
                        todoList.add("Table ID: " + tid + " || Request: " + command);
                    }
                    else if(command.equals("order")){
                        String category = "";
                        Item item;
                        int iid;
                        int quant;
                        for(int i=0; i<data.size(); i+=3){
                            category = data.get(i);
                            iid = Integer.parseInt(data.get(i+1));
                            quant = Integer.parseInt(data.get(i+2));
                            if(category.equals("drink")){
                                item = drinkItems.get(iid);
                                receiptMap.get(tid).addItem(item, quant);
                            }
                            else if(category.equals("bbq")){
                                item = bbqItems.get(iid);
                                receiptMap.get(tid).addItem(item, quant);
                            }
                            else if(category.equals("sides")){
                                item = sideItems.get(iid);
                                receiptMap.get(tid).addItem(item, quant);
                            }
                        }
                    }
                    else if(command.equals("chop")){
                        todoList.add("Table ID: " + tid + " || Request: " + command);
                    }
                    else if(command.equals("claim")){
                        int wid = Integer.parseInt(data.get(0));
                        tableMap.put(tid, wid);
                    }
                    else if(command.equals("close")){
                        tableMap.remove(tid);
                    }
                }
                //command is for table
                else{
                    if(command.equals("claim")){
                        int wid = Integer.parseInt(data.get(0));
                        receipt = new Receipt(id,wid);
                    }
                    else if(command.equals("order")){
                        String category = "";
                        Item item;
                        int iid;
                        int quant;
                        for(int i=0; i<data.size(); i+=3){
                            category = data.get(i);
                            iid = Integer.parseInt(data.get(i+1));
                            quant = Integer.parseInt(data.get(i+2));
                            if(category.equals("drink")){
                                item = drinkItems.get(iid);
                                receipt.addItem(item, quant);
                            }
                            else if(category.equals("bbq")){
                                item = bbqItems.get(iid);
                                receipt.addItem(item, quant);
                            }
                            else if(category.equals("sides")){
                                item = sideItems.get(iid);
                                receipt.addItem(item, quant);
                            }
                        }
                    }
                    else if(command.equals("void")){
                        String category = "";
                        Item item;
                        int iid;
                        int quant;
                        for(int i=0; i<data.size(); i+=3){
                            category = data.get(i);
                            iid = Integer.parseInt(data.get(i+1));
                            quant = Integer.parseInt(data.get(i+2));
                            if(category.equals("drink")){
                                item = drinkItems.get(iid);
                                receipt.removeItem(item, quant);
                            }
                            else if(category.equals("bbq")){
                                item = bbqItems.get(iid);
                                receipt.removeItem(item, quant);
                            }
                            else if(category.equals("sides")){
                                item = sideItems.get(iid);
                                receipt.removeItem(item, quant);
                            }
                        }
                    }
                    else if(command.equals("close")){
                        receipt = null;
                    }
                }


                todoList.add("Table ID: " + tid + " || Request: " + command + " || Data: " + data);

            }
        }
    }
}
