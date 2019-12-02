package com.example.androidslidee;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Utils {

    public static Bitmap ImageViewToBitmap(ImageView imageView)
    {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return bitmap;
    }
    public static Bitmap DrawableToBitmap (Drawable d)
    {
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return bitmap;
    }

    public static Bitmap SlideImageToBitMap(Slide slide)
    {
        File img = slide.getImagem();
        Bitmap imagemCriada = BitmapFactory.decodeFile(img.getAbsolutePath());
        return imagemCriada;
    }
}
