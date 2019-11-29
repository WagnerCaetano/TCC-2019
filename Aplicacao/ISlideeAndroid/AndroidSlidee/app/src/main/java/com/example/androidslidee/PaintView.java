package com.example.androidslidee;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.byox.drawview.enums.BackgroundScale;
import com.byox.drawview.enums.BackgroundType;
import com.byox.drawview.views.DrawView;
@Deprecated
public class PaintView extends Activity {

    byte[] byteArray;
    Button btnDesfazer;
    Button btnRefazer;
    Client wireless;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_view);

        btnDesfazer = findViewById(R.id.btnUndo);
        btnRefazer = findViewById(R.id.btnRendo);
        wireless = (Client) getIntent().getExtras().getSerializable("Wireless");
        Bitmap Image = (Bitmap) getIntent().getExtras().get("ImageBitmap");

        final DrawView mDrawView;
        mDrawView = findViewById(R.id.draw_view);

        mDrawView.canRedo();
        mDrawView.canUndo();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            byteArray = extras.getByteArray("img");
            mDrawView.setBackgroundImage(Image,BackgroundType.BITMAP,BackgroundScale.CENTER_CROP);
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
    }
}