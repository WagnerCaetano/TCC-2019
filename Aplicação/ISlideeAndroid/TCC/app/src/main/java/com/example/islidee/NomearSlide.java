package com.example.androidslidee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NomearSlide extends AppCompatActivity {

    private Button btnSalvar;
    private EditText nome;
    private ListView lista;
    private List<Slide> slides = new ArrayList<>();
    private Button voltar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nomear_slide);

        btnSalvar = findViewById(R.id.btnSalvar);
        nome = findViewById(R.id.txtNome);
        lista = findViewById(R.id.listaDeSlide);
        voltar = findViewById(R.id.btnVoltar);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), TelaManipuladora.class);
                startActivity(intent);
            }
        });

        for(int i=0; i<30; i++){
            slides.add(new Slide("Teste"+i, null));
        }

        final ArrayAdapter<Slide> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, slides);
        lista.setAdapter(adapter);

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
