/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.Objects;

/**
 *
 * @author 00
 */
public class Causa {
    private String causa;

    public Causa(String causa) {
        this.causa = causa;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.causa);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Causa other = (Causa) obj;
        if (!Objects.equals(this.causa, other.causa)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Causa{" + "causa= " + causa + '}';
    }
    
    
    
    
}
