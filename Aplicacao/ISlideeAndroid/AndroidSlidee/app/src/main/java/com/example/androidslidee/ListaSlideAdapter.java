package com.example.androidslidee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListaSlideAdapter extends ArrayAdapter<Slide> {

        private Context context;
        private List<Slide> slides = null;

        public ListaSlideAdapter(Context context,  List<Slide> slides) {
            super(context,0, slides);
            this.slides = slides;
            this.context = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            Slide slide = slides.get(position);

            if(view == null)
                view = LayoutInflater.from(context).inflate(R.layout.activity_manipuladora, null);

            /*ImageView imageViewSlide = view.findViewById(R.id.image_view_slide);
            imageViewSlide.setImageDrawable(slide.imagem);

            TextView textViewNomeSlide = view.findViewById(R.id.text_view_nome_slide);
            textViewNomeSlide.setText(slide.getNome());*/

            return view;
        }
    }
