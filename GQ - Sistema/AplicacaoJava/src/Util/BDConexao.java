/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author 00
 */
public class BDConexao {
    protected String server,usr,pwd,banco;
    
    public String getServer(){return this.server;}
    public void setServer(String S){this.server = S;}
    public String getUsr(){return this.usr;}
    public void setUsr(String U){this.usr = U;}
    public String getPwd(){return this.pwd;}
    public void setPwd(String P){this.pwd = P;}
    public String getBanco(){return this.banco;}
    public void setBanco(String B){this.banco = B;}
    
    public BDConexao(){this("localhost","BD_SistemaReclamacao","Admin","20092001");}
    public BDConexao(String Server,String Banco,String Usuario,String Senha){
        setServer(Server);
        setUsr(Usuario);
        setPwd(Senha);
        setBanco(Banco);
    }
    public String ToString(){
        return "";
    }
    
    public Connection criaConexao() throws SQLException,ClassNotFoundException{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://" + this.server + ";databaseName=" + this.banco+";user=" + this.usr +
                    ";password=" + this.pwd;
            return DriverManager.getConnection(url);
    }
}
