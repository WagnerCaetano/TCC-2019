package com.example.androidslidee;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Tela_Inicial extends Activity {

    private TextView ip;
    private Button btnConectar;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_select);
        ip = findViewById(R.id.txtIp);
        btnConectar = findViewById(R.id.btnConectar);
        btnConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ip.getText().length()>0)
                    entrar(ip.getText().toString());
            }
        });

        ActivityCompat.requestPermissions(Tela_Inicial.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        ActivityCompat.requestPermissions(Tela_Inicial.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        ActivityCompat.requestPermissions(Tela_Inicial.this,
                new String[]{Manifest.permission.INTERNET},
                1);
        ActivityCompat.requestPermissions(Tela_Inicial.this,
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

    private void entrar(String ip)
    {
        Intent intent = new Intent(this , Tela_Manipuladora.class);
        intent.putExtra("IpSelecionado",ip);
        startActivity(intent);
    }

}
