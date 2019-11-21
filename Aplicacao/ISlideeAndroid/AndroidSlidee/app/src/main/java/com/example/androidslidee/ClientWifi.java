package com.example.androidslidee;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class ClientWifi {

    //CONFIG
    private Socket connectedSocketIMG;
    private Socket connectedSocketMSG;
    private static final int SERVERPORT_MSG = 32500;
    private static final int SERVERPORT_IMG = 32499;
    private static String SERVER_IP = "";
    // ENVIAR
    private EditText edtText=null;
    private PrintWriter out = null;
    private EditText et = null;
    // RECEBER
    private ServerSocket serverSocket;
    private BufferedReader input = null;
    private TextView text=null;
    private ObjectInputStream ois=null;
    // BUTTONS
    // IMAGEM
    //private ImageView img;

    public ClientWifi(String SERVER_IP)
    {
        this.SERVER_IP = SERVER_IP;
    }

    public void enviarMensagem(final String msg) {
        SERVER_IP = edtText.getText().toString();
        Thread enviar = new Thread(new Runnable()
        {
            public void run() {
                try {
                    if(connectedSocketMSG==null)
                    {
                        InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                        connectedSocketMSG = new Socket(serverAddr, SERVERPORT_MSG);
                    }
                    if(connectedSocketMSG !=null){
                        if (out == null)
                            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connectedSocketMSG.getOutputStream())), true);
                        out.println(msg);
                    }
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });
        enviar.start();
        enviar.interrupt();
    }

    public void receberMensagem() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                    Socket socket = null;
                    try {
                        serverSocket = new ServerSocket(SERVERPORT_MSG);
                        connectedSocketMSG = serverSocket.accept();
                        input = new BufferedReader(new InputStreamReader(connectedSocketMSG.getInputStream()));
                        final String message = input.readLine();
                        System.out.println("Mensagem recebida: "+message);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
            }
        }).start();
    }
    public void enviarImagem(final Image img) {
        Thread sendImg = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                try {
                    if (connectedSocketIMG == null){
                        InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                        connectedSocketIMG = new Socket(serverAddr, SERVERPORT_IMG);
                    }
                    if (connectedSocketIMG !=null) {
                        ByteBuffer buffer = img.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        OutputStream os = connectedSocketIMG.getOutputStream();
                        os.write(bytes,0,bytes.length);
                        os.flush();
                        os.close();
                        Thread.sleep(200);
                        connectedSocketIMG.close();
                    }
                }catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        sendImg.start();
        sendImg.interrupt();
    }

    public void receberImagem(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket connectedSocket = null;
                try {
                    serverSocket = new ServerSocket(SERVERPORT_IMG);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                do  {
                    try {
                        connectedSocket = serverSocket.accept();
                        int filesize = 6022386;
                        int bytesRead;
                        int current = 0;
                        byte[] mybytearray  = new byte [filesize];
                        InputStream is = connectedSocket.getInputStream();
                        bytesRead = is.read(mybytearray,0,mybytearray.length);
                        current = bytesRead;
                        do {
                            bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
                            if(bytesRead >= 0) current += bytesRead;
                        } while(bytesRead > -1);
                        final Bitmap bmp = BitmapFactory.decodeByteArray(mybytearray,0,current);
                        //img.setImageBitmap(bmp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }while(connectedSocket!=null);
            }
        }).start();
    }
}
