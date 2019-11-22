package com.example.androidslidee;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientWifi {

    //CONFIG
    private Socket connectedSocketIMG;
    private Socket connectedSocketMSG;
    private static final int SERVERPORT_MSG = 32500;
    private static final int SERVERPORT_IMG = 32499;
    private static String SERVER_IP = "";
    private String MENSAGEM_CONTROLE = "";
    private List<Slide> slides;

    // ENVIAR
    private EditText edtText=null;
    private PrintWriter out = null;
    // RECEBER
    private ServerSocket serverSocket;
    private BufferedReader input = null;

    public ClientWifi(String SERVER_IP,List slides)
    {
        this.SERVER_IP = SERVER_IP;
        this.slides = slides;
        init();
    }
    private void init()
    {
        String thisIp = getIPAddress(true);
        String mensagem = "IP\n"+thisIp;
        try {
            enviarMensagem(mensagem);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try { Thread.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }
        receberMensagem();
        receberImagem();
    }

    public void enviarMensagem(final String msg) throws InterruptedException {
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
        Thread.sleep(500);
        if (MENSAGEM_CONTROLE == "OK") MENSAGEM_CONTROLE = "";
        else enviarMensagem(msg);
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
                    if(message == "OK")
                        MENSAGEM_CONTROLE = message;
                    else
                        MENSAGEM_CONTROLE = "";
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
                        Slide novoSlide = new Slide("SLIDE",bmp);
                        slides.add(novoSlide);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }while(connectedSocket!=null);
            }
        }).start();
    }
    public String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%');
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { }
        return null;
    }

}
