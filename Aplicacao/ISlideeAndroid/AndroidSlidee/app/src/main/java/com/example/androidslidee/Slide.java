package com.example.androidslidee;
import java.io.File;
import java.io.Serializable;

public class Slide implements Serializable {

    String nome;
    File imagem;

    public Slide(String nome, File imagem) {
        this.nome = nome;
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public File getImagem() {
        return imagem;
    }

    public void setImagem(File imagem) {
        this.imagem = imagem;
    }

    @Override
    public String toString() {
        return "Slide{" +
                "nome= '" + nome + '\'' +
                ", imagem= " + imagem +
                '}';
    }
}
