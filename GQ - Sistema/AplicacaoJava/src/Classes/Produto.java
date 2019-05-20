/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 *
 * @author u18300
 */
public class Produto {
    private int Id;
    private String nome;
    private String garantia;

    public Produto(String nome, String garantia) {
        this.nome = nome;
        this.garantia = garantia;
    }
    public boolean notVazio()
    {
        return(this.nome.length() <=0 && this.garantia.length() <=0);
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGarantia() {
        return garantia;
    }

    public void setGarantia(String garantia) {
        this.garantia = garantia;
    }

    @Override
    public String toString() {
        return "Produto{" + "nome=" + nome + ", garantia=" + garantia + '}';
    }
}
