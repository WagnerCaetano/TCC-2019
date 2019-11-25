package com.example.androidslidee;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SlideAdapter extends ArrayAdapter<Slide> {
    private Context context;
    private List<Slide> slides;

    public SlideAdapter(Context context, List<Slide> objects) {
        super(context, 0, objects);
        this.context = context;
        this.slides = objects;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Slide slide = slides.get(position);

        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_list_slide, null);

        ImageView imageViewZombie = view.findViewById(R.id.image_view_slide);
        Bitmap imagemCriada = BitmapFactory.decodeFile(slide.getImagem().getAbsolutePath());
        imageViewZombie.setImageBitmap(imagemCriada);

        TextView textViewNomeZombie = view.findViewById(R.id.text_view_nome_slide);
        textViewNomeZombie.setText(slide.getNome());

        return view;
    }
}
