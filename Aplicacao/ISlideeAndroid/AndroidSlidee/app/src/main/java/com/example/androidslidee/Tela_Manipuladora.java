package com.example.androidslidee;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.content.res.ResourcesCompat;
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

import com.byox.drawview.enums.BackgroundScale;
import com.byox.drawview.enums.BackgroundType;
import com.byox.drawview.views.DrawView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Tela_Manipuladora extends Activity {
    // CONFIGURACOES
    private TextView cronometro;
    private ImageButton btnPlay;
    private Handler handler;
    private static long initialTime;
    public static boolean isRunning;
    private static final long MILLIS_IN_SEC = 1000L;
    private static final int SECS_IN_MIN = 60;
    private boolean listener = false;
    private boolean draw = false;
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
    private Button btnDesfazer;
    private Button btnRefazer;

    // SLIDES
    private ListView lista;
    private List<Slide> slides = new ArrayList<>();
    private int INDICE_SLIDE = 0;
    private ImageView slideView;
    private ImageView cursor;

    // CLASSES
    private Client wireless = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manipuladora);

        /*DRAW VIEW*/
        btnDesfazer = findViewById(R.id.btnUndo);
        btnDesfazer.setEnabled(false);
        btnRefazer = findViewById(R.id.btnRendo);
        btnRefazer.setEnabled(false);
        final DrawView mDrawView;
        mDrawView = findViewById(R.id.draw_view);
        mDrawView.canRedo();
        mDrawView.canUndo();
        mDrawView.setEnabled(false);
        //mDrawView.setBackgroundImage(Utils.DrawableToBytes(slideView.getDrawable()), BackgroundType.BYTES, BackgroundScale.CENTER_CROP);
        btnDesfazer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.undo();
            }
        });

        btnRefazer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.redo();
            }
        });

        mDrawView.setOnDrawViewListener(new DrawView.OnDrawViewListener() {
            @Override
            public void onStartDrawing() {
                // Your stuff here
            }
            @Override
            public void onEndDrawing() {
                /*Bitmap bmp = ((BitmapDrawable)mDrawView.getBackground().getCurrent()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                bmp.recycle();*/
                Drawable draw = mDrawView.getBackground().getCurrent();
                byte [] byteArray = Utils.DrawableToBytes(draw);
                wireless.enviarImagem(byteArray);
            }
            @Override
            public void onClearDrawing() {
                // Your stuff here
            }
            @Override
            public void onRequestText() {
                // Your stuff here
            }
            @Override
            public void onAllMovesPainted() {
                // Your stuff here
            }
        });
        /*DRAW VIEW*/
        btnDraw = findViewById(R.id.btnDraw);
        btnDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setBackgroundImage(Utils.DrawableToBytes(slideView.getDrawable()), BackgroundType.BYTES, BackgroundScale.CENTER_CROP);
                mDrawView.setEnabled(!draw);
                btnDesfazer.setEnabled(!draw);
                btnRefazer.setEnabled(!draw);
                draw = !draw;
                /*Intent intent = new Intent(getApplicationContext(),PaintView.class);
                intent.putExtra("ImageBitmap",bitmap);
                intent.putExtra("Wireless", (Serializable) wireless);
                startActivity(intent);*/
            }
        });



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
                        Tela_Manipuladora.this, mOnTimeSetListener, minute,second);
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
                Toast.makeText(Tela_Manipuladora.this, "Você será notificado quando atingir " + mTime + " min de apresentação", Toast.LENGTH_SHORT).show();
            }
        };

        // INTENT
        Intent intent = getIntent();
        ip = intent.getStringExtra("IpSelecionado");

        // SLIDES
        slideView = findViewById(R.id.ivSlide);
        slideView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.inicial, null));
        final Adapter_Slide adapter =  new Adapter_Slide(this, slides);
        lista = findViewById(R.id.listaSlides);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i = INDICE_SLIDE;
                INDICE_SLIDE = position;
                int deltaPosition = 1;
                if (INDICE_SLIDE > i) {
                    deltaPosition += INDICE_SLIDE - i;
                    int x = 0;
                    do{
                        wireless.avancar();
                        x++;
                    }while(x < deltaPosition);

                }
                else {
                    deltaPosition +=  i - INDICE_SLIDE;
                    int x = 0;
                    do {
                        wireless.recuar();
                        x++;
                    }while(x < deltaPosition);
                }
                slideView.setImageBitmap(Utils.SlideImageToBitMap(adapter.getItem(position)));
                System.out.println(INDICE_SLIDE);
            }
        });
        wireless = new Client(ip,adapter);
        btnZoom = findViewById(R.id.btnZoom);
        btnZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhotoViewAttacher photoView = new PhotoViewAttacher(slideView);
                photoView.update();
                wireless.enviarImagem(Utils.DrawableToBytes(photoView.getImageView().getDrawable()));
            }
        });
        btnSlides = findViewById(R.id.btnSlides);
        btnSlides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Tela_NomearSlide.class);
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
                    slideView.setImageBitmap(Utils.SlideImageToBitMap(slides.get(INDICE_SLIDE)));
                    wireless.avancar();
                    System.out.println(INDICE_SLIDE);
                }
            }
        });
        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(INDICE_SLIDE>=1){
                    INDICE_SLIDE--;
                    slideView.setImageBitmap(Utils.SlideImageToBitMap(slides.get(INDICE_SLIDE)));
                    wireless.recuar();
                    System.out.println(INDICE_SLIDE);
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
                    Toast.makeText(Tela_Manipuladora.this, "Tempo limite atingido!", Toast.LENGTH_SHORT).show();
                }
                handler.postDelayed(runnable, MILLIS_IN_SEC);
            }
        }
    };
    private View.OnTouchListener handleTouch = new View.OnTouchListener() {
        private Rect rect;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
            }
            if(event.getAction() == MotionEvent.ACTION_MOVE){
                if(rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                    cursor.setVisibility(View.VISIBLE);
                    cursor.setX(event.getX());
                    cursor.setY(event.getY());
                    String X = (int) event.getX()+"";
                    String Y = (int) event.getY()+"";
                    if(X.trim().length()>0 && Y.trim().length()>0)
                        wireless.enviarMensagem("CURSOR\n"+X.trim()+"\n"+Y.trim());
                    return true;
                }
            }
            return false;

        }
    };
}

