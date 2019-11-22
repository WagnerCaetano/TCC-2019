package com.example.androidslidee;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TelaManipuladora extends AppCompatActivity {
    // CONFIGURACOES
    private TextView cronometro;
    private ImageButton btnPlay;
    private Handler handler;
    private static long initialTime;
    public static boolean isRunning;
    private static final long MILLIS_IN_SEC = 1000L;
    private static final int SECS_IN_MIN = 60;
    private boolean listener = false;
    TimePickerDialog.OnTimeSetListener mOnTimeSetListener;

    // STRINGS
    private String tempoLimite;
    private String ip;

    // BUTTONS
    private Button btnDraw;
    private Button btnCursor;
    private ImageButton cronoConfig;
    private Button btnSlides;
    private Button btnAvancar;
    private Button btnVoltar;

    // SLIDES
    private ListView lista;
    private List<Slide> slides = new ArrayList<>();
    private int INDICE_SLIDE = 0;
    private ImageView slideView;
    private ImageView cursor;

    // CLASSES
    private ClientWifi wireless = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manipuladora);
        // CRONOMETRO
        handler = new Handler();
        cronometro = findViewById(R.id.txtCronometro);
        btnPlay = findViewById(R.id.imPlay);
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
        cronoConfig = findViewById(R.id.cronoConfig);
        cronoConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCalendar =  Calendar.getInstance();
                int minute = mCalendar.get(Calendar.MINUTE);
                int second = mCalendar.get(Calendar.SECOND);
                DurationPicker mTimePickerDialog = new DurationPicker(
                        TelaManipuladora.this, mOnTimeSetListener, minute,second);
                mTimePickerDialog.setTitle("Configurar cronômetro");
                mTimePickerDialog.setMessage("Me avise quando o tempo chegar em:");
                mTimePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mTimePickerDialog.show();
            }
        });
        mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int minute, int second) {
                int sec = second;
                String min = minute+"";
                String secS = second+"";

                if(minute > 0)
                    sec = sec + (minute * 60);

                if(minute < 10)
                    min = "0" + minute;

                tempoLimite = sec+"";

                if(second < 10)
                    secS = "0" + second;

                String mTime = min+":"+secS;

                Toast.makeText(TelaManipuladora.this, "Você será notificado quando atingir " + mTime + " min de apresentação", Toast.LENGTH_SHORT).show();
            }
        };

        // INTENT
        Intent intent = getIntent();
        ip = intent.getStringExtra("IpSelecionado");
        wireless = new ClientWifi(ip,slides);

        //SLIDES
        slideView = findViewById(R.id.ivSlide);
        final SlideAdapter adapter =  new SlideAdapter(this, slides);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Slide slide = adapter.getItem(position);
                INDICE_SLIDE = position;
                slideView.setImageBitmap(slide.getImagem());
            }
        });
        slideView.setImageBitmap(slides.get(INDICE_SLIDE).getImagem());
        btnSlides = findViewById(R.id.btnSlides);
        btnSlides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NomearSlide.class);
                intent.putExtra("ListaSlides", (Serializable) slides);
                startActivity(intent);
            }
        });
        btnAvancar = findViewById(R.id.btnAvancar);
        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(INDICE_SLIDE<=slides.size()) {
                    INDICE_SLIDE++;
                    slideView.setImageBitmap(slides.get(INDICE_SLIDE).getImagem());
                }
            }
        });
        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(INDICE_SLIDE>=1){
                    INDICE_SLIDE--;
                    slideView.setImageBitmap(slides.get(INDICE_SLIDE).getImagem());
                }
            }
        });
        btnCursor = findViewById(R.id.btnCursor);
        btnCursor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(listener){
                    cursor.setVisibility(View.INVISIBLE);
                }
                slideView.setOnTouchListener(handleTouch);
                listener = !listener;
            }
        });
        cursor = findViewById(R.id.cursor);
        btnDraw = findViewById(R.id.btnDraw);
        btnDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PaintActivity.class);
                Toast.makeText(getApplicationContext(), "Para sair do modo desenho, pressione o botão de Voltar", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });
    }
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                long seconds = (System.currentTimeMillis() - initialTime) / MILLIS_IN_SEC;
                cronometro.setText((String.format("%02d:%02d", seconds / SECS_IN_MIN, seconds % SECS_IN_MIN)));
                String.format(String.valueOf(seconds));
                System.out.println(seconds);
                System.out.println(tempoLimite);
                if(String.format(String.valueOf(seconds)).equals(tempoLimite)){
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    long milliseconds = 2000;
                    vibrator.vibrate(milliseconds);
                    Toast.makeText(TelaManipuladora.this, "Tempo limite atingido!", Toast.LENGTH_SHORT).show();
                }
                handler.postDelayed(runnable, MILLIS_IN_SEC);
            }
        }
    };
    private View.OnTouchListener handleTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            cursor.setVisibility(View.VISIBLE);

            cursor.setX(event.getX());
            cursor.setY(event.getY());
            try {
                wireless.enviarMensagem("CURSOR\n"+event.getX()+"\n"+event.getY());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
    };
}

