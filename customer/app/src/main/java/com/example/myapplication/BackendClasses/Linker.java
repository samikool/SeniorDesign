package com.example.myapplication.BackendClasses;


import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

//import waiter.myapplication.MainActivity;
//import waiter.myapplication.R;
//import waiter.myapplication.TodoList.TodoListActivity;

public class Linker implements Runnable, Serializable {
    private static LinkedBlockingQueue<String> q;
    private static ArrayList<String> todoList;
    private static HashMap<String, Long> todoTimes;
//    private static TodoListActivity todoListActivity;
    private static ArrayList<Item> drinkItems;
    private static ArrayList<Item> bbqItems;
    private static ArrayList<Item> sideItems;
    private static Connection connection;
    private static int id;
    private static boolean isWaiter;
    private static Receipt receipt;
    private static HashMap<Integer, Receipt> receiptMap; //<tid,TableReceipt>
    private static HashMap<Integer, Integer> tableMap;
    private static View currentView;


    public static HashMap<Integer, Date> getCheckedMap() {
        return checkedMap;
    }

    private static HashMap<Integer, Date> checkedMap;

    //going to pass in anything structure that can be updated over network
    public Linker(int id, boolean isWaiter){
        drinkItems = new ArrayList<>();
        bbqItems = new ArrayList<>();
        sideItems = new ArrayList<>();
        q = new LinkedBlockingQueue<>();

        connection = new Connection("seniordesign.now.im", 4044, q);
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
            checkedMap = new HashMap<Integer, Date>();
            todoList = new ArrayList<String>();
            todoTimes = new HashMap<String, Long>();
        }else{
            registerTable();
        }



    }

    public static void setTodoList(ArrayList<String> todoList){
        Linker.todoList = todoList;
    }

    public static ArrayList<String> getTodoList(){
        return todoList;
    }

    public static HashMap<String, Long> getTodoTimes(){return todoTimes;}

//    public static void setTodoListActivity(TodoListActivity activity){
//        todoListActivity = activity;
//    }

//    public static void checkedTable(int tid){
//        checkedMap.put(tid, Calendar.getInstance().getTime());
//        MainActivity.updateTableColor(tid, currentView.getContext().getColorStateList(R.color.lightGreen));
//    }

