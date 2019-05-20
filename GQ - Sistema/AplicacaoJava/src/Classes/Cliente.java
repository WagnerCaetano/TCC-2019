/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Util.BDConexao;
import Util.CRUDTodos;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 *
 * @author u18300
 */
public class Cliente {
    private String nome;
    private int telefone;
    private String email;
    private String cep;
    private String endereco;
    private String cidade;
    private String senha;
    private String cpf;

    public Cliente(String nome, String email, String cep, String endereco, String cidade, String senha,String cpf,int telefone) {
        this.nome = nome;
        this.email = email;
        this.cep = cep;
        this.endereco = endereco;
        this.cidade = cidade;
        this.senha = senha;
        this.cpf = cpf;
        this.telefone = telefone;
    }
    public boolean notVazio()
    {
        return ( this.telefone >0 && this.cpf.length() > 0 && this.cep.length() > 0 && this.email.length() > 0 && this.cidade.length() > 0 && this.nome.length() > 0 && this.senha.length() > 0  && this.endereco.length() > 0);
    }
    
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenha() {
        return senha;
    }

    public int getTelefone() {
        return telefone;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.nome);
        hash = 73 * hash + Objects.hashCode(this.email);
        hash = 73 * hash + Objects.hashCode(this.cep);
        hash = 73 * hash + Objects.hashCode(this.endereco);
        hash = 73 * hash + Objects.hashCode(this.cidade);
        hash = 73 * hash + Objects.hashCode(this.senha);
        hash = 73 * hash + Objects.hashCode(this.cpf);
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
        final Cliente other = (Cliente) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.cep, other.cep)) {
            return false;
        }
        if (!Objects.equals(this.endereco, other.endereco)) {
            return false;
        }
        if (!Objects.equals(this.cidade, other.cidade)) {
            return false;
        }
        if (!Objects.equals(this.senha, other.senha)) {
            return false;
        }
        if (!Objects.equals(this.cpf, other.cpf)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Cliente{" + "nome= " + nome + ", email= " + email + ", cep= " + cep + ", endereco= " + endereco + ", cidade= " + cidade + '}';
    }
    
    
    
}
