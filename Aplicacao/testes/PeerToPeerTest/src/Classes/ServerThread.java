/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author u18300
 */
public class ServerThread extends Thread{
    private ServerSocket serverSocket;
    private Set<ServerThreadThread> serverThreadThreads = new HashSet<ServerThreadThread>();
    public ServerThread(String portNumb) throws IOException{
        serverSocket = new ServerSocket(Integer.valueOf(portNumb));
    }
    @Override
    public void run(){
        try{
            while(true){
                ServerThreadThread serverThreadThread = new ServerThreadThread(this,serverSocket.accept());
                serverThreadThreads.add(serverThreadThread);
                serverThreadThread.start();
            }
        }catch (Exception e){e.printStackTrace();}
    }
    public void sendMessage(String message){
        try{
            serverThreadThreads.forEach(t-> t.getPrintWriter().println(message));
        }catch(Exception e){ e.printStackTrace();}
    }
    public Set<ServerThreadThread> getServerThreadThread() {
        return serverThreadThreads;
    }
}
