package com.example.islidee;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView cronometro;
    private ImageButton btnPlay;
    private Handler handler;
    private ImageButton cronoConfig;
    private int tempoLimite;

    private static long initialTime;
    public static boolean isRunning;
    private static final long MILLIS_IN_SEC = 1000L;
    private static final int SECS_IN_MIN = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cronoConfig = findViewById(R.id.cronoConfig);

        cronoConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configurarCronometro();
            }
        });

        btnPlay = findViewById(R.id.imPlay);
        handler = new Handler();
        cronometro = findViewById(R.id.txtCronometro);

        btnPlay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                ImageButton fab = (ImageButton)view;

                if(!isRunning) {
                    isRunning = true;
                    initialTime=(System.currentTimeMillis());
                    fab.setImageResource(R.drawable.pause);
                    handler.postDelayed(runnable, MILLIS_IN_SEC);
                }else{
                    isRunning = false;
                    fab.setImageResource(R.drawable.play);
                    handler.removeCallbacks(runnable);
                    cronometro.setText("00:00");
                }
            }
        });

        btnPlay.setVisibility(View.INVISIBLE);
        cronometro.setVisibility(View.INVISIBLE);
    }
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                long seconds = (System.currentTimeMillis() - initialTime) / MILLIS_IN_SEC;
                cronometro.setText((String.format("%02d:%02d", seconds / SECS_IN_MIN, seconds % SECS_IN_MIN)));
                handler.postDelayed(runnable, MILLIS_IN_SEC);
            }
        }
    };

    private void configurarCronometro(){
        AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
        msgBox.setTitle("Configurar o cronômetro");
        msgBox.setIcon(android.R.drawable.ic_menu_set_as);
        msgBox.setMessage("Defina o momento de alerta do seu cronometro");
        msgBox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                btnPlay.setVisibility(View.VISIBLE);
                cronometro.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Você será notificado quando atingir " + tempoLimite + " min", Toast.LENGTH_SHORT).show();
            }
        });

        msgBox.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

}
