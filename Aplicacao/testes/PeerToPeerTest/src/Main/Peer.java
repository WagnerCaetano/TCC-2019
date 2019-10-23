/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Classes.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.Socket;
import javax.json.Json;

/**
 *
 * @author u18300
 */
public class Peer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("> enter username & port # for this peer:");
        String[] setupValues = bufferedReader.readLine().split(" ");
        ServerThread serverThread = new ServerThread(setupValues[1]);
        serverThread.start();
        new Peer().updateListenToPeers(bufferedReader , setupValues[0] ,serverThread);
    }
    
    public void updateListenToPeers(BufferedReader bufferedReader , String username , ServerThread serverThread) throws Exception
    {
        System.out.println(">enter (space separated) hostname:port#");
        System.out.println(" peers to receive messages from (s to skip):");
        String input = bufferedReader.readLine();
        String[] inputValues = input.split(" ");
        if (!input.equals("s")) 
            for (int i = 0; i < inputValues.length;i++)
            {
                String[] address = inputValues[i].split(":");
                Socket socket = null;
                try{
                    socket = new Socket(address[0],Integer.valueOf(address[1]));
                    new PeerThread(socket).start();
                }catch(Exception e)
                {
                   if (socket != null)
                       socket.close();
                   else System.out.println("Invalid input. Skipping to next step.");
                }
            }
        communicate(bufferedReader,username,serverThread);
    }
    
    public void communicate(BufferedReader bufferedReader , String username , ServerThread serverThread)
    {
        try{
            System.err.println("> you can now communicate (e to exit, c to change)");
            boolean flag = true;
            while (flag)
            {
                String message = bufferedReader.readLine();
                if(message.equals("e"))
                {
                   flag=false;
                   break;
                }else if (message.equals("c"))
                {
                    updateListenToPeers(bufferedReader, username, serverThread);
                }else{
                    StringWriter stringWriter = new StringWriter();
                    Json.createWriter(stringWriter).writeObject(Json.createObjectBuilder().add("username", username).add("message", message).build());
                    serverThread.sendMessage(stringWriter.toString());
            }
        }
            System.exit(0);
    }catch(Exception e) {
        e.printStackTrace();
    }
    }
}
