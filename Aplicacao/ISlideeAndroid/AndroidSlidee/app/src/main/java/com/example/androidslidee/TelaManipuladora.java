package com.example.androidslidee;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class TelaManipuladora extends AppCompatActivity {

    //Controles do usuário
    private ImageButton btnPlay;
    private ImageButton cronoConfig;
    private Button btnDraw;
    private Button btnCursor;
    private Button btnSlides;
    private Button btnZoom;
    private ImageView slide;
    private ImageView cursor;

    //Elementos auxiliares (para o desenvolvedor)
    private TextView cronometro;
    private Handler handler;
    private String ip;
    private String tempoLimite;
    private static long initialTime;
    public static boolean isRunning;
    private static final long MILLIS_IN_SEC = 1000L;
    private static final int SECS_IN_MIN = 60;
    private boolean listener = false;
    private Wireless wireless;

    //Lista dos slides
    private ListView lista;
    private List<Slide> slides = new ArrayList<>();


    TimePickerDialog.OnTimeSetListener mOnTimeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manipuladora);
        Intent intent = getIntent();
        ip = intent.getStringExtra("IpSelecionado");
        wireless = new Wireless(ip);

        slide = findViewById(R.id.ivSlide);
        cronoConfig = findViewById(R.id.cronoConfig);
        btnSlides = findViewById(R.id.btnSlides);
        btnCursor = findViewById(R.id.btnCursor);
        cursor = findViewById(R.id.cursor);
        btnPlay = findViewById(R.id.imPlay);
        handler = new Handler();
        cronometro = findViewById(R.id.txtCronometro);
        btnZoom = findViewById(R.id.btnZoom);
        btnDraw = findViewById(R.id.btnDraw);
        lista = findViewById(R.id.listaSlides);


        for(int i=0;i<10;i++)
            slides.add(new Slide("teste"+i, null));


        final ListaSlideAdapter adapter =  new ListaSlideAdapter(this, slides);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                slide.setImageDrawable(slides.get(position).imagem);
             }
            }
        );


        btnZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhotoViewAttacher photoView = new PhotoViewAttacher(slide);
                photoView.update();
            }
        });

        btnDraw.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                Resources res = getResources();

                Bitmap bitmap = BitmapFactory.decodeResource(res, slide.getImageAlpha());
                ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                //bitmap.compress(Bitmap.CompressFormat.PNG, 50, _bs);

                Intent intent = new Intent(getApplicationContext(), PaintView.class);
                intent.putExtra("img", _bs.toByteArray());
                startActivity(intent);

            }
        });

        btnSlides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NomearSlide.class);
                startActivity(intent);
            }
        });

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

        btnCursor.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                if(listener){

                    cursor.setVisibility(View.INVISIBLE);
                }
                slide.setOnTouchListener(handleTouch);

                listener = !listener;
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
            //wireless.sendCursor(Integer.parseInt(event.getX()+""),Integer.parseInt(event.getY()+""));
            return true;
        }
    };
}

