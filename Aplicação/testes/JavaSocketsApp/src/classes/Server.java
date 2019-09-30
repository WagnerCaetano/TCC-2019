/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;

/**
 *
 * @author 00
 */
public class Server {
    
    static ServerSocket ss;
    static Socket s;
    static BufferedReader br;
    static InputStreamReader isr;
    static String message = "";
    
    public Server(JTextField txt)
    {
    
    try
        {
            ss = new ServerSocket(7800);
            while(true)
            {
                s = ss.accept();
                isr = new InputStreamReader(s.getInputStream());
                br = new BufferedReader(isr);
                message = br.readLine();
                
                System.err.println(message);
                
                if (txt.getText().toString().equals(""))
                {
                    txt.setText("Android: " + message);
                }
                else
                {
                    txt.setText(txt.getText() + "\n" + message);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
