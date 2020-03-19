import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

//TODO: When connection handler gets request then, add to Queue
//TODO: Thread Constantly reading requests pulling from Queue
//TODO: After reading it needs to send appropiate message and update appropiate data structures
//TODO: HashMap to store table-server pair by IP

public class Server extends JFrame implements Runnable {
    //connection variables
    private int port;
    private ServerSocket serverSocket;
    private HashMap<Integer, ConnectionHandler> connectionHandlers;
    private int clientsConnected;
    private  LinkedBlockingQueue<String> requestQ;

    //executor for threading
    private  ExecutorService executor;

    //data structures for comms
    HashMap<Integer, Integer> tableMap;
    HashMap<Integer, Integer> waiterMap;
    HashMap<Integer, Integer> servingMap;

    //data structures for database data
    HashMap<Integer, Item> itemMap;
    HashMap<Integer, Receipt> receiptMap;

    //gui elements
    private JTextArea console;
    private JTextField inputField;

    public Server(int port){
        super("Server");
        //initialize connection variables
        this.connectionHandlers = new HashMap<>();
        this.clientsConnected = 0;
        this.port = port;
        requestQ = new LinkedBlockingQueue<>();

        //initialize executor
        this.executor = Executors.newCachedThreadPool();

        //initialize data structures for comms
        tableMap = new HashMap<>(); // <CID, TID>
        waiterMap = new HashMap<>(); // <CID, WID>
        servingMap = new HashMap<>(); // <TID, WID> since every TID can only have 1 WID

        //initialize data structures for data
        itemMap = new HashMap<>(); //TODO: initialize an item list with all possible items, probably from database
        receiptMap = new HashMap<>(); // <TID, Receipt>

        //initialize gui
        console = new JTextArea();
        inputField = new JTextField();
        inputField.setSize(1, 200);
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String data = inputField.getText();
            }
        });

        this.setLayout(new BorderLayout());
        this.add(console, BorderLayout.CENTER);
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
            if(!requestQ.isEmpty()){
                //Get message and Client ID
                String initialRequest = requestQ.poll();
                handleRequest(initialRequest);
            }
        }
    }

    /**
     * Main function to handle requests and send responses
     * @param initialRequest string of the intial request which will be broken into parts
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

        System.out.println("Client ID: " + cid + " || Request: " + command + " || Data: " + data);

        //first time client has connected
        //probably send some initialization data here
        if(command.equals("register")){
            //REGISTER, WAITER, WID
            //REGISTER, TABLE, TID
            int waitOrTableID = Integer.parseInt(data.get(1));
            if(data.get(0).equals("waiter")){
                connectionHandlers.get(cid).setWaiter(true);
                waiterMap.put(cid, waitOrTableID);
            }
            else {
                connectionHandlers.get(cid).setWaiter(false);
                tableMap.put(cid, waitOrTableID);
            }
        }
        //connection is a waiter
        else if(connectionHandlers.get(cid).isWaiter){
            int wid = waiterMap.get(cid);
            if(command.equals("claim")){
                //CID, CLAIM, TID
                int tid = Integer.parseInt(data.get(0));
                servingMap.put(tid, wid);
                receiptMap.put(tid, new Receipt(tid, wid));
                String response = tid+",claim,"+wid;
                waiterMap.forEach((k,v) -> {
                    connectionHandlers.get(k).sendMessage(response);
                });
            }
            else if(command.equals("order")){
                //CID, ORDER, TID, ITEM, QUANT
                int tid = Integer.parseInt(data.get(0));
                int iid = Integer.parseInt(data.get(1));
                int quant = Integer.parseInt(data.get(2));
                receiptMap.get(tid).addItem(itemMap.get(iid), quant);
                //Item item = database.pull(ITEM)
            }
            else if(command.equals("unorder")){
                //CID, UNORDER, TID, ITEM, QUANT
            }
        }
        //connection is a table
        else if(!connectionHandlers.get(cid).isWaiter){
            int tid = tableMap.get(cid);
        }                                             
    }



    private class ConnectionHandler implements Runnable{
        private Socket clientSocket;
        private int clientID;
        private BufferedWriter output;
        private BufferedReader input;
        private boolean activeConnection;
        private boolean isWaiter;


        public ConnectionHandler(int id){
            clientID = id;
            this.activeConnection = false;
        }

        public void setWaiter(boolean isTable){this.isWaiter = isTable;}

        public void waitForConnection() throws IOException{
            clientSocket = serverSocket.accept();

            activeConnection = true;
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
            while (activeConnection){
                try{
                    String command = input.readLine();
                    if(command != null){
                        //System.out.println(command + " from " + clientSocket.getInetAddress());
                        String request = String.valueOf(clientID) + "," + command;
                        //System.out.println("Adding: " + request + " to Q");
                        requestQ.offer(request);
                    }
                }catch (Exception e){
                    System.err.println(e);
                }
            }
            closeConnection();
        }

        public void sendMessage(String message){
            try{
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
        try{
            Server testServer = new Server(4044);
            testServer.start();
        }catch (Exception e){
            System.err.println(e);
        }
    }
}


