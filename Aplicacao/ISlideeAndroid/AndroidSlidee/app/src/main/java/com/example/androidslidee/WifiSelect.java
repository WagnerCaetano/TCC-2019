package com.example.androidslidee;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WifiSelect extends AppCompatActivity {

    private WifiManager wifiManager;
    private Button btnScan;
    private Button btnWifi;
    private ListView listViewIp;
    private TextView connectionString;

    ArrayList<String> ipList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_select);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        btnScan = findViewById(R.id.btnScan);
        btnWifi = findViewById(R.id.btnWifi);
        connectionString = findViewById(R.id.connectionStatus);
        listViewIp = findViewById(R.id.listView);

        if(!wifiManager.isWifiEnabled())
        {
            btnScan.setEnabled(false);
            connectionString.setText("Wifi desligado \n é preciso que esteja ligado");
        }
        else{
            btnScan.setEnabled(true);
            connectionString.setText("Wifi ligado");
        }

        btnWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!wifiManager.isWifiEnabled())
                {
                    wifiManager.setWifiEnabled(true);
                    btnWifi.setText("DESLIGAR WIFI");
                    btnScan.setEnabled(true);
                    connectionString.setText("Wifi ligado");
                }
                else{
                    wifiManager.setWifiEnabled(false);
                    btnWifi.setText("LIGAR WIFI");
                    btnScan.setEnabled(false);
                    connectionString.setText("Wifi desligado \n é preciso que esteja ligado");
                }
            }
        });
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ScanIpTask().execute();
            }
        });

        listViewIp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(listViewIp.getItemAtPosition(position)+"");
                entrar(listViewIp.getItemAtPosition(position).toString());
            }
        });

        ipList = new ArrayList();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, ipList);
        listViewIp.setAdapter(adapter);
    }

    private void entrar(String ip)
    {
        Intent intent = new Intent(this ,TelaManipuladora.class);
        intent.putExtra("IpSelecionado",ip.substring(17));
        startActivity(intent);
    }

    private class ScanIpTask extends AsyncTask<Void, String, Void>{

        String hostname = getIPAddress(true);
        String subnet = hostname.replaceAll("(.*\\.)\\d+$", "$1");;

        static final int lower = 1;
        static final int upper = 254;
        static final int timeout = 500;

        @Override
        protected void onPreExecute() {
            ipList.clear();
            adapter.notifyDataSetInvalidated();
            connectionString.setText("Escaneamento iniciado");

        }

        @Override
        protected Void doInBackground(Void... params) {
            System.out.println(subnet);
            for (int i = lower; i <= upper; i++) {
                String host = subnet + i;

                try {
                    InetAddress inetAddress = InetAddress.getByName(host);
                    if (inetAddress.isReachable(timeout)){
                        publishProgress("Dispositivo "+i+ " : "+inetAddress.toString());
                    }

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            ipList.add(values[0]);
            adapter.notifyDataSetInvalidated();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            connectionString.setText("Concluído escaneamento");
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
    }

}