//    public static void markTable(int tid){
//        Date zeroDate = Calendar.getInstance().getTime();
//        zeroDate.setTime(0);
//        checkedMap.put(tid, zeroDate);
//        MainActivity.updateTableColor(tid, currentView.getContext().getColorStateList(R.color.red));
//        String request = tid+",call";
//        if(!todoList.contains(request)) {
//            todoList.add(request);
//            todoTimes.put(request, Calendar.getInstance().getTime().getTime());
//
//            if (TodoListActivity.isActive()) {
//                todoListActivity.addTodo(request);
//            }
//            markTable(tid);
//        }
//    }



    public static ArrayList<Item> getDrinkItems() {
        return drinkItems;
    }
    public static ArrayList<Item> getBBQItems() {
        return bbqItems;
    }
    public static ArrayList<Item> getSideItems() {
        return sideItems;
    }

    public static Receipt getTableReceipt(int tid){
        return receiptMap.get(tid);
    }

    public static void setCurrentView(View v){
        currentView = v;
        System.out.println(currentView);
    }

    public static int getId(){
        return id;
    }




    private void processItems(ArrayList<String> data){
        if (data.get(0).equals("drink")){
            drinkItems.clear();
            fillItemList(data, drinkItems, ItemType.drink);
        }
        else if((data.get(0).equals("side"))){
            sideItems.clear();
            fillItemList(data, sideItems, ItemType.side);
        }
        else if(data.get(0).equals("bbq")){
            bbqItems.clear();
            fillItemList(data, bbqItems, ItemType.bbq);
        }
    }

    private void fillItemList(ArrayList<String> data, ArrayList<Item> itemList, ItemType type){
        itemList.add(new Item()); //empty item so index matches iid
        for(int i=1; i<data.size(); i+=4){
            Item item = new Item(Integer.parseInt(data.get(i)), data.get(i+1), Double.parseDouble(data.get(i+2)), data.get(i+3));
            item.setItemType(type);
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

    public static void voidItem(Item item, int quant, int tid){
        int iid = item.getId();
        if(item.getItemType() == ItemType.bbq){
            voidBBq(iid, quant, tid);
        }
        else if(item.getItemType() == ItemType.drink){
            voidDrink(iid, quant, tid);
        }
        else if(item.getItemType() == ItemType.side){
            voidSide(iid, quant, tid);
        }
    }

    public static void voidBBq(int iid, int quant, int tid){
        sendMessage("void,"+tid+",bbq,"+iid+","+quant);
        receiptMap.get(tid).addItem(bbqItems.get(iid), quant);
    }

    public static void voidDrink(int iid, int quant, int tid){
        sendMessage("void,"+tid+",drink,"+iid+","+quant);
        receiptMap.get(tid).addItem(drinkItems.get(iid), quant);
    }

    public static void voidSide(int iid, int quant, int tid){
        sendMessage("void,"+tid+",side,"+iid+","+quant);
        receiptMap.get(tid).addItem(sideItems.get(iid), quant);
    }

    public static void orderItems(Receipt r){
        int tid = r.getTid();
        for(Item item : r.getItems()){
            int quant = r.getItemCount(item);
            if(item.getItemType() == ItemType.bbq){
                orderBBQ(item.getId(), quant, tid);
            }
            else if(item.getItemType() == ItemType.drink){
                orderDrink(item.getId(), quant, tid);
            }
            else if(item.getItemType() == ItemType.side){
                orderSide(item.getId(), quant, tid);
            }
        }
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

//    public static void claim(int tid){
//        receiptMap.put(tid, new Receipt(tid, id));
//
//        MainActivity.updateTableColor(tid, currentView.getContext().getColorStateList(R.color.lightGreen));
//        tableMap.put(tid, id);
//        checkedTable(tid);
//        sendMessage("claim,"+tid);
//    }
//
//    public static void closeTable(int tid){
//        receiptMap.remove(tid);
//        tableMap.remove(tid);
//        checkedMap.remove(tid);
//        sendMessage("close,"+tid);
//        MainActivity.updateTableColor(tid, currentView.getContext().getColorStateList(R.color.defaultGray));
//    }

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
            //get message
            String request = "";
            try{
                request =  q.take();
            }catch (Exception e){
                System.out.println(e);
            }
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
                System.out.println(currentView);
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
                    if(!todoList.contains(request)){
                        todoList.add(request);
                        todoTimes.put(request, Calendar.getInstance().getTime().getTime());

//                        if(TodoListActivity.isActive()){
//                            todoListActivity.addTodo(request);
//                        }
//                        markTable(tid);
//                    }
//                }
//                else if(command.equals("check")){
//                    if(!todoList.contains(request)){
//                        todoList.add(request);
//                        todoTimes.put(request, Calendar.getInstance().getTime().getTime());
//
//                        if(TodoListActivity.isActive()){
//                            todoListActivity.addTodo(request);
//                        }
//                        markTable(tid);
                    }
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
                    if(!todoList.contains(request)){
                        todoList.add(request);
                        todoTimes.put(request, Calendar.getInstance().getTime().getTime());

//                        if(TodoListActivity.isActive()){
//                            todoListActivity.addTodo(request);
//                        }
//                        markTable(tid);
                    }
                }
//                else if(command.equals("claim")){
//                    int wid = Integer.parseInt(data.get(0));
//                    tableMap.put(tid, wid);
//                    MainActivity.updateTableColor(tid, currentView.getContext().getColorStateList(R.color.gray));
//
//
//                }
//                else if(command.equals("close")){
//                    tableMap.remove(tid);
//                    MainActivity.updateTableColor(tid, currentView.getContext().getColorStateList(R.color.defaultGray));
//                }
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


            System.out.println("Table ID: " + tid + " || Request: " + command + " || Data: " + data);


        }
    }
}
