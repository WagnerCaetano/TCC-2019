package com.example.client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {


    //CONFIG
    private Socket connectedSocket;
    private static final int SERVERPORT = 32499;
    private static String SERVER_IP = "";
    private boolean parar = false;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
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
    private Button btnEnviar;
    private Button btnReceber;
    private Button btnParar;
    // IMAGEM
    private ImageView img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //edtText = findViewById(R.id.txtIP);
        text = findViewById(R.id.txtReceber);
        //btnEnviar = findViewById(R.id.btnEnviar);
        //btnEnviar.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { onEnviar(); } } );
        btnReceber = findViewById(R.id.btnReceber);
        btnReceber.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { onReceber(); /*btnEnviar.setEnabled(false);*/ } } );
        btnParar = findViewById(R.id.btnParar);
        btnParar.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { parar = true; /*btnEnviar.setEnabled(true);*/} } );
        img = findViewById(R.id.imageView);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.INTERNET},
                1);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                1);
        if(!checkPermissionForExternalStorage(this)) {
            requestPermissionForExternalStorage(this);
        } else {
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if(checkPermissionForExternalStorage(this)) {
            } else {
            }
        }
    }

    public boolean checkPermissionForExternalStorage(Activity activity) {
        if(Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void requestPermissionForExternalStorage(Activity activity) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(activity,
                    "External Storage permission needed. Please allow in App Settings for additional functionality.",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    public void onEnviar () {
        try {
            SERVER_IP = edtText.getText().toString();
            new Thread(new Runnable()
            {
                public void run() {
                    try {
                        InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                        connectedSocket = new Socket(serverAddr, SERVERPORT);
                    } catch (UnknownHostException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(connectedSocket !=null)
        {
            try {
                //et = (EditText) findViewById(R.id.EditText01);
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connectedSocket.getOutputStream())), true);
                new Thread(new Runnable() {
                    public void run() {
                        out.println(et.getText().toString());
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onReceber(){
        new Thread(new Runnable() {
            @Override
                public void run() {
                    int file_int = 1;
                    Socket connectedSocket = null;
                    try {
                        serverSocket = new ServerSocket(SERVERPORT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    do  {
                        try {
                            connectedSocket = serverSocket.accept();
                            int filesize = 6022386;
                            int bytesRead;
                            int current = 0;
                            File path = new File(Environment.getExternalStorageDirectory().toString()+"/Slides");
                            if (!path.exists())
                                path.mkdirs();
                            File file = new File(path, "foto"+file_int+".jpg");
                            byte[] mybytearray  = new byte [filesize];
                            InputStream is = connectedSocket.getInputStream();
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

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img.setImageBitmap(bmp);
                                }
                            });
                            file_int++;
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(parar)
                        {
                            serverSocket=null;
                            break;
                        }
                    }while(connectedSocket!=null);
                }
        }).start();
    }
}
