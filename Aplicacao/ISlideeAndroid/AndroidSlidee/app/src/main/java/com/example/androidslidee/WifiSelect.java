package com.example.androidslidee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WifiSelect extends AppCompatActivity {

    private TextView ip;
    private Button btnConectar;

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

    }

    private void entrar(String ip)
    {
        Intent intent = new Intent(this ,TelaManipuladora.class);
        intent.putExtra("IpSelecionado",ip);
        startActivity(intent);
    }

}
