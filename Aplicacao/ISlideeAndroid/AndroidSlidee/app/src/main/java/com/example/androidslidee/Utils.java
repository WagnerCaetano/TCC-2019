package com.example.androidslidee;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Utils {

    public static byte[] DrawableToBytes(Drawable img)
    {
        Bitmap bmp =((BitmapDrawable) img).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bmp.recycle();
        return byteArray;
    }

    public static Bitmap SlideImageToBitMap(Slide slide)
    {
        File img = slide.getImagem();
        Bitmap imagemCriada = BitmapFactory.decodeFile(img.getAbsolutePath());
        return imagemCriada;
    }
}
