package com.example.androidslidee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.byox.drawview.views.DrawView;

public class PaintView extends AppCompatActivity {

    byte[] byteArray;
    Button btnDesfazer;
    Button btnRefazer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_view);

        btnDesfazer = findViewById(R.id.btnUndo);
        btnRefazer = findViewById(R.id.btnRendo);

        final DrawView mDrawView;
        mDrawView = findViewById(R.id.draw_view);

        mDrawView.canRedo();
        mDrawView.canUndo();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            byteArray = extras.getByteArray("img");
            //mDrawView.setBackgroundImage(byteArray, BackgroundType.BYTES, BackgroundScale.CENTER_CROP);
        }

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
                // Your stuff here
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
    }
}
