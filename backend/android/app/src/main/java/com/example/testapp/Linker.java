package com.example.testapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Linker implements Runnable, Serializable {
    private static LinkedBlockingQueue<String> q = new LinkedBlockingQueue();
    private static ArrayList<String> todoList;

    //going to pass in anything structure that can be updated over network
    public Linker(ArrayList<String> todoList){
        this.todoList = todoList;
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(this);
    }

    public static ArrayList<String> getTodoList(){
        return todoList;
    }

    public static void addMessage(String message){
        q.offer(message);
    }

    public static void removeTodo(int position){
        todoList.remove(position);
    }

    @Override
    public void run() {
        int i=0;
        while(true){
            if(!q.isEmpty()){
                //get message
                String message = q.poll();
                System.out.println("Adding Item: " + message + " " + i);
                todoList.add(message + i++);
                System.out.println(todoList.size());
//                String[] message = q.poll().split(",");
//                int id = Integer.parseInt(message[0]);
//                String command = message[1];
//                String data = message[2];

                //do things with data
            }
        }

    }
}
