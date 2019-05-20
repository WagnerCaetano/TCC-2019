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
public class reclamacoesModeloDeTabela extends AbstractTableModel{
    private static final long serialVersionUID = 1L;
    private List<FichaReclamacao> ficha;
    
    public reclamacoesModeloDeTabela(List<FichaReclamacao> ficha) {
       this.ficha = ficha;
    }
    
    @Override
     public String getColumnName(int column) {
       switch (column) {
       case 0:
         return "ID";
       case 1:
         return "CPF Cliente";
       case 2:
         return "Nome Cliente";
       case 3:
         return "Tipo";
       case 4:
         return "Data Emicao";
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
        return this.ficha.size();
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
         FichaReclamacao ficha = this.ficha.get(rowIndex);
         switch (columnIndex) {
         case 0:
         {
             return ficha.getFichaCodigo();
         }
         case 1:
         {
             try {
                 return ficha.getClienteCpf();
             } catch (SQLException | ClassNotFoundException ex) {
                 Logger.getLogger(reclamacoesModeloDeTabela.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         case 2:
         {
             try {
                 return ficha.getClienteNome();
             } catch (SQLException | ClassNotFoundException ex) {
                 Logger.getLogger(reclamacoesModeloDeTabela.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         case 3:
           return ficha.getDescricao();
         case 4:
           return ficha.getDataEmicao();
         }
        return null;
    }
    public void addRow(FichaReclamacao e){
        ficha.add(e);
    }
    
}
