package com.example.islidee;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.method.Touch;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView cronometro;
    private ImageButton btnPlay;
    private Handler handler;
    private ImageButton cronoConfig;
    private String tempoLimite;

    private Button btnCursor;
    private ImageView cursor;
    private ImageView slide;

    private static long initialTime;
    public static boolean isRunning;
    private static final long MILLIS_IN_SEC = 1000L;
    private static final int SECS_IN_MIN = 60;

    private boolean listener = false;

    TimePickerDialog.OnTimeSetListener mOnTimeSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        slide = findViewById(R.id.ivSlide);

        btnCursor = findViewById(R.id.btnCursor);
        cursor = findViewById(R.id.cursor);
        btnPlay = findViewById(R.id.imPlay);

        handler = new Handler();
        cronoConfig = findViewById(R.id.cronoConfig);
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

        cronoConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCalendar =  Calendar.getInstance();
                int minute = mCalendar.get(Calendar.MINUTE);
                int second = mCalendar.get(Calendar.SECOND);


                DurationPicker mTimePickerDialog = new DurationPicker(
                        MainActivity.this, mOnTimeSetListener, minute,second);
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

                Toast.makeText(MainActivity.this, "Você será notificado quando atingir " + mTime + " min de apresentação", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "Tempo limite atingido!", Toast.LENGTH_SHORT).show();
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
            return true;
        }
    };
}
