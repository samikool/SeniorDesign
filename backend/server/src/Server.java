import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


public class Server extends JFrame implements Runnable {
    //connection variables
    private int port;
    private ServerSocket serverSocket;
    private HashMap<Integer, ConnectionHandler> connectionHandlers;
    private int clientsConnected;
    private  LinkedBlockingQueue<String> requestQ;
    private DBConnector db;

    //executor for threading
    private  ExecutorService executor;

    //data structures for comms
    HashMap<Integer, Integer> conToTableMap;
    HashMap<Integer, Integer> tableToConMap;
    HashMap<Integer, Integer> waiterToConMap;
    HashMap<Integer, Integer> conToWaiterMap;
    HashMap<Integer, Integer> servingMap;

    //data structures for database data
    HashMap<Integer, Receipt> receiptMap;

    //gui elements
    private JTextArea console;
    private JScrollPane pane;
    private JTextField inputField;

    public Server(int port){
        super("Server");
        //initialize connection variables
        this.connectionHandlers = new HashMap<>();
        this.clientsConnected = 0;
        this.port = port;
        requestQ = new LinkedBlockingQueue<>();
        db = new DBConnector();
        //db.insertImages();

        //initialize executor
        this.executor = Executors.newCachedThreadPool();

        //initialize data structures for comms
        conToTableMap = new HashMap<>(); // <CID, TID>
        tableToConMap = new HashMap<>();
        conToWaiterMap = new HashMap<>(); // <CID, WID>
        waiterToConMap = new HashMap<>();
        servingMap = new HashMap<>(); // <TID, WID> since every TID can only have 1 WID

        //initialize data structures for data
        receiptMap = new HashMap<>(); // <TID, Receipt>

        //initialize gui

        console = new JTextArea();
        pane = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        inputField = new JTextField();
        inputField.setSize(1, 200);
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String data = inputField.getText();
            }
        });

        this.setLayout(new BorderLayout());
        this.add(pane, BorderLayout.CENTER);
        this.add(inputField, BorderLayout.NORTH);

        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,400);
    }

    public void start() throws IOException{
        serverSocket = new ServerSocket(port,512);
        executor.execute(this);

        while(true){
            try{
                System.out.println("waiting for client: " + clientsConnected + " to connect...");
                connectionHandlers.put(clientsConnected, new ConnectionHandler(clientsConnected));
                connectionHandlers.get(clientsConnected).waitForConnection();

                executor.execute(connectionHandlers.get(clientsConnected));

                System.out.println("Connection: " + clientsConnected + " successfully made");
                clientsConnected++;
            }catch (Exception e){
                System.err.println("Error connecting to cleint " + clientsConnected);
                System.err.println(e);
            }
        }
    }

    @Override
    public void run() {
        while (true){
            //Get message and Client ID
            try{
                String initialRequest = requestQ.take();
                handleRequest(initialRequest);
            }catch (Exception e){
                System.out.println(e);
            }


        }
    }

    /**
     * Main function to handle requests and send responses
     * @param initialRequest string of the initial request which will be broken into parts
     */
    public void handleRequest(String initialRequest){
        //split request into parts
        String[] request = initialRequest.split(",");
        int cid = Integer.parseInt(request[0]);
        String command = request[1];
        ArrayList<String> data = new ArrayList<>();
        if (request.length > 2) {
            for (int i = 2; i < request.length; i++) {
                data.add(request[i]);
            }
        }

        //first time client has connected
        //probably send some initialization data here
        if(command.equals("register")){
            //REGISTER, WAITER, WID
            //REGISTER, TABLE, TID
            int waitOrTableID = Integer.parseInt(data.get(1));
            if(data.get(0).equals("waiter")){
                connectionHandlers.get(cid).setWaiter(true);
                conToWaiterMap.put(cid, waitOrTableID);
                waiterToConMap.put(waitOrTableID, cid);
            }
            else {
                connectionHandlers.get(cid).setWaiter(false);
                conToTableMap.put(cid, waitOrTableID);
                tableToConMap.put(waitOrTableID, cid);
            }
        }
        else if(command.equals("items")){
            //ITEMS
            ArrayList<Item> drinks = db.getAllDrinks();
            ArrayList<Item> bbqs = db.getAllBBQs();
            ArrayList<Item> sides = db.getAllSides();

            String drinkString = "0,items,drink";
            for (Item item : drinks){
                drinkString += "," + item.getId()+ "," + item.getName() + "," + item.getPrice() + "," + item.getEncodedImage();
            }

            String bbqString = "0,items,bbq";
            for (Item item : bbqs){
                bbqString += "," + item.getId()+ "," + item.getName() + "," + item.getPrice() + "," + item.getEncodedImage();
            }

            String sideString = "0,items,side";
            for (Item item : sides){
                sideString += "," + item.getId()+ "," + item.getName() + "," + item.getPrice() + "," + item.getEncodedImage();
            }

            connectionHandlers.get(cid).sendMessage(drinkString);
            connectionHandlers.get(cid).sendMessage(bbqString);
            connectionHandlers.get(cid).sendMessage(sideString);

        }
        //connection is a waiter
        else if(connectionHandlers.get(cid).isWaiter){
            int wid = conToWaiterMap.get(cid);
            if(command.equals("claim")){
                //CID, CLAIM, TID
                int tid = Integer.parseInt(data.get(0));
                servingMap.put(tid, wid);
                receiptMap.put(tid, new Receipt(tid, wid));
                String response = tid+",claim,"+wid;
                conToWaiterMap.forEach((k, v) -> {
                    connectionHandlers.get(k).sendMessage(response);
                });
                int tableCid = tableToConMap.get(tid);
                connectionHandlers.get(tableCid).sendMessage(response);
            }
            else if(command.equals("order")){
                //CID, ORDER, TID, CATEGORY, IID, QUANT, ...
               int tid = Integer.parseInt(data.get(0));

                String category = "";
                Item item;
                int iid;
                int quant;
                for(int i=1; i<data.size(); i+=3){
                    category = data.get(i);
                    iid = Integer.parseInt(data.get(i+1));
                    quant = Integer.parseInt(data.get(i+2));
                    if(category.equals("bbq")){
                        item = db.getBBQ(iid);
                        receiptMap.get(tid).addItem(item, quant);
                    }
                    else if(category.equals("drink")){
                        item = db.getDrink(iid);
                        receiptMap.get(tid).addItem(item, quant);
                    }
                    else if(category.equals("sides")){
                        item = db.getSide(iid);
                        receiptMap.get(tid).addItem(item, quant);
                    }
                }

                int sendCid = tableToConMap.get(tid);
                String response = tid+",order";
                for (int i=1; i<data.size(); i++) {
                    response+= ","+data.get(i);
                }

                connectionHandlers.get(sendCid).sendMessage(response);
            }
            else if(command.equals("void")){
                //CID, VOID, TID, CATEGORY, IID, QUANT, ...
                int tid = Integer.parseInt(data.get(0));

                String category = "";
                Item item;
                int iid;
                int quant;
                for(int i=1; i<data.size(); i+=3){
                    category = data.get(i);
                    iid = Integer.parseInt(data.get(i+1));
                    quant = Integer.parseInt(data.get(i+2));
                    if(category.equals("bbq")){
                        item = db.getBBQ(iid);
                        receiptMap.get(tid).removeItem(item, quant);
                    }
                    else if(category.equals("drink")){
                        item = db.getDrink(iid);
                        receiptMap.get(tid).removeItem(item, quant);
                    }
                }
                int sendCID = tableToConMap.get(tid);
                String response = tid+",void";
                for(int i=1; i<data.size(); i++){
                    response += ","+data.get(i);
                }
                connectionHandlers.get(sendCID).sendMessage(response);
            }
            else if(command.equals("close")){
                //CID, CLOSE, TID
                int tid = Integer.parseInt(data.get(0));
                Receipt receipt = receiptMap.remove(tid);
                if(receipt.getNumItems() > 0){
                    db.writeReceipt(receipt);
                }

                String response = tid+",close";
                conToWaiterMap.forEach((k, v) -> {
                    connectionHandlers.get(k).sendMessage(response);
                });

                int tableCid = tableToConMap.get(tid);
                connectionHandlers.get(tableCid).sendMessage(response);
            }
        }
        //connection is a table
        else if(!connectionHandlers.get(cid).isWaiter){
            int tid = conToTableMap.get(cid);
            if(command.equals("call")){
                //TID,CALL
                int wid = servingMap.get(tid);
                int sendCid = waiterToConMap.get(wid);

                String response = tid + ",call";
                connectionHandlers.get(sendCid).sendMessage(response);
            }
            else if(command.equals("order")){
                int wid = servingMap.get(tid);
                int sendCid = waiterToConMap.get(wid);

                int iid;
                int quant;
                for (int i=0; i < data.size(); i+=3) {
                    String category = data.get(i);
                    iid = Integer.parseInt(data.get(i+1));
                    quant = Integer.parseInt(data.get(i+2));

                    if(category.equals("drink")){
                        Item item = db.getDrink(iid);
                        receiptMap.get(tid).addItem(item, quant);
                    }
                    else if(category.equals("bbq")){
                        Item item = db.getBBQ(iid);
                        receiptMap.get(tid).addItem(item, quant);
                    }
                    else if(category.equals("side")){
                        Item item = db.getSide(iid);
                        receiptMap.get(tid).addItem(item, quant);
                    }
                }
                String response = tid + ",order";
                for(int i=0; i<data.size(); i++){
                    response += "," + data.get(i);
                }
                connectionHandlers.get(sendCid).sendMessage(response);
            }
            else if(command.equals("check")){
                //TID,CHECK
                int wid = servingMap.get(tid);
                int sendCid = waiterToConMap.get(wid);

                String response = tid + ",check";
                connectionHandlers.get(sendCid).sendMessage(response);
            }
            else if(command.equals("chop")){
                //TID,CHOP,UTENSIL,QUANT
                int wid = servingMap.get(tid);
                int sendCid = waiterToConMap.get(wid);

                String utensil = data.get(0);
                int quant =  Integer.parseInt(data.get(1));

                String response = tid+",chop,"+utensil+","+quant;
                connectionHandlers.get(sendCid).sendMessage(response);
            }
        }
        System.out.println("Client ID: " + cid + " || Request: " + command + " || Data: " + data + " || isWaiter: " + connectionHandlers.get(cid).isWaiter);
        console.append("Client ID: " + cid + " || Request: " + command + " || Data: " + data + " || isWaiter: " + connectionHandlers.get(cid).isWaiter + "\n");
        printReceipt();
    }

    private void printReceipt(){
        for (Receipt receipt : receiptMap.values()) {
            if(receipt.getNumItems() > 0){
                System.out.println(receipt);
                console.append(receipt.toString());
            }
        }
    }

    private class ConnectionHandler implements Runnable{
        private Socket clientSocket;
        private int clientID;
        private BufferedWriter output;
        private BufferedReader input;
        private boolean isWaiter;


        public ConnectionHandler(int id){
            clientID = id;
        }

        public void setWaiter(boolean isTable){this.isWaiter = isTable;}

        public void waitForConnection() throws IOException{
            clientSocket = serverSocket.accept();
        }

        public void initializeBuffers(){
            try{
                output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                output.flush();

                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("Buffers successfully setup");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public void receiveRequests(){
            while (true){
                try{
                    String command = input.readLine();
                    //System.out.println(command);
                    if(command != null){
                        //System.out.println(command + " from " + clientSocket.getInetAddress());
                        String request = clientID + "," + command;
                        //System.out.println("Adding: " + request + " to Q");
                        requestQ.put(request);
                    }
                    else{break;}
                    Thread.sleep(1);
                }catch (Exception e){
                    System.err.println(e);
                }
            }
            closeConnection();
        }

        public void sendMessage(String message){
            try{
                System.out.println("Sending: " + message);
                console.append("Sending ClientID: " + this.clientID + " " + message + "\n");
                output.write(message+"\n");
                output.flush();
            }catch (IOException e){
                System.err.println(e);
            }
        }

        public void closeConnection(){
            System.out.println("Attempting to close connection with client: " + clientID);
            try{
                clientSocket.close();
                input.close();
                output.close();
                System.out.println("Client connection " + clientID + " closed." );
                console.append("Client connection " + clientID + " closed.\n");
            }catch (IOException e){
                System.err.println("Error closing client connection");
                System.err.println(e);
            }

        }

        @Override
        public void run() {
            initializeBuffers();
            receiveRequests();
        }
    }

    public static void main(String[] args){
        //DBConnector db = new DBConnector();
        //db.insertImages();
//        for (Item item : db.getAllBBQs()) {
//                System.out.println(item);
//        }
//
//        for (Item item : db.getAllDrinks()){
//            System.out.println(item);
//        }
//
//
//        Item samgyp = db.getBBQ(1);
//        Item galbi = db.getBBQ(4);
//        Item pepsi = db.getDrink(2);
//        Item sprite = db.getDrink(7);
//
//        Receipt receipt = new Receipt(3, 004);
//        receipt.addItem(samgyp, 2);
//        receipt.addItem(galbi,1);
//        receipt.addItem(pepsi, 1);
//        receipt.addItem(sprite, 1);
//
//        db.writeReceipt(receipt);
//
//        System.out.println(receipt);

        try{
            Server testServer = new Server(4044);
            testServer.start();
        }catch (Exception e){
            System.err.println(e);
        }
    }
}