package com.example.islidee;

import android.media.Image;

public class Slide {

    String nome;
    Image imagem;

    public Slide(String nome, Image imagem) {
        this.nome = nome;
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Image getImagem() {
        return imagem;
    }

    public void setImagem(Image imagem) {
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
