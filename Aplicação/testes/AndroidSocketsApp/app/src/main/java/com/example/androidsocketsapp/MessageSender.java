package com.example.androidsocketsapp;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MessageSender extends AsyncTask<String,Void,Void> {

    Socket s;
    DataOutputStream dos;
    PrintWriter pw;
    String ip;
    public MessageSender(String ip)
    {
        this.ip = ip;
    }

    @Override
    protected Void doInBackground(String... voids) {
        String message = voids[0];
        try {


            s = new Socket(ip,7800);
            pw = new PrintWriter(s.getOutputStream());
            pw.write(message);
            pw.flush();
            pw.close();
            s.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
