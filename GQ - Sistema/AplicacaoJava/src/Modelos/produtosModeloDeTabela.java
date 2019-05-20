/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import Classes.*;
import java.util.List;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author u18300
 */
public class produtosModeloDeTabela extends AbstractTableModel{
    private static final long serialVersionUID = 1L;
    private List<Produto> prod;
    
    public produtosModeloDeTabela(List<Produto> prod) {
       this.prod = prod;
    }
    
    @Override
     public String getColumnName(int column) {
       switch (column) {
       case 0:
         return "ID";
       case 1:
         return "Produto";
       case 2:
         return "Garantia";
       }
       return "";
     }
     
    @Override
    public int getColumnCount() {
        return 3;
    }

    // devolve a quantidade de linhas
    @Override
    public int getRowCount() {
        return this.prod.size();
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
         Produto prod = this.prod.get(rowIndex);
         switch (columnIndex) {
         case 0:
         {
             return prod.getId();
         }
         case 1:
         {
             return prod.getNome();
         }
         case 2:
         {
             return prod.getGarantia();
         }
        }
         return null;
    }
    public void addRow(Produto e){
        prod.add(e);
    }
    
}
