package com.example.androidslidee;
import android.graphics.Bitmap;

import java.io.Serializable;

public class Slide implements Serializable {

    String nome;
    Bitmap imagem;

    public Slide(String nome, Bitmap imagem) {
        this.nome = nome;
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Bitmap getImagem() {
        return imagem;
    }

    public void setImagem(Bitmap imagem) {
        this.imagem = imagem;
    }

    @Override
    public String toString() {
        return "Slide{" +
                "nome='" + nome + '\'' +
                ", imagem=" + imagem +
                '}';
    }
}
