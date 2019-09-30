/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.awt.Image;

/**
 *
 * @author u18325
 */
public class Slide {
    
    String Nome;
    Image image;
    
    public Slide(String nome, Image img){
        Nome = nome;
        image = img;
    }

    public String getNome() {
        return Nome;
    }

    public Image getImage() {
        return image;
    }

    public void setNome(String Nome) {
        this.Nome = Nome;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Slide{" + "Nome=" + Nome + ", image=" + image + '}';
    }
        
}
