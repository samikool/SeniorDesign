import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TODO: When connection handler gets request then, add to Queue
//TODO: Thread Constantly reading requests pulling from Queue
//TODO: After reading it needs to send appropiate message and update appropiate data structures
//TODO: HashMap to store table-server pair by IP

public class Server extends JFrame implements Runnable {
    private int port;
    private ServerSocket serverSocket;
    private ConnectionHandler[] connectionHandlers;
    private static ExecutorService executor;
    private int clientsConnected;
    private JTextArea console;
    private JTextField inputField;

    public Server(int port){
        super("Server");
        this.connectionHandlers = new ConnectionHandler[512];
        this.executor = Executors.newCachedThreadPool();
        this.clientsConnected = 0;
        this.port = port;

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

    public static ExecutorService getExecutor(){return executor;}

    public Server(String port) {
        this(Integer.valueOf(port));
    }

    public void start() throws IOException{
        serverSocket = new ServerSocket(port,512);
        

        while(true){
            try{
                System.out.println("waiting for client: " + clientsConnected + " to connect...");
                connectionHandlers[clientsConnected] = new ConnectionHandler(clientsConnected);
                connectionHandlers[clientsConnected].waitForConnection();

                executor.execute(connectionHandlers[clientsConnected]);

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
        
    }


    private class ConnectionHandler implements Runnable{
        private Socket clientSocket;
        private int clientID;
        //private ObjectInputStream input;
        //private ObjectOutputStream output;
        private BufferedWriter output;
        private BufferedReader input;
        private boolean activeConnection;

        public ConnectionHandler(int id){
            clientID = id;
            this.activeConnection = false;
        }

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

        public void processConnection(){
            while (activeConnection){
                try{
                    String type = input.readLine();
                    if(type != null){
                        System.out.println(type);
                        output.write(type+'\n');
                        output.flush();
                    }
                }catch (Exception e){
                    System.err.println(e);
                }
            }
            closeConnection();
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
            processConnection();
        }
    }

    public static void main(String[] args){
        try{
            Server testServer = new Server("4044");
            testServer.start();
        }catch (Exception e){
            System.err.println(e);
        }
    }
}


