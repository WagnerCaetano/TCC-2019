        /*
         * To change this license header, choose License Headers in Project Properties.
         * To change this template file, choose Tools | Templates
         * and open the template in the editor.
         */
        package classes;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
        import java.io.*;
        import java.net.*;

        public class Server {            
            // declare a server socket and a client socket for the server
            ServerSocket echoServer = null;
            Socket clientSocket = null;
            BufferedImage[] img;
            int port;
            public Server( int port , BufferedImage[] img) {
            this.port = port;
            this.img = img;
            }
            public void stopServer() {
            System.out.println( "Server cleaning up." );
            System.exit(0);
            }
            public void startServer() {
            // Try to open a server socket on the given port
            // Note that we can't choose a port less than 1024 if we are not
            // privileged users (root)
                try {
                echoServer = new ServerSocket(port);
                }
                catch (IOException e) {
                System.out.println(e);
                }   
            System.out.println( "Waiting for connections. Only one connection is allowed." );
            // Create a socket object from the ServerSocket to listen and accept connections.
            // Use Server1Connection to process the connection.
            while ( true ) {
                try {
                clientSocket = echoServer.accept();
                Server1Connection oneconnection = new Server1Connection(clientSocket, this,img);
                oneconnection.run();
                }   
                catch (IOException e) {
                System.out.println(e);
                }
            }
            }
        }
        class Server1Connection {
            BufferedReader is;
            PrintStream os;
            Socket clientSocket;
            Server server;
            BufferedImage[] img;

            public Server1Connection(Socket clientSocket, Server server,BufferedImage[] img) {
            this.clientSocket = clientSocket;
            this.server = server;
            this.img = img;
            
            System.out.println( "Connection established with: " + clientSocket );
            try {
                is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                os = new PrintStream(clientSocket.getOutputStream());
            } catch (IOException e) {
                System.out.println(e);
            }
            }
            public void run() throws AWTException, IOException {
                String line;
            try {
                boolean serverStop = false;
                    while (true) {
                        line = is.readLine();
                        switch(line){
                            case "s":{
                                line = is.readLine();
                                 if(line == "a") //avançar
                                    Utils.avancar();
                                 else if(line == "r") //retroceder
                                    Utils.retroceder();
                                 break;
                            }
                            case "c":{ //mover cursor
                                line = is.readLine();
                                int x = new Integer(line);
                                line = is.readLine();
                                int y = new Integer(line);
                                Utils.mover(x, y);
                            }
                            case "p":{
                                
                            }
                        }                                                                                    
                System.out.println( "Received " + line );
                int n = Integer.parseInt(line);
                if ( n == -1 ) {
                    serverStop = true;
                    break;
                }
                if ( n == 0 ) break;
                        os.println("" + img); 
                    }
                System.out.println( "Connection closed." );
                    is.close();
                    os.close();
                    clientSocket.close();
                if ( serverStop ) server.stopServer();
            } catch (IOException e) {
                System.out.println(e);
        }
    }
}