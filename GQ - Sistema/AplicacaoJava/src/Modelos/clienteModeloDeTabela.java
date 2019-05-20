/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import Classes.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author u18300
 */
public class clienteModeloDeTabela extends AbstractTableModel{
    private static final long serialVersionUID = 1L;
    private List<Cliente> clientes;
    
    public clienteModeloDeTabela(List<Cliente> clientes) {
       this.clientes = clientes;
    }
    
    @Override
     public String getColumnName(int column) {
       switch (column) {
       case 0:
         return "CPF";
       case 1:
         return "Nome";
       case 2:
         return "Email";
       case 3:
         return "Endereco";
       case 4:
         return "CEP";
       }
       return "";
     }
     
    @Override
    public int getColumnCount() {
        return 5;
    }

    // devolve a quantidade de linhas
    @Override
    public int getRowCount() {
        return this.clientes.size();
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
         Cliente cliente = this.clientes.get(rowIndex);
         switch (columnIndex) {
         case 0:
            return cliente.getCpf();
         case 1:
            return cliente.getNome();
         case 2:
            return cliente.getEmail();
         case 3:
            return cliente.getEndereco();
         case 4:
           return cliente.getCep();
         }
        return null;
    }
    public void addRow(Cliente e){
        clientes.add(e);
    }
    
}
