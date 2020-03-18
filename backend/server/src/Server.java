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
    private  HashMap<Integer, ArrayList<Integer>> servingMap;


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
        servingMap = new HashMap<>();

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

    public void handleRequest(String initialRequest){
        //split request into parts
        String[] request = initialRequest.split(",");
        int id = Integer.parseInt(request[0]);
        String command = request[1];
        String data = "";
        if (request.length > 2) {data = request[2];}

        System.out.println("Client ID: " + id + " || Request: " + command + " || Data: " + data);

        //first time client has connected
        //probably send some initialization data here
        if(command == "register"){
            if(data == "waiter")
                connectionHandlers.get(id).setWaiter(true);
            else
                connectionHandlers.get(id).setWaiter(false);
            connectionHandlers.get(id).sendMessage("Waiter: " + connectionHandlers.get(id).isWaiter);
        }
        //connection is a waiter
        else if(connectionHandlers.get(id).isWaiter){
            if(command == "claim"){
                servingMap.get(id).add(Integer.parseInt(data));
            }
            else if(command == "order"){
                //process order data
                //update table check

                
            }


        }
        //connection is a table
        else{

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


