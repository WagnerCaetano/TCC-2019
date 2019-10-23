package com.example.androidsocketsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText el;
    String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        el = (EditText)findViewById(R.id.edtSend);
        try {
            ip = findRightIp();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String findRightIp() throws IOException {
        Socket s;
        String hostname = getIPAddress(true);
        String IpWithNoFinalPart = hostname.replaceAll("(.*\\.)\\d+$", "$1");


        for (int i = 1;i<=255;i++)
        {
            String ip = IpWithNoFinalPart+i;
            s = new Socket(ip,7800);
            if(s.isConnected())
                return IpWithNoFinalPart+i;
        }
        return null;

    }
    public String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return null;
    }

    public void send (View v)
    {
        MessageSender messageSender = new MessageSender(ip);
        messageSender.execute(el.getText().toString());
    }


}
