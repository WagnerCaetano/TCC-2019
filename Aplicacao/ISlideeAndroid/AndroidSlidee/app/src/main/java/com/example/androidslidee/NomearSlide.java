package com.example.androidslidee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class NomearSlide extends AppCompatActivity {

    private Button btnSalvar;
    private EditText nome;
    private Button btnVoltar;
    private ListView lista;
    private List<Slide> slides = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nomear_slide);

        lista = findViewById(R.id.listaSlides);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnVoltar = findViewById(R.id.btnVoltar);
        nome = findViewById(R.id.txtNome);

        for(int i=0;i<10;i++)
            slides.add(new Slide("teste"+i, null));


        final ListaSlideAdapter adapter =  new ListaSlideAdapter(this, slides);
        lista.setAdapter(adapter);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TelaManipuladora.class);
                startActivity(intent);
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                nome.setEnabled(true);

                nome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        btnSalvar.setEnabled(true);

                        btnSalvar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                slides.get(position).setNome(nome.getText() + "");
                                lista.setAdapter(adapter);
                            }
                        });
                    }
                });
            }
        });
    }
}
