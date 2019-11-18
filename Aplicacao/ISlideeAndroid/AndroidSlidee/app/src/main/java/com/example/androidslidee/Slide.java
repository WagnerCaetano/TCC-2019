package com.example.androidslidee;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.DrawableRes;

public class Slide {

    String nome;
    Drawable imagem;

    public Slide(String nome, Drawable imagem) {
        this.nome = nome;
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Drawable getImagem() {
        return imagem;
    }

    public void setImagem(Drawable imagem) {
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
