package com.example.androidslidee;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import org.apache.commons.io.FileUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Activity implements Serializable {

    //CONFIG
    private Socket connectedSocketIMG;
    private Socket connectedSocketMSG;
    private static final int SERVERPORT_MSG = 32500;
    private static final int SERVERPORT_IMG = 32499;
    private static String SERVER_IP = "";
    private Adapter_Slide slides;
    private Thread mensagem = null;
    private Thread imagem = null;

    // ENVIAR
    private PrintWriter out = null;
    // RECEBER
    private ServerSocket serverSocketIMG = null;
    private ServerSocket serverSocketMSG = null;
    private BufferedReader input = null;

    public Client(String SERVER_IP, Adapter_Slide slides)
    {
        this.SERVER_IP = SERVER_IP;
        this.slides = slides;
        enviarMensagem("CONEXAO");
        receberMensagem();
        receberImagem();
    }

    public void enviarMensagem(final String msg) {
        mensagem = new Thread(new Runnable()
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
        mensagem.start();
        mensagem.interrupt();
    }
    public void avancar() {
        enviarMensagem("AVANCAR");
    }
    public void recuar() {
        enviarMensagem("RECUAR");
    }

    public void receberMensagem() {
        mensagem = new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                try {
                    if (serverSocketMSG == null || !serverSocketMSG.isBound() || serverSocketMSG.isClosed() || connectedSocketMSG == null ||connectedSocketMSG.isClosed()) {
                        serverSocketMSG = new ServerSocket(SERVERPORT_MSG);
                        connectedSocketMSG = serverSocketMSG.accept();
                    }
                    input = new BufferedReader(new InputStreamReader(connectedSocketMSG.getInputStream()));
                    final String message = input.readLine();
                    System.out.println("Mensagem recebida: "+message);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        mensagem.start();
    }
    public void enviarImagem(final byte[] bytes) {
        imagem = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                try {
                    if (connectedSocketIMG == null){
                        InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                        connectedSocketIMG = new Socket(serverAddr, SERVERPORT_IMG);
                    }
                    if (connectedSocketIMG !=null) {
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
        imagem.start();
        imagem.interrupt();
    }

    public void receberImagem(){
        imagem =  new Thread(new Runnable() {
            @Override
            public void run() {
                int file_int = 1;
                File path = new File(Environment.getExternalStorageDirectory().toString()+"/Slides");
                try{
                    serverSocketIMG = new ServerSocket(SERVERPORT_IMG);
                    if(path.exists())
                        FileUtils.deleteDirectory(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                do  {
                    try {
                        connectedSocketIMG = serverSocketIMG.accept();
                        int filesize = 6022386;
                        int bytesRead;
                        int current = 0;

                        if (!path.exists())
                            path.mkdirs();
                        File file = new File(path, "foto"+file_int+".jpg");
                        byte[] mybytearray  = new byte [filesize];
                        InputStream is = connectedSocketIMG.getInputStream();
                        FileOutputStream fos = new FileOutputStream(file);
                        bytesRead = is.read(mybytearray,0,mybytearray.length);
                        current = bytesRead;
                        do {
                            bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
                            if(bytesRead >= 0) current += bytesRead;
                        } while(bytesRead > -1);
                        final Bitmap bmp = BitmapFactory.decodeByteArray(mybytearray,0,current);
                        bmp.compress(Bitmap.CompressFormat.JPEG,100,fos);
                        fos.flush();
                        fos.close();

                        System.out.println("RECEBIDO "+file);

                        final Slide novoSlide = new Slide("SLIDE"+file_int,file);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                slides.add(novoSlide);
                            }
                        });


                        file_int++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }while(connectedSocketIMG!=null);
            }
        });
        imagem.start();
    }
}
