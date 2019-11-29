package com.example.androidslidee;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class TelaManipuladora extends Activity {
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
    private Button btnZoom;

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

        // SLIDES
        slideView = findViewById(R.id.ivSlide);
        slideView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.inicial, null));
        final SlideAdapter adapter =  new SlideAdapter(this, slides);
        lista = findViewById(R.id.listaSlides);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i = INDICE_SLIDE;
                INDICE_SLIDE = position;
                int deltaPosition = 0;
                if (INDICE_SLIDE > i) {
                    deltaPosition = INDICE_SLIDE - i;
                    for(int x = 0 ; x < deltaPosition;x++)
                        wireless.avancar();
                }
                else {
                    deltaPosition =  i - INDICE_SLIDE;
                    for(int x = 0 ; x < deltaPosition;x++)
                        wireless.recuar();
                }
                slideView.setImageBitmap(SlideImageToBitMap(adapter.getItem(position)));
            }
        });
        wireless = new ClientWifi(ip,adapter);
        btnZoom = findViewById(R.id.btnZoom);
        btnZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhotoViewAttacher photoView = new PhotoViewAttacher(slideView);
                photoView.update();
                wireless.zoom(photoView.getScale(),INDICE_SLIDE);

            }
        });
        btnSlides = findViewById(R.id.btnSlides);
        btnSlides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NomearSlide.class);
                intent.putExtra("ListaSlides", (Serializable) slides);
                startActivityForResult(intent,200);
            }
        });
        btnAvancar = findViewById(R.id.btnAvancar);
        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(INDICE_SLIDE<slides.size()-1) {
                    INDICE_SLIDE++;
                    slideView.setImageBitmap(SlideImageToBitMap(slides.get(INDICE_SLIDE)));
                    wireless.avancar();
                }
            }
        });
        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(INDICE_SLIDE>=1){
                    INDICE_SLIDE--;
                    slideView.setImageBitmap(SlideImageToBitMap(slides.get(INDICE_SLIDE)));
                    wireless.recuar();
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
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Resources res = getResources();
                Bitmap bitmap = BitmapFactory.decodeResource(res, slideView.getImageAlpha());
                Intent intent = new Intent(getApplicationContext(),PaintView.class);
                intent.putExtra("ImageBitmap",bitmap);
                intent.putExtra("Wireless", (Serializable) wireless);
                startActivity(intent);
            }
        });
    }

    private Bitmap SlideImageToBitMap(Slide slide)
    {
        File img = slide.getImagem();
        Bitmap imagemCriada = BitmapFactory.decodeFile(img.getAbsolutePath());
        return imagemCriada;
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
            wireless.enviarMensagem("CURSOR\n"+event.getX()+"\n"+event.getY());
            return true;
        }
    };
}

